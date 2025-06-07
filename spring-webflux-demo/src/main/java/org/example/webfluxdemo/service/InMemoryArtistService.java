package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Artist;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryArtistService implements ArtistService {

    private final Map<String, Artist> artistMap = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public InMemoryArtistService() {
        // Initialize with some data
        save(new Artist(null, "Artist A")).subscribe();
        save(new Artist(null, "Artist B")).subscribe();
    }

    @Override
    public Flux<Artist> findAll() {
        return Flux.fromIterable(artistMap.values());
    }

    @Override
    public Mono<Artist> findById(String id) {
        return Mono.justOrEmpty(artistMap.get(id));
    }

    @Override
    public Mono<Artist> save(Artist artist) {
        return Mono.fromCallable(() -> {
            if (artist.getId() == null || artist.getId().isEmpty()) {
                String newId = String.valueOf(idCounter.incrementAndGet());
                artist.setId(newId);
            }
            artistMap.put(artist.getId(), artist);
            return artist;
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.fromRunnable(() -> artistMap.remove(id));
    }
}
