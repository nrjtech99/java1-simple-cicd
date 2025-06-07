package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Artist;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Flux<Artist> findAll();
    Mono<Artist> findById(String id);
    Mono<Artist> save(Artist artist);
    Mono<Void> deleteById(String id);
}
