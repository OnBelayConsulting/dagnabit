package com.onbelay.dagnabit.graphnode.repository;

import com.onbelay.dagnabit.common.repository.Repository;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;

import java.util.List;

public interface GraphNodeRepository extends Repository<GraphNode> {
    public static final String NAME = "graphNodeRepository";

    public GraphNode findByName(String name);


    public List<Integer> findGraphNodeIds(DefinedQuery definedQuery);

    public List<GraphNode> fetchByIds(QuerySelectedPage selectedPage);



}
