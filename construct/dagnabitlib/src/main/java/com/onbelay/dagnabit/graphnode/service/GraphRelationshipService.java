package com.onbelay.dagnabit.graphnode.service;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

import java.util.List;

public interface GraphRelationshipService {
    public static final String NAME = "graphRelationshipService";

    GraphRelationshipSnapshot load(EntityId entityId);

    List<GraphRelationshipSnapshot> findByDefinedQuery(DefinedQuery definedQuery);

    QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery);

    List<GraphRelationshipSnapshot> findByIds(QuerySelectedPage page);

    TransactionResult save(GraphRelationshipSnapshot snapshot);

    TransactionResult save(List<GraphRelationshipSnapshot> snapshots);

    GraphRelationshipSnapshot findByExternalReference(Integer externalReferenceId);
}
