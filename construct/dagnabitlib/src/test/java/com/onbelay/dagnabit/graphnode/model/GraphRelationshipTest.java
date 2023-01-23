package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import org.junit.Test;

public class GraphRelationshipTest extends DagnabitSpringTestCase {

    private GraphNode fromGraphNode;
    private GraphNode toGraphNode;


    public void setUp() {
        super.setUp();
        fromGraphNode = GraphNodeFixture.createSavedGraphNode("from");
        toGraphNode = GraphNodeFixture.createSavedGraphNode("to");
        flush();
    }

    @Test
    public void createRelationship() {
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(
                toGraphNode,
                fromGraphNode,
                "parent");
        flush();

        GraphRelationship created = GraphRelationship.load(relationship.getGraphRelationshipId());
    }

}
