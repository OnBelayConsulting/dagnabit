package com.onbelay.dagnabit.graphnode.repositoryimpl;

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
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
    public static final String FIND_BY_EXTERNAL_REFERENCE = "GraphRelationship.FIND_BY_EXTERNAL_REFERENCE";

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

        if (entityId.isNull())
            return null;

        if (entityId.isInvalid())
            throw new OBRuntimeException(CoreTransactionErrorCode.INVALID_ENTITY_ID.getCode());

        if (entityId.isSet())
            return find(GraphRelationship.class, entityId.getId());
        else if (entityId.getCode() != null)
            return findByName(entityId.getCode());
        else
            return null;
    }


    public GraphRelationship findByExternalReference(Integer externalReferenceId) {
        return executeSingleResultQuery(
                FIND_BY_EXTERNAL_REFERENCE,
                "externalReferenceId",
                externalReferenceId);
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
