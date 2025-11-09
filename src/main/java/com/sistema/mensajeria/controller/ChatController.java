package com.sistema.mensajeria.controller;

import com.sistema.mensajeria.model.ChatMessage;
import com.sistema.mensajeria.model.ConnectionNotification;
import com.sistema.mensajeria.model.User;
import com.sistema.mensajeria.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * Controlador WebSocket para manejar mensajes del chat
 * Gestiona el envío y recepción de mensajes entre clientes
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Maneja los mensajes de chat enviados por los usuarios
     * Los mensajes son validados y luego distribuidos a todos los clientes suscritos
     * 
     * @param chatMessage Mensaje de chat recibido
     * @return El mensaje procesado para ser enviado a todos los clientes
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Validar que el mensaje no esté vacío
        if (chatMessage.getContent() == null || chatMessage.getContent().trim().isEmpty()) {
            logger.warn("Intento de enviar mensaje vacío por {}", chatMessage.getSender());
            return null; // No enviar mensaje vacío
        }
        
        // Sanitizar el contenido del mensaje
        chatMessage.setContent(chatMessage.getContent().trim());
        
        // Validar longitud máxima
        if (chatMessage.getContent().length() > 500) {
            logger.warn("Mensaje demasiado largo de {}", chatMessage.getSender());
            return null;
        }
        
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        
        logger.info("Mensaje recibido de {}: {}", 
                    chatMessage.getSender(), 
                    chatMessage.getContent());
        
        return chatMessage;
    }

    /**
     * Maneja la conexión de un nuevo usuario al chat
     * Registra al usuario y notifica a todos los clientes conectados
     * 
     * @param chatMessage Mensaje con información del usuario
     * @param headerAccessor Acceso a los headers de la sesión WebSocket
     * @return Mensaje de notificación de conexión
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Validar que el sender no esté vacío
        if (chatMessage.getSender() == null || chatMessage.getSender().trim().isEmpty()) {
            logger.warn("Intento de conexión sin nombre de usuario");
            return null;
        }
        
        // Obtener el ID de sesión del WebSocket
        String sessionId = headerAccessor.getSessionId();
        
        // Agregar username en los atributos de sesión WebSocket
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        
        // Crear y registrar el nuevo usuario
        User newUser = new User(chatMessage.getSender(), sessionId);
        userService.addUser(newUser);
        
        logger.info("Nuevo usuario conectado: {} (SessionId: {}, Total usuarios: {})", 
                    chatMessage.getSender(), 
                    sessionId,
                    userService.getUserCount());
        
        // Crear mensaje de notificación
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setContent(chatMessage.getSender() + " se ha unido al chat");
        
        // Enviar actualización del número de usuarios conectados
        sendUserCountUpdate();
        
        return chatMessage;
    }

    /**
     * Maneja las notificaciones de "usuario escribiendo"
     * 
     * @param chatMessage Mensaje con información del usuario escribiendo
     */
    @MessageMapping("/chat.typing")
    @SendTo("/topic/public")
    public ChatMessage userTyping(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.TYPING);
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessage;
    }

    /**
     * Envía una actualización del conteo de usuarios a todos los clientes
     */
    private void sendUserCountUpdate() {
        ConnectionNotification notification = new ConnectionNotification(
            ConnectionNotification.NotificationType.USER_LIST_UPDATE,
            null,
            userService.getUserCount()
        );
        
        messagingTemplate.convertAndSend("/topic/userCount", notification);
    }

    /**
     * Maneja la desconexión de un usuario
     * Este método es llamado por el evento listener cuando se detecta una desconexión
     * 
     * @param sessionId ID de sesión del usuario desconectado
     */
    public void handleUserDisconnection(String sessionId) {
        User disconnectedUser = userService.removeUser(sessionId);
        
        if (disconnectedUser != null) {
            logger.info("Usuario desconectado: {} (SessionId: {}, Total usuarios: {})", 
                        disconnectedUser.getUsername(),
                        sessionId,
                        userService.getUserCount());
            
            // Crear mensaje de salida
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setType(ChatMessage.MessageType.LEAVE);
            leaveMessage.setSender(disconnectedUser.getUsername());
            leaveMessage.setContent(disconnectedUser.getUsername() + " ha abandonado el chat");
            leaveMessage.setTimestamp(LocalDateTime.now());
            
            // Enviar notificación de desconexión
            messagingTemplate.convertAndSend("/topic/public", leaveMessage);
            
            // Actualizar conteo de usuarios
            sendUserCountUpdate();
        }
    }
}
