package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

public class GraphRelationshipFixture {

    public static GraphRelationship createSavedRelationship(
            GraphNode fromNode,
            GraphNode toNode,
            String relationship) {

        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        snapshot.setFromNodeId(fromNode.getGraphNodeId());
        snapshot.setToNodeId(toNode.getGraphNodeId());
        snapshot.getDetail().setType(relationship);

        GraphRelationship graphRelationship = new GraphRelationship();
        graphRelationship.createWith(snapshot);
        return graphRelationship;
    }
}
