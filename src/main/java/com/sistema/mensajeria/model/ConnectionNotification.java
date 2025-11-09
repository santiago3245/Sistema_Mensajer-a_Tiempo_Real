package com.sistema.mensajeria.model;

import java.time.LocalDateTime;

/**
 * Clase que representa una notificación de conexión/desconexión
 * Utilizada para informar a los usuarios sobre cambios en el estado de conexión
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
public class ConnectionNotification {

    public enum NotificationType {
        USER_JOINED,
        USER_LEFT,
        USER_LIST_UPDATE
    }

    private NotificationType type;
    private String username;
    private int totalUsers;
    private LocalDateTime timestamp;
    private String message;

    /**
     * Constructor por defecto
     */
    public ConnectionNotification() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros
     */
    public ConnectionNotification(NotificationType type, String username, int totalUsers) {
        this.type = type;
        this.username = username;
        this.totalUsers = totalUsers;
        this.timestamp = LocalDateTime.now();
        this.message = buildMessage();
    }

    /**
     * Construye el mensaje de notificación según el tipo
     */
    private String buildMessage() {
        switch (type) {
            case USER_JOINED:
                return username + " se ha unido al chat";
            case USER_LEFT:
                return username + " ha abandonado el chat";
            case USER_LIST_UPDATE:
                return "Usuarios conectados: " + totalUsers;
            default:
                return "";
        }
    }

    // Getters y Setters

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
        this.message = buildMessage();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.message = buildMessage();
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
        this.message = buildMessage();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ConnectionNotification{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", totalUsers=" + totalUsers +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
