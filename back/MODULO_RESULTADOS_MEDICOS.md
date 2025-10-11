# Módulo de Resultados Médicos - Salud Vital

## Descripción
Este módulo permite gestionar los resultados de exámenes médicos de los pacientes en la clínica Salud Vital. Proporciona funcionalidades completas para crear, consultar, actualizar y eliminar resultados médicos, así como búsquedas especializadas.

## Arquitectura
El módulo sigue el patrón de arquitectura en capas implementado en el proyecto:

- **Entity**: `ResultadoMedico` - Entidad JPA que representa la tabla en la base de datos
- **DTO**: `ResultadoMedicoDTO` - Objeto de transferencia de datos para la API
- **Repository**: `ResultadoMedicoRepository` - Capa de acceso a datos con consultas personalizadas
- **Service**: `ResultadoMedicoService` - Lógica de negocio y operaciones
- **Controller**: `ResultadoMedicoController` - Endpoints REST para la API
- **Mapper**: `ResultadoMedicoMapper` - Conversión entre Entity y DTO

## Características del Modelo

### Campos del ResultadoMedico
- **id**: Identificador único (Long, auto-generado)
- **paciente**: Nombre del paciente (String, obligatorio)
- **tipoExamen**: Tipo de examen realizado (String, obligatorio)
- **resultados**: Resultados del examen (String, obligatorio)
- **medicoResponsable**: Médico que realizó o interpretó el examen (String, obligatorio)
- **fechaExamen**: Fecha y hora del examen (LocalDateTime, obligatorio)
- **fechaEmision**: Fecha y hora de emisión del resultado (LocalDateTime, obligatorio)
- **observaciones**: Observaciones adicionales (String, opcional)
- **estado**: Estado del resultado (String: PENDIENTE, COMPLETADO, REVISADO)

### Validaciones
- Campos obligatorios: paciente, tipoExamen, resultados, medicoResponsable, fechaExamen, fechaEmision
- Fechas no pueden ser futuras (validación @PastOrPresent)
- Estado por defecto: "PENDIENTE"

## Endpoints de la API

### Operaciones CRUD Básicas
- `POST /api/resultados-medicos` - Crear nuevo resultado médico
- `GET /api/resultados-medicos` - Listar todos los resultados
- `GET /api/resultados-medicos/{id}` - Obtener resultado por ID
- `PUT /api/resultados-medicos/{id}` - Actualizar resultado existente
- `DELETE /api/resultados-medicos/{id}` - Eliminar resultado

### Búsquedas Especializadas
- `GET /api/resultados-medicos/buscar/paciente?paciente={nombre}` - Buscar por paciente
- `GET /api/resultados-medicos/buscar/tipo-examen?tipoExamen={tipo}` - Buscar por tipo de examen
- `GET /api/resultados-medicos/buscar/medico?medicoResponsable={medico}` - Buscar por médico
- `GET /api/resultados-medicos/buscar/estado?estado={estado}` - Buscar por estado
- `GET /api/resultados-medicos/pendientes` - Obtener resultados pendientes
- `GET /api/resultados-medicos/recientes` - Obtener resultados de los últimos 30 días

### Operaciones de Estado
- `PUT /api/resultados-medicos/{id}/cambiar-estado?estado={nuevoEstado}` - Cambiar estado

## Consultas del Repository

### Métodos de Búsqueda
- `findByPacienteContainingIgnoreCase()` - Búsqueda por paciente (case-insensitive)
- `findByTipoExamenContainingIgnoreCase()` - Búsqueda por tipo de examen
- `findByMedicoResponsableContainingIgnoreCase()` - Búsqueda por médico
- `findByEstado()` - Búsqueda por estado exacto
- `findByFechaExamenBetween()` - Búsqueda por rango de fechas
- `findByPacienteContainingIgnoreCaseAndTipoExamenContainingIgnoreCase()` - Búsqueda combinada

### Consultas Personalizadas
- `findResultadosPendientes()` - Resultados pendientes ordenados por fecha
- `findResultadosRecientes()` - Resultados de los últimos 30 días

## Datos de Prueba
El `DataLoader` incluye 6 resultados médicos de ejemplo:
1. Juan Pérez - Hemograma Completo (COMPLETADO)
2. Ana María - Electrocardiograma (COMPLETADO)
3. Pedro López - Radiografía de Tórax (PENDIENTE)
4. Carlos Ramírez - Resonancia Magnética Cerebral (REVISADO)
5. Laura Gómez - Perfil Lipídico (COMPLETADO)
6. María Torres - Ultrasonido Abdominal (PENDIENTE)

## Testing
El módulo incluye tests unitarios completos:
- `ResultadoMedicoServiceTest` - Tests para la lógica de negocio
- `ResultadoMedicoControllerTest` - Tests para los endpoints REST

### Cobertura de Tests
- Creación, lectura, actualización y eliminación
- Búsquedas por diferentes criterios
- Manejo de casos de error (IDs inexistentes)
- Validación de respuestas HTTP correctas
- Cambio de estados

## Ejemplo de Uso

### Crear un Resultado Médico
```json
POST /api/resultados-medicos
{
  "paciente": "María García",
  "tipoExamen": "Análisis de Sangre",
  "resultados": "Glucosa: 95 mg/dL (Normal), Colesterol: 180 mg/dL (Normal)",
  "medicoResponsable": "Dr. Ana López",
  "fechaExamen": "2024-01-15T10:00:00",
  "fechaEmision": "2024-01-15T14:30:00",
  "observaciones": "Resultados dentro de parámetros normales",
  "estado": "COMPLETADO"
}
```

### Buscar Resultados por Paciente
```
GET /api/resultados-medicos/buscar/paciente?paciente=María
```

### Cambiar Estado
```
PUT /api/resultados-medicos/1/cambiar-estado?estado=REVISADO
```

## Consideraciones de Seguridad
- Validación de entrada en todos los endpoints
- Manejo de errores con respuestas HTTP apropiadas
- Logging de operaciones importantes
- Validación de fechas para evitar fechas futuras

## Próximas Mejoras Sugeridas
1. Implementar autenticación y autorización
2. Agregar validaciones de negocio más específicas
3. Implementar paginación para listados grandes
4. Agregar filtros de fecha más avanzados
5. Implementar notificaciones automáticas por estado
6. Agregar soporte para archivos adjuntos (imágenes, PDFs)
7. Implementar auditoría de cambios
