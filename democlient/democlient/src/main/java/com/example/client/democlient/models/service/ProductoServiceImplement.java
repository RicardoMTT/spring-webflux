package com.example.client.democlient.models.service;

import com.example.client.democlient.models.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImplement implements ProductoService {

    @Autowired
    private WebClient client;

    @Override
    public Flux<Producto> findAll() {
        return client.get().accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(Producto.class)
                );
    }

    @Override
    public Mono<Producto> findById(String id) {
        return null;
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return null;
    }

    @Override
    public Mono<Producto> update(Producto producto) {
        return null;
    }

    @Override
    public Mono<Void> update(String id) {
        return null;
    }
}
