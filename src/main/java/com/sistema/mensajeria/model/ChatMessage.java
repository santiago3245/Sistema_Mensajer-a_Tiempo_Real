package com.sistema.mensajeria.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Clase que representa un mensaje en el sistema de chat
 * Contiene información del emisor, contenido y timestamp
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
public class ChatMessage {

    /**
     * Enumeración de tipos de mensaje
     */
    public enum MessageType {
        CHAT,           // Mensaje de chat normal
        JOIN,           // Usuario se une al chat
        LEAVE,          // Usuario abandona el chat
        TYPING,         // Usuario está escribiendo
        SYSTEM          // Mensaje del sistema
    }

    private MessageType type;

    @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
    private String content;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String sender;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Constructor por defecto
     */
    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros
     */
    public ChatMessage(MessageType type, String content, String sender) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
