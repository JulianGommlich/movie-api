package de.dhbw.movie_api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class MovieController {

    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }
    
    @PostMapping("/movies")
    public ResponseEntity<Movie> postMovie(@RequestBody Movie movie) {
        var persistedMovie = repository.save(movie);
        return ResponseEntity
            .created(URI.create(String.format("/movie/%d", persistedMovie.getId())))
            .body(persistedMovie);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(repository.findAll());
    }
    
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findById(id).orElseThrow(NotFoundException::new));
    }
    
    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> putMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        final var persistedMovie = repository.findById(id).orElseThrow(NotFoundException::new);
        updatedMovie.setId(persistedMovie.getId());
        return ResponseEntity.ok(repository.save(updatedMovie));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
