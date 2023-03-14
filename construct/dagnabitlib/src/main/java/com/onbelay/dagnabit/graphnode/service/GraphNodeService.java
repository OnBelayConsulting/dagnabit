package com.onbelay.dagnabit.graphnode.service;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;

import java.util.List;

public interface GraphNodeService {
    public static final String NAME = "graphNodeService";

    GraphNodeSnapshot load(EntityId id);

    List<GraphNodeSnapshot> findByDefinedQuery(DefinedQuery definedQuery);

    QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery);

    List<GraphNodeSnapshot> findByIds(QuerySelectedPage page);

    TransactionResult save(GraphNodeSnapshot snapshot);

    TransactionResult save(List<GraphNodeSnapshot> snapshots);

    GraphNodeSnapshot findByExternalReference(Integer externalReferenceId);
}
