package com.onbelay.dagnabit.graphnode.repository;

import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.model.GraphNode;

import java.util.List;

public interface GraphNodeRepository {
    public static final String NAME = "graphNodeRepository";

    public GraphNode findByName(String name);

    public GraphNode load(EntityId entityId);

    public List<Integer> findGraphNodeIds(DefinedQuery definedQuery);

    public List<GraphNode> fetchByIds(QuerySelectedPage selectedPage);



}
