# Aplicacion spring boot

Aplicacion web usando spring boot que esta compuesto por 2 microservicios:

- demoapirest, usando programacion reactiva hace el consumo a una coleccion de mongodb
- democlient, que mediante WebClient se comunica con el otro microservicio para traer información de esa collecion.

## Endpoints
  - GET: /api/productos
    Obtener un listado de productos de una coleccion de mongodb
  - POST: /api/productos
    Crear un producto en una coleccion de mongodb
## Mono y Flux

  Mono : Representa un flujo que puede emitir 0 o 1 elemento.
  
  Flux:  Representa un flujo que puede manejar manejar de 0,1 a mas elementos.
    
