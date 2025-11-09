package com.sistema.mensajeria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración del WebSocket utilizando STOMP sobre WebSocket
 * Esta clase configura los endpoints y el broker de mensajes
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el broker de mensajes para distribuir mensajes a los clientes suscritos
     * 
     * @param registry Registro del broker de mensajes
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Habilita un broker simple en memoria para los destinos que comienzan con /topic
        registry.enableSimpleBroker("/topic");
        
        // Define el prefijo para los mensajes destinados a métodos anotados con @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra los endpoints STOMP que los clientes utilizarán para conectarse al WebSocket
     * 
     * @param registry Registro de endpoints STOMP
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint "/ws-chat" con soporte SockJS para navegadores que no soporten WebSocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
