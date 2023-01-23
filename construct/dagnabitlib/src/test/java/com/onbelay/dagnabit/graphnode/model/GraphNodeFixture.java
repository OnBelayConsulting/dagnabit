package com.onbelay.dagnabit.graphnode.model;

public class GraphNodeFixture {

    private GraphNodeFixture() { }

    public static GraphNode createSavedGraphNode(String name) {
        GraphNode  graphNode = new GraphNode();
        graphNode.createWith(name, name);
        return graphNode;
    }

}
