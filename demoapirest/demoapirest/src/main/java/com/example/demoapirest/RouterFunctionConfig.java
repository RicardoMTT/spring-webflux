package com.example.demoapirest;

import com.example.demoapirest.handler.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    /*
    * Registrar componentes en el contenedor, para que sea manejado por spring
    * Aca tendra todas las rutas de mi api rest
    * */
    @Bean
    public RouterFunction<ServerResponse> routes(ProductoHandler handler){
        return RouterFunctions.route(
                RequestPredicates.GET("/api/v2/productos"),
                handler::listar
        );
    }
}
