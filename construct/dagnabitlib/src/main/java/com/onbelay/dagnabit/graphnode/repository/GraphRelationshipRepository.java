package com.onbelay.dagnabit.graphnode.repository;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;

import java.util.List;

public interface GraphRelationshipRepository {
    public static final String NAME = "graphRelationshipRepository";

    public GraphRelationship findByName(String name);

    public GraphRelationship load(EntityId entityId);

    public List<Integer> findGraphRelationshipIds(DefinedQuery definedQuery);

    public List<GraphRelationship> fetchByIds(QuerySelectedPage selectedPage);


    GraphRelationship findByExternalReference(Integer externalReferenceId);
}
