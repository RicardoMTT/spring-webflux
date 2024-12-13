package com.example.demoapirest.controllers;

import com.example.demoapirest.models.documents.Producto;
import com.example.demoapirest.models.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")//Ruta base
public class ProductoController {

    @Autowired
    private ProductoService service;

    //@GetMapping
    //public Flux<Producto> listar(){
    //    return service.findAll();
    //}

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto,@PathVariable String id){
        return  service.findById(id).flatMap(p -> {
            p.setNombre(p.getNombre());
            p.setPrecio(p.getPrecio());
            return service.save(p);
        }).map(pro -> ResponseEntity.created(URI.create("/api/productos".concat(pro.getId())))
        .contentType(MediaType.APPLICATION_JSON).body(pro))
        .defaultIfEmpty(ResponseEntity.notFound()
        .build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String,Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto){
        Map<String,Object> respuesta = new HashMap<String,Object>();
        return monoProducto.flatMap(producto -> {
            if (producto.getCreateAt() == null){
                producto.setCreateAt(new Date());
            }
            return service.save(producto).map(p -> {
                respuesta.put("producto",p);
                respuesta.put("mensaje","Producto creado");
                return ResponseEntity
                                .created(URI.create("/api/productos/".concat(p.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(respuesta);
                    }
            );
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e-> Mono.just(e.getFieldErrors()))
                    .flatMapMany(errors -> Flux.fromIterable(errors))
                    .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        respuesta.put("errors",list);
                        respuesta.put("status",HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(respuesta));
                    });
        });




    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> listar2(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.findAll())
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
        return service.findById(id).map(p->ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON).body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
        return service.findById(id).flatMap(p->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
