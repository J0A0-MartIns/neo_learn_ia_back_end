package neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl;

import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;



/**
 * Implementação abstrata do GenericService.
 *
 * @param <E>    A Entidade JPA
 * @param <ID>   O tipo do ID da Entidade
 * @param <I>    O DTO de Entrada (Input/Request)
 * @param <R>    O DTO de Resposta (Response)
 * @param <REPO> O JpaRepository para a Entidade E
 */
public abstract class AbstractGenericService<
        E,
        ID,
        I,
        R,
        REPO extends JpaRepository<E, ID>
        > implements GenericService<ID, I, R> {


    protected final REPO repository;

    public AbstractGenericService(REPO repository) {
        this.repository = repository;
    }


    protected abstract E toEntity(I inputDTO);


    protected abstract R toResponseDTO(E entity);


    protected abstract void updateEntityFromDTO(E entity, I inputDTO);



    @Override
    public List<R> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public R findById(ID id) {
        E entity = findEntityById(id);
        return toResponseDTO(entity);
    }

    @Override
    public R create(I inputDTO) {
        E entity = toEntity(inputDTO);
        E savedEntity = repository.save(entity);
        return toResponseDTO(savedEntity);
    }

    @Override
    public R update(ID id, I inputDTO) {
        E entity = findEntityById(id);
        updateEntityFromDTO(entity, inputDTO);
        E updatedEntity = repository.save(entity);
        return toResponseDTO(updatedEntity);
    }

    @Override
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Recurso não encontrado com o id: " + id);
        }
        repository.deleteById(id);
    }


    protected E findEntityById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com o id: " + id));
    }
}