package hornet.framework.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.JpaEntityGraph;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudProjectionRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Read a entity by ID and use a projection for the return
     * @param id entity id
     * @param projectionClass the projection to use for return the result
     * @param dynamicEntityGraph the grap to load or null
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    <R> Optional<R> getByIdWithProjection(ID id, Class<R> projectionClass, JpaEntityGraph dynamicEntityGraph);

    /**
     * Read a entity by ID and use a projection for the return
     * @param id entity id
     * @param projectionClass the projection to use for return the result
     * @param dynamicEntityGraph the grap to load or null
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    <R> Optional<R> getByIdWithProjection(ID id, Class<R> projectionClass, EntityGraph<T> dynamicEntityGraph);

    /**
     * Initialyse Entitygraph for a entity
     * @return EntityGraph
     */
    EntityGraph<T> createEntityGraph();

    /**
     * Initialyse a entity graph for a entity and set attributes and subgraph
     * @param subGraphAttributName
     * @param attributs attibuts for entity graph
     * @return EntityGraph
     */
    EntityGraph<T> createEntityGraph(final String subGraphAttributName, final String... attributs);


    /**
     * Returns all entities sorted by the given options switch specifiction and use a projection for the return.
     *
     * @param projectionClass the projection to use for return the result
     * @param specification for the query
     * @param sort
     * @return all entities converted in projection class and sorted by the given options
     */
    <R> List<R> findAllWithProjection(Class<R> projectionClass, Specification<T> spec, Sort sort);

    /**
     * Returns all entities switch specifiction and use a projection for the return
     * @param projectionClass the projection to use for return the result
     * @param specification for the query
     * @return all entities converted in projection class
     */
    <R> List<R> findAllWithProjection(Class<R> projectionClass, Specification<T> spec);

}
