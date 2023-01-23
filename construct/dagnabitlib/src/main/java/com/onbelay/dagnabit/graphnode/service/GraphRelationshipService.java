package com.onbelay.dagnabit.graphnode.service;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;

import java.util.List;

public interface GraphRelationshipService {
    public static final String NAME = "graphRelationshipService";

    GraphRelationshipSnapshot load(Integer id);

    List<GraphRelationshipSnapshot> findByDefinedQuery(DefinedQuery definedQuery);

    QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery);

    List<GraphRelationshipSnapshot> findByIds(QuerySelectedPage page);

    TransactionResult save(GraphRelationshipSnapshot snapshot);

    TransactionResult save(List<GraphRelationshipSnapshot> snapshots);
}
