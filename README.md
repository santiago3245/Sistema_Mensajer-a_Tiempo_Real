# Sistema de Mensajería en Tiempo Real

Sistema de chat distribuido implementado con Java 17, Spring Boot y WebSocket, que permite la comunicación instantánea entre múltiples usuarios conectados simultáneamente.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Arquitectura](#arquitectura)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Ejecución](#ejecución)
- [Uso](#uso)
- [Pruebas](#pruebas)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Documentación Técnica](#documentación-técnica)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## 📖 Descripción

El Sistema de Mensajería en Tiempo Real es una aplicación web distribuida que permite la comunicación instantánea entre múltiples usuarios. Implementa el protocolo WebSocket para establecer conexiones bidireccionales persistentes entre clientes y servidor, garantizando una latencia mínima en la transmisión de mensajes.

### Funcionalidades Principales

- ✅ Mensajería en tiempo real sin necesidad de recargar la página
- ✅ Conexión/desconexión dinámica de usuarios
- ✅ Notificaciones de eventos (usuarios que se unen/abandonan)
- ✅ Indicador de "usuario escribiendo"
- ✅ Información de timestamp en cada mensaje
- ✅ Conteo de usuarios conectados en tiempo real
- ✅ Interfaz responsive y moderna
- ✅ Validación de mensajes y manejo de errores
- ✅ Reconexión automática en caso de pérdida de conexión

## 🎯 Características

### Requerimientos Funcionales Implementados

1. **Conexiones Simultáneas**: Soporta múltiples clientes conectados concurrentemente
2. **Broadcast de Mensajes**: Los mensajes se distribuyen a todos los usuarios conectados
3. **Notificaciones de Conexión**: Sistema de alertas para conexiones/desconexiones
4. **Metadata de Mensajes**: Cada mensaje incluye:
   - Nombre del usuario emisor
   - Hora de envío
   - Contenido del mensaje
5. **Gestión de Sesión**: Funcionalidad de cerrar sesión y limpiar el chat

### Requerimientos No Funcionales

- **Lenguaje**: Java 17
- **Protocolo**: WebSocket (STOMP sobre SockJS)
- **Escalabilidad**: Arquitectura preparada para múltiples conexiones
- **Usabilidad**: Interfaz intuitiva y responsive
- **Eficiencia**: Latencia mínima en transmisión de mensajes
- **Seguridad**: Validación de entrada y manejo robusto de errores

## 🏗️ Arquitectura

El sistema implementa una arquitectura cliente-servidor con comunicación bidireccional:

```
┌─────────────────┐         WebSocket/STOMP        ┌─────────────────┐
│                 │◄──────────────────────────────►│                 │
│  Cliente Web    │         Conexión Persistente   │  Servidor       │
│  (JavaScript)   │                                │  Spring Boot    │
│                 │◄──────────────────────────────►│                 │
└─────────────────┘                                └─────────────────┘
        │                                                    │
        │                                                    │
        ▼                                                    ▼
┌─────────────────┐                                ┌─────────────────┐
│  SockJS Client  │                                │ STOMP Broker    │
│  STOMP.js       │                                │ Message Handler │
└─────────────────┘                                └─────────────────┘
```

### Componentes Principales

#### Backend (Servidor)

1. **WebSocketConfig**: Configuración del broker de mensajes y endpoints STOMP
2. **ChatController**: Maneja los mensajes entrantes y salientes
3. **UserService**: Gestiona el registro de usuarios conectados
4. **WebSocketEventListener**: Escucha eventos de conexión/desconexión
5. **Modelos**: ChatMessage, User, ConnectionNotification

#### Frontend (Cliente)

1. **HTML**: Estructura de la interfaz de usuario
2. **CSS**: Estilos responsivos y modernos
3. **JavaScript**: Lógica de conexión WebSocket y manipulación del DOM

## 🛠️ Tecnologías Utilizadas

### Backend

- **Java 17**: Lenguaje de programación
- **Spring Boot 3.2.0**: Framework principal
- **Spring WebSocket**: Implementación de WebSocket
- **STOMP**: Protocolo de mensajería sobre WebSocket
- **Maven**: Gestor de dependencias y construcción

### Frontend

- **HTML5**: Estructura
- **CSS3**: Estilos y animaciones
- **JavaScript (ES6)**: Lógica del cliente
- **SockJS**: Librería de fallback para WebSocket
- **STOMP.js**: Cliente STOMP para JavaScript
- **Font Awesome**: Iconos

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java Development Kit (JDK) 17 o superior**
  - [Descargar JDK](https://www.oracle.com/java/technologies/downloads/)
  - Verificar instalación: `java -version`

- **Maven 3.6 o superior**
  - [Descargar Maven](https://maven.apache.org/download.cgi)
  - Verificar instalación: `mvn -version`

- **Navegador web moderno** (Chrome, Firefox, Edge, Safari)

- **Git** (opcional, para clonar el repositorio)

## 🚀 Instalación

### 1. Clonar o Descargar el Proyecto

```bash
# Opción 1: Clonar desde GitHub
git clone https://github.com/tu-usuario/sistema-mensajeria.git
cd sistema-mensajeria

# Opción 2: Descargar el ZIP y extraer
```

### 2. Compilar el Proyecto

```bash
# En la raíz del proyecto, ejecutar:
mvn clean install
```

Este comando descargará todas las dependencias necesarias y compilará el proyecto.

## ▶️ Ejecución

### Método 1: Usando Maven

```bash
mvn spring-boot:run
```

### Método 2: Usando el JAR compilado

```bash
# Primero compilar
mvn clean package

# Luego ejecutar
java -jar target/sistema-mensajeria-1.0.0.jar
```

### Método 3: Desde el IDE

1. Importar el proyecto como proyecto Maven
2. Ejecutar la clase `MensajeriaApplication.java`

### Verificar que el servidor está corriendo

Deberías ver en la consola:

```
===========================================
Sistema de Mensajería en Tiempo Real
Servidor WebSocket iniciado correctamente
Puerto: 8080
===========================================
```

## 💻 Uso

### Acceder a la Aplicación

1. Abrir el navegador web
2. Navegar a: `http://localhost:8080`
3. Ingresar un nombre de usuario (2-50 caracteres)
4. Hacer clic en "Iniciar Chat"

### Funcionalidades

#### Enviar Mensajes
- Escribir el mensaje en el campo de texto
- Presionar Enter o hacer clic en el botón de enviar
- El mensaje se distribuirá a todos los usuarios conectados

#### Ver Usuarios Conectados
- El contador en la parte superior derecha muestra usuarios en línea

#### Cerrar Sesión
- Hacer clic en el botón "Salir" en la esquina superior derecha

### Probar con Múltiples Usuarios

Para simular múltiples usuarios conectados:

1. Abrir múltiples pestañas o ventanas del navegador
2. Acceder a `http://localhost:8080` en cada una
3. Ingresar con diferentes nombres de usuario
4. Los mensajes enviados desde cualquier pestaña aparecerán en todas

## 🧪 Pruebas

### Pruebas Funcionales

#### Prueba 1: Conexión de Usuario
1. Acceder a la aplicación
2. Ingresar nombre de usuario válido
3. Verificar que se muestra la pantalla de chat
4. Verificar notificación de conexión exitosa

#### Prueba 2: Envío de Mensajes
1. Conectar dos usuarios en diferentes pestañas
2. Enviar mensaje desde Usuario A
3. Verificar que Usuario B recibe el mensaje
4. Verificar timestamp correcto

#### Prueba 3: Notificaciones de Conexión
1. Conectar Usuario A
2. Conectar Usuario B
3. Verificar que Usuario A ve notificación de Usuario B uniéndose
4. Desconectar Usuario B
5. Verificar que Usuario A ve notificación de Usuario B abandonando

#### Prueba 4: Conteo de Usuarios
1. Verificar contador inicia en 0
2. Conectar usuarios uno por uno
3. Verificar que el contador incrementa correctamente
4. Desconectar usuarios
5. Verificar que el contador decrementa

#### Prueba 5: Validación de Entrada
1. Intentar conectar con username vacío (debe fallar)
2. Intentar conectar con username de 1 carácter (debe fallar)
3. Intentar enviar mensaje vacío (no debe enviar)
4. Intentar enviar mensaje de >500 caracteres (debe mostrar error)

### Pruebas de Rendimiento

#### Test de Carga
- Conectar 10-20 usuarios simultáneamente
- Enviar mensajes desde múltiples usuarios
- Verificar latencia menor a 1 segundo
- Verificar que no hay pérdida de mensajes

### Pruebas de Robustez

#### Test de Reconexión
1. Iniciar servidor y conectar usuario
2. Detener servidor
3. Verificar mensaje de error
4. Reiniciar servidor
5. Verificar reconexión automática

## 📁 Estructura del Proyecto

```
sistema-mensajeria/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sistema/mensajeria/
│   │   │       ├── MensajeriaApplication.java
│   │   │       ├── config/
│   │   │       │   ├── WebSocketConfig.java
│   │   │       │   └── CorsConfig.java
│   │   │       ├── controller/
│   │   │       │   └── ChatController.java
│   │   │       ├── model/
│   │   │       │   ├── ChatMessage.java
│   │   │       │   ├── User.java
│   │   │       │   └── ConnectionNotification.java
│   │   │       ├── service/
│   │   │       │   └── UserService.java
│   │   │       └── listener/
│   │   │           └── WebSocketEventListener.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── css/
│   │           │   └── style.css
│   │           └── js/
│   │               └── main.js
│   │
│   └── test/
│       └── java/
│
├── docs/
│   ├── Informe_Tecnico.md
│   ├── Diagramas/
│   │   ├── DiagramaClases.puml
│   │   ├── DiagramaComponentes.puml
│   │   └── DiagramaSecuencia.puml
│   └── Manual_Usuario.md
│
├── pom.xml
├── README.md
├── .gitignore
└── LICENSE
```

## 📚 Documentación Técnica

La documentación técnica completa se encuentra en la carpeta `/docs`:

- **Informe Técnico**: Documento completo con arquitectura, diseño y pruebas
- **Diagramas UML**: Diagramas de clases, componentes y secuencia
- **Manual de Usuario**: Guía detallada de uso de la aplicación

### Diagramas

Los diagramas están disponibles en formato PlantUML en `/docs/Diagramas/`:
- Diagrama de Clases
- Diagrama de Componentes
- Diagrama de Secuencia

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Autores

- Sistema de Mensajería en Tiempo Real - Proyecto Académico

## 📞 Contacto

Para preguntas o sugerencias sobre el proyecto, por favor abrir un issue en el repositorio de GitHub.

---

**Nota**: Este proyecto fue desarrollado como parte de un ejercicio académico sobre aplicaciones distribuidas y sistemas de comunicación en tiempo real.
