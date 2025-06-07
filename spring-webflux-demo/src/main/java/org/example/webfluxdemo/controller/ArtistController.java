package org.example.webfluxdemo.controller;

import org.example.webfluxdemo.domain.Artist;
import org.example.webfluxdemo.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public Flux<Artist> getAllArtists() {
        return artistService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Artist> getArtistById(@PathVariable String id) {
        return artistService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Artist> createArtist(@RequestBody Artist artist) {
        return artistService.save(artist);
    }

    @PutMapping("/{id}")
    public Mono<Artist> updateArtist(@PathVariable String id, @RequestBody Artist artist) {
        // Ensure the id in the path matches the id in the body, or set it.
        // For simplicity, we're relying on the service to handle the update logic,
        // potentially creating a new artist if ID doesn't exist or updating existing.
        // A more robust implementation might involve checking if artist.getId() is null or matches path id.
        artist.setId(id); // Make sure the ID from the path is used
        return artistService.save(artist);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteArtist(@PathVariable String id) {
        return artistService.deleteById(id);
    }
}
