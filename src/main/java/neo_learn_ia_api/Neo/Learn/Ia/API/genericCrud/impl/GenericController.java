package neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller abstrato com endpoints CRUD genéricos.
 *
 * @param <ID> O tipo do ID
 * @param <I>  O DTO de Entrada (Input/Request)
 * @param <R>  O DTO de Resposta (Response)
 */
public abstract class GenericController<ID, I, R> {


    protected final GenericService<ID, I, R> service;

    public GenericController(GenericService<ID, I, R> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<R> create(@RequestBody I inputDTO) {
        R responseDTO = service.create(inputDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<R>> findAll() {
        List<R> responseDTOs = service.findAll();
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<R> findById(@PathVariable ID id) {
        R responseDTO = service.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<R> update(@PathVariable ID id,  @RequestBody I inputDTO) {
        R responseDTO = service.update(id, inputDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}