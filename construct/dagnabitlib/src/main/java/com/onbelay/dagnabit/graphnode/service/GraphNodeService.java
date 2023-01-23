package com.onbelay.dagnabit.graphnode.service;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;

import java.util.List;

public interface GraphNodeService {
    public static final String NAME = "graphNodeService";

    GraphNodeSnapshot load(Integer id);

    List<GraphNodeSnapshot> findByDefinedQuery(DefinedQuery definedQuery);

    QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery);

    List<GraphNodeSnapshot> findByIds(QuerySelectedPage page);

    TransactionResult save(GraphNodeSnapshot snapshot);

    TransactionResult save(List<GraphNodeSnapshot> snapshots);
}
