package com.sistema.mensajeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Sistema de Mensajería en Tiempo Real
 * Utiliza Spring Boot para la configuración y gestión del servidor
 * 
 * @author Sistema de Mensajería
 * @version 1.0.0
 */
@SpringBootApplication
public class MensajeriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MensajeriaApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Sistema de Mensajería en Tiempo Real");
        System.out.println("Servidor WebSocket iniciado correctamente");
        System.out.println("Puerto: 8080");
        System.out.println("===========================================");
    }
}
