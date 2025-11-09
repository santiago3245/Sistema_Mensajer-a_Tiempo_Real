package com.sistema.mensajeria.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa un usuario conectado al sistema
 * Almacena información básica del usuario
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
public class User {

    private String username;
    private String sessionId;
    private LocalDateTime connectedAt;
    private boolean online;

    /**
     * Constructor por defecto
     */
    public User() {
        this.connectedAt = LocalDateTime.now();
        this.online = true;
    }

    /**
     * Constructor con parámetros
     */
    public User(String username, String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
        this.connectedAt = LocalDateTime.now();
        this.online = true;
    }

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(sessionId, user.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", connectedAt=" + connectedAt +
                ", online=" + online +
                '}';
    }
}
