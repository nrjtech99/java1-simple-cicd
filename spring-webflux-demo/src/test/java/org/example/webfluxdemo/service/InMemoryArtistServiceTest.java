package org.example.webfluxdemo.service;

import org.example.webfluxdemo.domain.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class InMemoryArtistServiceTest {

    private InMemoryArtistService artistService;

    @BeforeEach
    void setUp() {
        artistService = new InMemoryArtistService(); // Re-initialize for each test for isolation
    }

    @Test
    void findAll() {
        // The service is initialized with 2 artists
        StepVerifier.create(artistService.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById_existing() {
        // Assuming IDs are "1", "2", ... based on AtomicLong starting at 0 and incrementAndGet
        StepVerifier.create(artistService.findById("1"))
                .expectNextMatches(artist -> artist.getName().equals("Artist A") && artist.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void findById_nonExisting() {
        StepVerifier.create(artistService.findById("99"))
                .expectNextCount(0) // or .verifyComplete() if it's empty
                .verifyComplete();
    }

    @Test
    void save_newArtist() {
        Artist newArtist = new Artist(null, "Artist C");
        StepVerifier.create(artistService.save(newArtist))
                .expectNextMatches(savedArtist -> savedArtist.getId() != null && savedArtist.getName().equals("Artist C"))
                .verifyComplete();

        // Verify it's added to the list (now 3 artists)
        StepVerifier.create(artistService.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void save_updateArtist() {
        // First, save a new artist to ensure its ID
        Artist artistToUpdate = new Artist(null, "Artist D");
        Mono<Artist> savedArtistMono = artistService.save(artistToUpdate);

        StepVerifier.create(savedArtistMono)
            .flatMap(savedArtist -> {
                savedArtist.setName("Artist D Updated");
                return artistService.save(savedArtist);
            })
            .expectNextMatches(updatedArtist ->
                updatedArtist.getName().equals("Artist D Updated") &&
                updatedArtist.getId() != null // ID should be preserved
            )
            .verifyComplete();
    }


    @Test
    void deleteById() {
        // Save an artist, then delete it
        Artist tempArtist = new Artist(null, "Artist Temp");
        artistService.save(tempArtist)
            .flatMap(savedArtist -> artistService.deleteById(savedArtist.getId()))
            .as(StepVerifier::create)
            .verifyComplete();

        // Verify it's removed (back to 2 initial artists if this is the only modification)
        // Or find by its ID and expect empty
         artistService.save(new Artist(null,"Artist E")).block(); // id "3"
         String idToDelete = "3"; // Assuming this ID will exist from the previous save.

        StepVerifier.create(artistService.deleteById(idToDelete).then(artistService.findById(idToDelete)))
                .expectComplete() // Expecting Mono<Void> from deleteById to complete, then findById to be empty.
                .verify();
    }
}
