# Sistema de Agencia de Viajes

## 📱 Introducción y Objetivos

**Proyecto Final:** Sistema de Agencia de Viajes

Este proyecto implementa una solución integral de software para la gestión centralizada de una agencia de viajes, eliminando los procesos manuales tradicionales (hojas de cálculo, emails) y proporcionando una plataforma moderna y escalable.

---

## 🎯 Análisis del Problema

Las agencias de viajes tradicionales enfrentan múltiples desafíos:

- ❌ Dependencia de sistemas manuales desactualizados
- ❌ Falta de visión unificada del negocio
- ❌ Alto riesgo de errores en reservas
- ❌ Procesos administrativos ineficientes
- ❌ Dificultad para obtener información en tiempo real

---

## 💡 Justificación del Proyecto

Es necesaria una solución de software **centralizada y automatizada** que:

✅ Unifique la gestión de clientes, paquetes y reservas  
✅ Automatice el flujo desde la consulta hasta la confirmación  
✅ Reduzca la carga administrativa en un **~30%**  
✅ Asegure consistencia de datos entre agentes  
✅ Permita análisis de negocio y rentabilidad  

---

## 🎯 Objetivo Principal

> **Diseñar e implementar un sistema de "Back Office" que centralice la gestión de clientes, paquetes turísticos, servicios (vuelos, hoteles) y reservas en una agencia de viajes moderna.**

### Objetivos Secundarios

- Proporcionar una interfaz amigable para distintos roles de usuario
- Garantizar la integridad y disponibilidad de datos
- Permitir consultas en tiempo real sobre disponibilidad de servicios
- Facilitar el análisis de tendencias y rentabilidad
- Escalar según el crecimiento de reservas y clientes

---

## 🛠️ Tecnologías Empleadas

### Backend & Bases de Datos

| Tecnología | Descripción |
|-----------|-------------|
| **Firebase Firestore** | Base de datos NoSQL en tiempo real. Almacena clientes, reservas, destinos, servicios y paquetes turísticos |
| **Firebase Authentication** | Autenticación segura de usuarios (Administrador, Agente, Cliente) |
| **Firebase Cloud Functions** | Lógica de negocio serverless (validaciones, cálculos, triggers) |

### Frontend - Aplicación Móvil Android

| Tecnología | Descripción |
|-----------|-------------|
| **Android SDK API 21-29** | Aplicación nativa Android compatible con versiones desde Android 5.0 |
| **AndroidX** | Librerías modernas de compatibilidad |
| **Retrofit2** | Cliente HTTP para consumo de APIs externas |
| **OsmDroid** | Mapas basados en OpenStreetMap |
| **Firebase Mobile SDK** | Integración con Firestore y Authentication |

### Control de Versiones & Documentación

| Tecnología | Descripción |
|-----------|-------------|
| **Git + GitHub** | Versionado de código fuente y documentación |
| **Gradle** | Sistema de construcción y gestión de dependencias |
| **Markdown** | Documentación técnica y guías |

---

## 📊 Estructura del Proyecto

```
AgenciaDeViajes/
├── app/                          # Módulo de aplicación Android
│   ├── src/
│   │   └── main/
│   │       ├── java/             # Código fuente Java
│   │       │   └── com.example.agenciadeviajes/
│   │       │       ├── activities/      # Activities (vistas)
│   │       │       ├── models/          # Modelos de datos
│   │       │       └── services/        # Servicios (API, Firebase)
│   │       └── res/              # Recursos (layouts, strings, estilos)
│   └── build.gradle              # Configuración de compilación
├── build.gradle                  # Configuración raíz de Gradle
├── settings.gradle               # Configuración de módulos
└── documentation/                # Archivos de documentación
```

---

## 🏗️ Arquitectura General

### Componentes Principales

