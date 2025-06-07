package org.example.webfluxdemo.controller;

import org.example.webfluxdemo.domain.Artist;
import org.example.webfluxdemo.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ArtistController.class) // Test only the ArtistController
public class ArtistControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean // Mocks the ArtistService dependency
    private ArtistService artistService;

    @Test
    void getAllArtists() {
        Artist artist1 = new Artist("1", "Artist A");
        Artist artist2 = new Artist("2", "Artist B");

        Mockito.when(artistService.findAll()).thenReturn(Flux.just(artist1, artist2));

        webTestClient.get().uri("/artists")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Artist.class).hasSize(2).contains(artist1, artist2);
    }

    @Test
    void getArtistById_existing() {
        Artist artist = new Artist("1", "Artist A");
        Mockito.when(artistService.findById("1")).thenReturn(Mono.just(artist));

        webTestClient.get().uri("/artists/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Artist.class).isEqualTo(artist);
    }

    @Test
    void getArtistById_nonExisting() {
        Mockito.when(artistService.findById("99")).thenReturn(Mono.empty());

        webTestClient.get().uri("/artists/99")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound(); // Or handle as appropriate if service returns error Mono
    }

    @Test
    void createArtist() {
        Artist artistToCreate = new Artist(null, "New Artist"); // ID is null for creation
        Artist createdArtist = new Artist("1", "New Artist"); // ID is set after creation

        Mockito.when(artistService.save(Mockito.any(Artist.class))).thenReturn(Mono.just(createdArtist));

        webTestClient.post().uri("/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistToCreate)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Artist.class).isEqualTo(createdArtist);
    }

    @Test
    void updateArtist() {
        Artist artistToUpdate = new Artist("1", "Updated Artist");
        // The ID in the path is "1", the body also contains the ID "1"
        Mockito.when(artistService.save(Mockito.any(Artist.class))).thenReturn(Mono.just(artistToUpdate));


        webTestClient.put().uri("/artists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Artist("1", "Updated Artist")) // Sending DTO that would result in artistToUpdate
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Artist.class).isEqualTo(artistToUpdate);
    }

    @Test
    void updateArtist_mismatchedId() {
        // Simulate a scenario where the service might create if ID in body is different or null,
        // but controller ensures path ID is set.
        Artist artistPayload = new Artist("2", "Artist X"); // ID in payload different from path
        Artist artistAfterSave = new Artist("1", "Artist X"); // Service uses path ID '1'

        // Mock the service to reflect that it uses the path ID for the operation
        // and returns an artist with that ID.
         Mockito.when(artistService.save(Mockito.argThat(artist -> "1".equals(artist.getId()) && "Artist X".equals(artist.getName()))))
            .thenReturn(Mono.just(artistAfterSave));


        webTestClient.put().uri("/artists/1") // Path ID is "1"
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistPayload) // Body has ID "2"
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Artist.class).isEqualTo(artistAfterSave);
    }


    @Test
    void deleteArtist() {
        Mockito.when(artistService.deleteById("1")).thenReturn(Mono.empty()); // Mono<Void> is empty on success

        webTestClient.delete().uri("/artists/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
