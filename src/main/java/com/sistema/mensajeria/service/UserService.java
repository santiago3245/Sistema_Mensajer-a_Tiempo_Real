package com.sistema.mensajeria.service;

import com.sistema.mensajeria.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para gestionar los usuarios conectados al sistema
 * Mantiene un registro de usuarios activos y sus sesiones
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Mapa thread-safe para almacenar usuarios conectados
    private final Map<String, User> connectedUsers = new ConcurrentHashMap<>();

    /**
     * Agrega un nuevo usuario al sistema
     * 
     * @param user Usuario a agregar
     * @return true si se agregó exitosamente, false si ya existía
     */
    public boolean addUser(User user) {
        if (user == null || user.getUsername() == null || user.getSessionId() == null) {
            logger.warn("Intento de agregar usuario inválido");
            return false;
        }

        User existingUser = connectedUsers.putIfAbsent(user.getSessionId(), user);
        if (existingUser == null) {
            logger.info("Usuario agregado: {} (SessionId: {})", user.getUsername(), user.getSessionId());
            return true;
        }
        
        logger.warn("Usuario ya existe: {}", user.getSessionId());
        return false;
    }

    /**
     * Elimina un usuario del sistema
     * 
     * @param sessionId ID de sesión del usuario a eliminar
     * @return El usuario eliminado, o null si no existía
     */
    public User removeUser(String sessionId) {
        User removedUser = connectedUsers.remove(sessionId);
        if (removedUser != null) {
            logger.info("Usuario eliminado: {} (SessionId: {})", removedUser.getUsername(), sessionId);
        }
        return removedUser;
    }

    /**
     * Obtiene un usuario por su ID de sesión
     * 
     * @param sessionId ID de sesión del usuario
     * @return El usuario, o null si no se encuentra
     */
    public User getUser(String sessionId) {
        return connectedUsers.get(sessionId);
    }

    /**
     * Obtiene todos los usuarios conectados
     * 
     * @return Colección de usuarios conectados
     */
    public Collection<User> getAllUsers() {
        return connectedUsers.values();
    }

    /**
     * Obtiene el número total de usuarios conectados
     * 
     * @return Número de usuarios conectados
     */
    public int getUserCount() {
        return connectedUsers.size();
    }

    /**
     * Verifica si un usuario está conectado
     * 
     * @param sessionId ID de sesión del usuario
     * @return true si el usuario está conectado, false en caso contrario
     */
    public boolean isUserConnected(String sessionId) {
        return connectedUsers.containsKey(sessionId);
    }

    /**
     * Limpia todos los usuarios (útil para pruebas o reinicio)
     */
    public void clearAllUsers() {
        int count = connectedUsers.size();
        connectedUsers.clear();
        logger.info("Se han eliminado {} usuarios del sistema", count);
    }
}
