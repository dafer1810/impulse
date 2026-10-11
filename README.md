# 📦 Impulse - Gestor de Inventario

> Una aplicación Android completa y robusta para la gestión eficiente de inventarios, ventas y tareas comerciales.

[![Android](https://img.shields.io/badge/Android-SDK%2033+-green?logo=android)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Java-orangered?logo=java)](https://www.java.com)
[![License](https://img.shields.io/badge/License-MIT-blue)]()
[![GitHub last commit](https://img.shields.io/github/last-commit/dafer1810/impulse?color=blue)](https://github.com/dafer1810/impulse)

---

## 📱 Descripción del Proyecto

**Impulse** es una solución móvil diseñada para pequeños y medianos negocios que necesitan gestionar inventario de forma ágil y efectiva. La aplicación permite el control completo de productos, ventas y tareas de forma intuitiva y eficiente.

### ✨ Características Principales

- **📊 Gestión de Inventario**
  - Registro y actualización de productos
  - Control de stock en tiempo real
  - Seguimiento por categoría
  - Códigos de producto únicos

- **💰 Módulo de Ventas**
  - Creación y procesamiento de facturas
  - Carrito de compras dinámico
  - Historial de transacciones
  - Cálculo automático de totales

- **✅ Sistema de Tareas**
  - Creación y asignación de tareas
  - Estados: Pendiente, En progreso, Completada
  - Fechas de vencimiento
  - Indicadores visuales por estado

- **🎨 Interfaz Intuitiva**
  - Diseño Material Design
  - RecyclerView para listas eficientes
  - Navegación fluida entre módulos
  - Adaptadores personalizados para cada tipo de dato

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Descripción |
|-----------|-----------|
| **Java** | Lenguaje principal de desarrollo |
| **Android SDK** | Framework para desarrollo nativo |
| **RecyclerView** | Listas optimizadas y eficientes |
| **Material Design** | Componentes visuales modernos |
| **Arquitectura MVC** | Estructura separada en modelos, vistas y controladores |

---

## 📂 Estructura del Proyecto

```
impulse/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/impulseapp/
│   │   │   │   ├── Producto.java
│   │   │   │   ├── ProductoAdapter.java
│   │   │   │   ├── Venta.java
│   │   │   │   ├── VentaAdapter.java
│   │   │   │   ├── Tarea.java
│   │   │   │   ├── TareaAdapter.java
│   │   │   │   ├── ItemFactura.java
│   │   │   │   ├── ItemFacturaAdapter.java
│   │   │   │   └── InventarioAdapter.java
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       └── drawable/
│   │   └── test/
│   └── build.gradle
└── README.md
```

---

## 🚀 Funcionalidades Clave

### 1. **Gestión de Productos**
- Crear, editar y eliminar productos
- Información: Nombre, código, categoría, precio y stock
- Listado con búsqueda y filtrado

### 2. **Control de Inventario**
- Vista en tiempo real del stock disponible
- Alertas de bajo inventario
- Historial de movimientos

### 3. **Módulo de Facturación**
- Interfaz de factura intuitiva
- Líneas de detalle con cantidad y precio
- Cálculo automático de subtotales y totales
- Eliminar items del carrito

### 4. **Gestión de Tareas**
- Asignación de responsables
- Estados visuales diferenciados (con códigos de color)
- Fechas de entrega
- Historial de completadas

---

## 🎯 Casos de Uso

- ✅ Pequeños comercios y tiendas
- ✅ Almacenes y bodegas
- ✅ Negocios de ventas por catálogo
- ✅ Equipos de logística
- ✅ Empresas con múltiples puntos de venta

---

## 📋 Requisitos

- **Android SDK 33+**
- **Java 8+**
- **Android Studio**
- **Gradle**

---

## 🔧 Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/dafer1810/impulse.git
cd impulse
```

2. **Abrir en Android Studio:**
   - Importar el proyecto como existente
   - Permitir que se descarguen las dependencias

3. **Compilar y ejecutar:**
   - Conectar un dispositivo Android o iniciar un emulador
   - Click en `Run` en Android Studio

---

## 💡 Puntos Destacados del Código

### Adaptadores Eficientes
- **ProductoAdapter**: Manejo optimizado de listas de productos con formateo de precios
- **VentaAdapter**: Visualización de transacciones con detalles comprimidos
- **TareaAdapter**: Estados con código de color para mejor UX
- **ItemFacturaAdapter**: Gestión dinámica de líneas de factura

### Patrones de Diseño
- **Adapter Pattern**: Para la visualización de datos en RecyclerView
- **MVC**: Separación clara entre modelos, vistas y controladores
- **OnClickListener**: Interactividad eficiente en elementos de lista

---

## 📊 Métricas del Proyecto

- **Líneas de Código**: 500+
- **Clases Principales**: 9+
- **Adaptadores**: 5
- **Lenguaje**: Java 100%

---

## 🤝 Contribución

Este es un proyecto colaborativo. Las sugerencias y feedback son bienvenidos.

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

---

## 👨‍💻 Autores

| Nombre | GitHub |
|--------|--------|
| **Daniela Fernanda Moreno Sanchez** | [@dafer1810](https://github.com/dafer1810) |
| **Darcy Lizbeth Yandi Flor** | [GitHub Profile](https://github.com) |
| **Cesar Mauricio Florez Ruiz** | [@CesarMauriciof](https://github.com/CesarMauriciof) |
| **Keiner Alex Buitron Pareja** | [GitHub Profile](https://github.com) |

---

## 📞 Contacto

Para consultas sobre este proyecto, puedes contactarnos a través de [GitHub Issues](https://github.com/dafer1810/impulse/issues).

---

**Última actualización:** Octubre 2026
