package com.onbelay.dagnabit.graphnode.repositoryimpl;

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.model.GraphRelationshipColumnDefinitions;
import com.onbelay.dagnabit.graphnode.repository.GraphRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value=GraphRelationshipRepository.NAME)
@Transactional
public class GraphRelationshipRepositoryBean extends BaseRepository<GraphRelationship> implements GraphRelationshipRepository {
    public static final String FIND_BY_NAME = "GraphRelationship.FIND_BY_NAME";

    @Autowired
    private GraphRelationshipColumnDefinitions graphRelationshipColumnDefinitions;

    @Override
    public GraphRelationship findByName(String name) {
        return executeSingleResultQuery(
                FIND_BY_NAME,
                "name",
                name);
    }

    public GraphRelationship load(EntityId entityId) {
        if (entityId.isSet())
            return find(GraphRelationship.class, entityId.getId());
        else
            return null;
    }



    @Override
    public List<Integer> findGraphRelationshipIds(DefinedQuery definedQuery) {
        return executeDefinedQueryForIds(
                graphRelationshipColumnDefinitions,
                definedQuery);
    }

    @Override
    public List<GraphRelationship> fetchByIds(QuerySelectedPage selectedPage) {
        return fetchEntitiesById(
                graphRelationshipColumnDefinitions,
                "GraphRelationship",
                selectedPage);
    }



}