```
┌─────────────────────────────────────────────────────────┐
│         Aplicación Android (Frontend)                   │
│  - Activities (Login, Main, Reservas, Perfil, etc.)    │
│  - RecyclerViews (Listados de vuelos, reservas)        │
│  - Maps (OsmDroid)                                      │
└────────────────┬────────────────────────────────────────┘
                 │
                 │ (Retrofit2 + Google APIs)
                 │
┌────────────────▼────────────────────────────────────────┐
│         Firebase Backend                                │
│  ├── Authentication (Auth)                             │
│  ├── Firestore Database (NoSQL)                        │
│  ├── Cloud Functions (Lógica de negocio)              │
│  └── Storage (Imágenes, documentos)                    │
└────────────────┬────────────────────────────────────────┘
                 │
                 │ (APIs REST)
                 │
┌────────────────▼────────────────────────────────────────┐
│         APIs Externas                                   │
│  - OpenSky Network (Datos de vuelos en tiempo real)    │
└─────────────────────────────────────────────────────────┘
```

---

## 👥 Roles y Perfiles de Usuario

| Rol | Permisos | Funcionalidades |
|-----|----------|-----------------|
| **Administrador** | Control total | Gestionar usuarios, crear destinos, paquetes, servicios, ver reportes |
| **Agente de Viajes** | Permisos operativos | Crear/modificar/cancelar reservas, gestionar cartera de clientes |
| **Cliente** | Lectura limitada | Consultar reservas, historial, modificar perfil |

*Ver documento [ROLES_PERMISOS.md](ROLES_PERMISOS.md) para detalles.*

---

## 📱 Funcionalidades Principales

### 1. **Autenticación y Gestión de Usuarios**
- Login con email/contraseña
- Registro de nuevos usuarios
- Recuperación de contraseña
- Gestión de perfil

### 2. **Búsqueda y Reserva de Vuelos**
- Búsqueda por origen/destino/fecha
- Integración con API OpenSky (datos en tiempo real)
- Mostar información de vuelos disponibles
- Reserva de vuelos
- Visualización de vuelos directos

### 3. **Búsqueda y Reserva de Hoteles**
- Búsqueda de hoteles por destino/fecha
- Información de disponibilidad
- Reserva de hoteles

### 4. **Gestión de Reservas**
- Crear nuevas reservas
- Visualizar todas las reservas del usuario
- Modificar reservas (en estado permitido)
- Cancelar reservas
- Historial de reservas

### 5. **Mapas y Localización**
- Visualización de destinos en mapas (OsmDroid)
- Ubicación de hoteles y atracciones

### 6. **Multimedia**
- Reproducción de música de fondo
- Galería de imágenes de destinos

---

## 🚀 Fases de Desarrollo

| Fase | Duración | Descripción |
|------|----------|-------------|
| **1. Análisis** | Sem 1-2 | Definición de requisitos, análisis del problema y estudio de viabilidad |
| **2. Diseño** | Sem 3-5 | Diseño conceptual, lógico y físico; UML; prototipado |
| **3. Desarrollo** | Sem 6-12 | Implementación de backend, base de datos y frontend |
| **4. Pruebas & Deploy** | Sem 13-14 | Pruebas unitarias, integración y despliegue |

---

## 📈 Valor Empresarial

### Digitalización
- Reemplaza procesos manuales
- **Reducción estimada del 30%** en tiempo de gestión por reserva

### Consistencia de Datos
- Todos los agentes trabajan con la misma información
- Sincronización en tiempo real de precios y disponibilidad

### Inteligencia de Negocio
- Datos estructurados para análisis
- Identificación de paquetes más vendidos
- Análisis de rentabilidad por servicio

---


## 🔮 Líneas Futuras

- ✨ **Mejora de la App Móvil** - Migración a Jetpack Compose
- ✨ **Web Dashboard** - Panel de administración web
- ✨ **Notificaciones Push** - Alertas de reservas y promociones
- ✨ **Pago Online** - Integración con pasarelas de pago
- ✨ **Reportes Avanzados** - Analytics y business intelligence
- ✨ **Chatbot** - Asistente de atención al cliente

---

## 📝 Conclusión

El Sistema de Agencia de Viajes proporciona una solución integral y moderna para centralizar la gestión de una agencia de viajes. La arquitectura basada en Firebase garantiza **escalabilidad, seguridad y disponibilidad en tiempo real**, mientras que la aplicación Android proporciona una interfaz amigable y accesible para usuarios.

**Empresa:** Viajes Globales  
**Versión:** 1.0  
**Estado:** En desarrollo  

---

**Proyecto Final - Sistema de Agencia de Viaje**
