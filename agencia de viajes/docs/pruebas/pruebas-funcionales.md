# Pruebas Funcionales

## Objetivo
Validar que las funcionalidades principales de la aplicación cumplen los requisitos esperados.

## Casos de prueba principales

1. Registro de usuario
- Precondición: usuario no autenticado.
- Pasos: abrir registro, introducir email/contraseña válidos, confirmar.
- Resultado esperado: usuario creado en Firebase Auth y documento en Firestore (`users`).

2. Inicio de sesión
- Precondición: usuario existente.
- Pasos: introducir credenciales correctas.
- Resultado esperado: acceso al panel principal según rol.

3. Búsqueda de vuelos
- Precondición: usuario autenticado.
- Pasos: seleccionar origen, destino y fecha; buscar.
- Resultado esperado: listado de vuelos en pantalla.

4. Crear reserva de vuelo
- Precondición: vuelo disponible.
- Pasos: seleccionar vuelo, completar datos, confirmar.
- Resultado esperado: reserva creada en Firestore (`reservas`) con estado inicial correcto.

5. Visualizar reservas
- Precondición: usuario con reservas.
- Pasos: entrar en “Mis Reservas”.
- Resultado esperado: listado filtrado por usuario y sincronización en tiempo real.

6. Cancelar reserva
- Precondición: reserva confirmada y política de cancelación válida.
- Pasos: abrir reserva, cancelar, confirmar.
- Resultado esperado: estado actualizado a `cancelada` y registro en auditoría.

## Resumen de ejecución (estimado)

- Total pruebas definidas: 6
- Estado actual: pendientes de ejecución formal y evidencias con captura


