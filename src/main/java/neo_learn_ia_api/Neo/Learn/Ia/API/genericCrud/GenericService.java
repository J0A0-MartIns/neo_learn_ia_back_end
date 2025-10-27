package neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud;

import java.util.List;

/**
 * Interface de serviço genérica para operações CRUD.
 *
 * @param <ID> O tipo do identificador da entidade (ex: Long, UUID)
 * @param <I>  O DTO de entrada (Input/Request DTO)
 * @param <R>  O DTO de saída (Response DTO)
 */
public interface GenericService<ID, I, R> {

    List<R> findAll();

    R findById(ID id);

    R create(I inputDTO);

    R update(ID id, I inputDTO);

    void delete(ID id);
}
