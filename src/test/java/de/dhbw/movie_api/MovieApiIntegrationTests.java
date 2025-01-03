package de.dhbw.movie_api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovieApiIntegrationTests {

    @Autowired
    private MockMvc webMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MovieRepository repository;

    @AfterEach
    @SuppressWarnings("unused")
    void teardown() {
        repository.deleteAll();
    }
    
    @Test
    void testPostMovie() throws JsonProcessingException, Exception {
        final  var movie = new Movie("The Godfather", 1972, "Drama", true);

        webMvc.perform(MockMvcRequestBuilders.post("/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(movie)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void testGetMovies() throws Exception {
        repository.save(new Movie("Gladiator", 2000, "Action-Drama", true));
        repository.save(new Movie("Napoleon", 2023, "Drama", false));

        webMvc.perform(MockMvcRequestBuilders.get("/movies")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").value("Gladiator"))
            .andExpect(jsonPath("$[1].name").value("Napoleon"));       
    }

    @Test
    void testGetMovieById() throws Exception {
        final var persistedMovie = repository
            .save(new Movie("Kill Bill", 2004, "Action", false));

        webMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", persistedMovie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Kill Bill"));
    }

    @Test
    void testGetMovieByIdNotFound() throws Exception {
        webMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", 42)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testPutMovie() throws Exception {
        final var persistedMovie = repository
            .save(new Movie("Interstellar", 2014, "Science Fiction", true));

        final var updatedMovie = new Movie("Interstellar", 2014, "Action", false);
        webMvc.perform(MockMvcRequestBuilders.put("/movies/{id}", persistedMovie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updatedMovie)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.genre").value("Action"))
            .andExpect(jsonPath("$.is_available").value(false));
    }

    @Test
    void testPutMovieNotFound() throws Exception {
        final var updatedMovie = new Movie("Interstellar", 2014, "Action", false);

        webMvc.perform(MockMvcRequestBuilders.put("/movies/{id}", 42)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updatedMovie)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMovieNotFound() throws Exception {
        final var persistedMovie = repository
            .save(new Movie("Shutter Island", 2010, "Mystery", true));

        webMvc.perform(MockMvcRequestBuilders.delete("/movies/{id}", persistedMovie.getId()))
            .andExpect(status().isNoContent());
    }
}
