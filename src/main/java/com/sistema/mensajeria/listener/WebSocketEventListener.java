package com.sistema.mensajeria.listener;

import com.sistema.mensajeria.controller.ChatController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listener para eventos de WebSocket
 * Maneja las conexiones y desconexiones de usuarios
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private ChatController chatController;

    /**
     * Maneja el evento de conexión de un nuevo cliente WebSocket
     * 
     * @param event Evento de conexión
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        logger.info("Nueva conexión WebSocket establecida. SessionId: {}", sessionId);
    }

    /**
     * Maneja el evento de desconexión de un cliente WebSocket
     * Notifica al controlador para actualizar el estado de los usuarios
     * 
     * @param event Evento de desconexión
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        if (username != null) {
            logger.info("Usuario desconectado: {} (SessionId: {})", username, sessionId);
            chatController.handleUserDisconnection(sessionId);
        } else {
            logger.info("Sesión desconectada sin usuario registrado. SessionId: {}", sessionId);
        }
    }
}
