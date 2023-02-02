package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;

public class GraphNodeFixture {

    private GraphNodeFixture() { }

    public static GraphNode createSavedGraphNode(String name) {
        GraphNode  graphNode = new GraphNode();
        graphNode.createWith(name, name);
        return graphNode;
    }

    public static GraphNode createSavedGraphNode(
            String name,
            String category) {

        GraphNodeSnapshot snapshot = new GraphNodeSnapshot();
        snapshot.getDetail().setName(name);
        snapshot.getDetail().setCategory(category);

        GraphNode  graphNode = new GraphNode();
        graphNode.createWith(snapshot);
        return graphNode;
    }

}
