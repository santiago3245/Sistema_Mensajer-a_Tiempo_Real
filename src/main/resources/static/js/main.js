/**
 * Sistema de Mensajería en Tiempo Real - Frontend JavaScript
 * Implementación del cliente WebSocket usando STOMP sobre SockJS
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */

'use strict';

// ===========================
// Variables Globales
// ===========================
let stompClient = null;
let username = null;
let isConnected = false;
let typingTimer = null;
let typingTimeout = 3000; // 3 segundos

// ===========================
// Elementos del DOM
// ===========================
const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const messageArea = document.querySelector('#messageArea');
const logoutBtn = document.querySelector('#logout-btn');
const clearChatBtn = document.querySelector('#clear-chat-btn');
const connectedUserName = document.querySelector('#connected-user-name');
const usersCount = document.querySelector('#users-count');
const typingIndicator = document.querySelector('#typing-indicator');

// ===========================
// Colores para Avatares
// ===========================
const colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0',
    '#9c27b0', '#673ab7', '#3f51b5', '#0097a7'
];

// ===========================
// Event Listeners
// ===========================
usernameForm.addEventListener('submit', onUsernameSubmit, true);
messageForm.addEventListener('submit', onMessageSubmit, true);
messageInput.addEventListener('input', onUserTyping, true);
logoutBtn.addEventListener('click', onLogout, true);
clearChatBtn.addEventListener('click', onClearChat, true);

// ===========================
// Función: Envío de Username
// ===========================
function onUsernameSubmit(event) {
    event.preventDefault();
    username = document.querySelector('#name').value.trim();
    
    if (username) {
        // Validar longitud del username
        if (username.length < 2 || username.length > 50) {
            showToast('El nombre debe tener entre 2 y 50 caracteres', 'error');
            return;
        }
        
        // Ocultar página de username y mostrar página de chat
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        
        // Mostrar nombre de usuario conectado
        connectedUserName.textContent = username;
        
        // Conectar al WebSocket
        connect();
    }
}

// ===========================
// Función: Conectar WebSocket
// ===========================
function connect() {
    if (isConnected) {
        console.log('Ya está conectado al servidor');
        return;
    }
    
    // Crear conexión SockJS
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    
    // Configurar reconexión automática
    stompClient.reconnect_delay = 5000;
    
    // Headers de conexión
    const headers = {
        username: username
    };
    
    // Conectar
    stompClient.connect(headers, onConnected, onError);
    
    console.log('Intentando conectar al servidor WebSocket...');
}

// ===========================
// Función: Callback de Conexión Exitosa
// ===========================
function onConnected() {
    isConnected = true;
    console.log('Conectado al servidor WebSocket');
    
    // Suscribirse al canal público
    stompClient.subscribe('/topic/public', onMessageReceived);
    
    // Suscribirse al canal de conteo de usuarios
    stompClient.subscribe('/topic/userCount', onUserCountUpdate);
    
    // Notificar al servidor sobre el nuevo usuario
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({
            sender: username,
            type: 'JOIN'
        })
    );
    
    showToast('Conectado exitosamente al chat', 'success');
}

// ===========================
// Función: Callback de Error de Conexión
// ===========================
function onError(error) {
    isConnected = false;
    console.error('Error de conexión:', error);
    
    showToast('Error al conectar con el servidor. Reintentando...', 'error');
    
    // Intentar reconectar después de 5 segundos
    setTimeout(() => {
        if (!isConnected) {
            connect();
        }
    }, 5000);
}

// ===========================
// Función: Envío de Mensaje
// ===========================
function onMessageSubmit(event) {
    event.preventDefault();
    
    const messageContent = messageInput.value.trim();
    
    if (messageContent && stompClient && isConnected) {
        // Validar longitud del mensaje
        if (messageContent.length > 500) {
            showToast('El mensaje no puede exceder 500 caracteres', 'error');
            return;
        }
        
        const chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };
        
        // Enviar mensaje al servidor
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        
        // Limpiar input
        messageInput.value = '';
        
        // Detener indicador de escritura
        clearTimeout(typingTimer);
    } else if (!isConnected) {
        showToast('No estás conectado al servidor', 'error');
    }
}

// ===========================
// Función: Usuario Escribiendo
// ===========================
function onUserTyping() {
    if (!stompClient || !isConnected) return;
    
    clearTimeout(typingTimer);
    
    // Enviar notificación de escritura
    stompClient.send("/app/chat.typing", {}, JSON.stringify({
        sender: username,
        type: 'TYPING'
    }));
    
    typingTimer = setTimeout(() => {
        // El usuario dejó de escribir
    }, typingTimeout);
}

// ===========================
// Función: Recepción de Mensaje
// ===========================
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    
    console.log('Mensaje recibido:', message);
    
    switch (message.type) {
        case 'CHAT':
            displayChatMessage(message);
            break;
        case 'JOIN':
            displayEventMessage(message, 'join');
            break;
        case 'LEAVE':
            displayEventMessage(message, 'leave');
            break;
        case 'TYPING':
            if (message.sender !== username) {
                displayTypingIndicator(message.sender);
            }
            break;
        default:
            console.log('Tipo de mensaje desconocido:', message.type);
    }
}

