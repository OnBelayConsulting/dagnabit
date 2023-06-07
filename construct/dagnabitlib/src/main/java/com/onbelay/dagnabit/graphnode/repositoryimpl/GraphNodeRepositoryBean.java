package com.onbelay.dagnabit.graphnode.repositoryimpl;

import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.enums.CoreTransactionErrorCode;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeColumnDefinitions;
import com.onbelay.dagnabit.graphnode.repository.GraphNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value=GraphNodeRepository.NAME)
@Transactional
public class GraphNodeRepositoryBean extends BaseRepository<GraphNode> implements GraphNodeRepository {
    public static final String FIND_NODE_BY_NAME = "GraphNode.FIND_NODE_BY_NAME";
    public static final String FIND_BY_EXTERNAL_REFERENCE = "GraphNode.FIND_BY_EXTERNAL_REFERENCE";

    @Autowired
    private GraphNodeColumnDefinitions graphNodeColumnDefinitions;

    @Override
    public GraphNode findByName(String name) {
        return executeSingleResultQuery(
                FIND_NODE_BY_NAME,
                "name",
                name);
    }


    @Override
    public GraphNode findByExternalReference(Integer externalReferenceId) {
        return executeSingleResultQuery(
                FIND_BY_EXTERNAL_REFERENCE,
                "externalReferenceId",
                externalReferenceId);
    }


    @Override
    public List<Integer> findGraphNodeIds(DefinedQuery definedQuery) {
        return executeDefinedQueryForIds(
                graphNodeColumnDefinitions,
                definedQuery);
    }

    @Override
    public List<GraphNode> fetchByIds(QuerySelectedPage selectedPage) {
        return fetchEntitiesById(
                graphNodeColumnDefinitions,
                "GraphNode",
                selectedPage);
    }

    public GraphNode load(EntityId entityId) {

        if (entityId.isNull())
            return null;

        if (entityId.isInvalid())
            throw new OBRuntimeException(CoreTransactionErrorCode.INVALID_ENTITY_ID.getCode());

        if (entityId.isSet())
            return find(GraphNode.class, entityId.getId());
        else if (entityId.getCode() != null)
            return findByName(entityId.getCode());
        else
            return null;
    }

}
