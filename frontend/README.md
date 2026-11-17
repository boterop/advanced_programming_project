# Edificio Rent - Plataforma de Alquiler de Apartamentos

Plataforma web para la reserva y gestión de apartamentos en un edificio residencial.

## Características

- Búsqueda y filtrado de apartamentos
- Visualización detallada de propiedades
- Sistema de autenticación de usuarios
- Gestión de perfiles de usuario
- Reservas de apartamentos

## Tecnologías

- React 18
- Material UI
- React Router
- Vite
- Tailwind CSS

## Estructura del Proyecto

```
src/
├── components/     # Componentes reutilizables
├── layouts/        # Diseños de página
├── pages/        # Páginas de la aplicación
├── services/       # Servicios de API
├── utils/        # Funciones auxiliares
├── styles/         # Estilos globales
└── hooks/          # Hooks personalizados
```

## Instrucciones de Desarrollo

1. Instalar dependencias:
   ```bash
   pnpm install
   ```

2. Iniciar el servidor de desarrollo:
   ```bash
   pnpm run dev
   ```

3. Construir para producción:
   ```bash
   pnpm run build
   ```

4. Ejecutar pruebas:
   ```bash
   pnpm run test
   ```

## Estructura de Rutas

- `/` - Página de inicio
- `/login` - Inicio de sesión
- `/register` - Registro de usuarios
- `/search` - Búsqueda de apartamentos
- `/apartment/:id` - Detalles del apartamento
- `/profile` - Perfil del usuario