// ===========================
// Función: Mostrar Mensaje de Chat
// ===========================
function displayChatMessage(message) {
    const messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');
    
    // Añadir clase si es mensaje propio
    if (message.sender === username) {
        messageElement.classList.add('own-message');
    }
    
    // Crear avatar
    const avatarElement = document.createElement('div');
    avatarElement.classList.add('message-avatar');
    avatarElement.textContent = message.sender.charAt(0).toUpperCase();
    avatarElement.style.backgroundColor = getAvatarColor(message.sender);
    
    // Crear header del mensaje
    const headerElement = document.createElement('div');
    headerElement.classList.add('message-header');
    
    const senderElement = document.createElement('span');
    senderElement.classList.add('message-sender');
    senderElement.textContent = message.sender;
    
    const timeElement = document.createElement('span');
    timeElement.classList.add('message-time');
    timeElement.textContent = formatTimestamp(message.timestamp);
    
    headerElement.appendChild(avatarElement);
    headerElement.appendChild(senderElement);
    headerElement.appendChild(timeElement);
    
    // Crear contenido del mensaje
    const contentElement = document.createElement('div');
    contentElement.classList.add('message-content');
    contentElement.textContent = message.content;
    
    // Ensamblar mensaje
    messageElement.appendChild(headerElement);
    messageElement.appendChild(contentElement);
    
    messageArea.appendChild(messageElement);
    
    // Scroll al último mensaje
    messageArea.scrollTop = messageArea.scrollHeight;
}

// ===========================
// Función: Mostrar Mensaje de Evento
// ===========================
function displayEventMessage(message, type) {
    const messageElement = document.createElement('li');
    messageElement.classList.add('event-message', type);
    
    const textElement = document.createElement('p');
    textElement.textContent = message.content;
    
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    
    // Scroll al último mensaje
    messageArea.scrollTop = messageArea.scrollHeight;
}

// ===========================
// Función: Mostrar Indicador de Escritura
// ===========================
function displayTypingIndicator(sender) {
    const typingText = typingIndicator.querySelector('.typing-text');
    typingText.textContent = `${sender} está escribiendo`;
    
    typingIndicator.classList.remove('hidden');
    
    // Ocultar después de 3 segundos
    setTimeout(() => {
        typingIndicator.classList.add('hidden');
    }, 3000);
}

// ===========================
// Función: Actualización de Conteo de Usuarios
// ===========================
function onUserCountUpdate(payload) {
    const notification = JSON.parse(payload.body);
    usersCount.textContent = notification.totalUsers;
}

// ===========================
// Función: Limpiar Chat
// ===========================
function onClearChat() {
    // Confirmar acción
    if (confirm('¿Estás seguro de que quieres limpiar todos los mensajes de tu pantalla?')) {
        // Limpiar área de mensajes
        messageArea.innerHTML = '';
        
        // Mostrar notificación
        showToast('Chat limpiado correctamente', 'success');
        
        console.log('Chat limpiado por el usuario');
        
        // Opcional: Agregar mensaje informativo
        const infoMessage = document.createElement('li');
        infoMessage.classList.add('event-message');
        const infoText = document.createElement('p');
        infoText.textContent = 'Has limpiado el chat. Los nuevos mensajes aparecerán aquí.';
        infoText.style.fontStyle = 'italic';
        infoMessage.appendChild(infoText);
        messageArea.appendChild(infoMessage);
    }
}

// ===========================
// Función: Cerrar Sesión
// ===========================
function onLogout() {
    if (stompClient && isConnected) {
        stompClient.disconnect(() => {
            console.log('Desconectado del servidor');
            isConnected = false;
            
            // Limpiar chat
            messageArea.innerHTML = '';
            username = null;
            
            // Mostrar página de username
            chatPage.classList.add('hidden');
            usernamePage.classList.remove('hidden');
            
            showToast('Has cerrado sesión correctamente', 'success');
        });
    }
}

// ===========================
// Función: Obtener Color de Avatar
// ===========================
function getAvatarColor(username) {
    let hash = 0;
    for (let i = 0; i < username.length; i++) {
        hash = 31 * hash + username.charCodeAt(i);
    }
    const index = Math.abs(hash % colors.length);
    return colors[index];
}

// ===========================
// Función: Formatear Timestamp
// ===========================
function formatTimestamp(timestamp) {
    if (!timestamp) {
        return new Date().toLocaleTimeString('es-ES', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }
    
    // Formato: "2024-11-08T10:30:45"
    const date = new Date(timestamp);
    return date.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

// ===========================
// Función: Mostrar Notificación Toast
// ===========================
function showToast(message, type = 'success') {
    const toast = document.querySelector('#toast');
    const toastMessage = toast.querySelector('.toast-message');
    const toastIcon = toast.querySelector('.toast-icon');
    
    // Configurar icono según el tipo
    let iconClass = 'fas fa-check-circle';
    if (type === 'error') {
        iconClass = 'fas fa-exclamation-circle';
    } else if (type === 'warning') {
        iconClass = 'fas fa-exclamation-triangle';
    }
    
    toastIcon.className = 'toast-icon ' + iconClass;
    toastMessage.textContent = message;
    toast.className = 'toast ' + type;
    
    // Mostrar toast
    toast.classList.remove('hidden');
    
    // Ocultar después de 3 segundos
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

// ===========================
// Manejo de Visibilidad de Página
// ===========================
document.addEventListener('visibilitychange', () => {
    if (document.hidden) {
        console.log('Página oculta');
    } else {
        console.log('Página visible');
        // Reconectar si es necesario
        if (username && !isConnected) {
            connect();
        }
    }
});

// ===========================
// Manejo de Cierre de Ventana
// ===========================
window.addEventListener('beforeunload', () => {
    if (stompClient && isConnected) {
        stompClient.disconnect();
    }
});

console.log('Sistema de Mensajería - Cliente inicializado');
