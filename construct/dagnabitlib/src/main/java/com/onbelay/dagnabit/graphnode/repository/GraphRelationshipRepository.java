package com.onbelay.dagnabit.graphnode.repository;

import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;

import java.util.List;

public interface GraphRelationshipRepository {
    public static final String NAME = "graphRelationshipRepository";

    public GraphRelationship findByName(String name);

    public List<Integer> findGraphRelationshipIds(DefinedQuery definedQuery);

    public List<GraphRelationship> fetchByIds(QuerySelectedPage selectedPage);



}
