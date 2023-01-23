package com.onbelay.dagnabit.graphnode.factory;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import org.junit.Test;

public class PersistedGraphNodeFactoryTest extends DagnabitSpringTestCase {

    private GraphNode fromGraphNode;
    private GraphNode toGraphNode;


    public void setUp() {
        super.setUp();
        fromGraphNode = GraphNodeFixture.createSavedGraphNode("from");
        toGraphNode = GraphNodeFixture.createSavedGraphNode("to");
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(toGraphNode, fromGraphNode, "parent");
        flush();
        flush();
    }

    @Test
    public void createRelationship() {
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(toGraphNode, fromGraphNode, "parent");
        flush();

        GraphRelationship created = GraphRelationship.load(relationship.getGraphRelationshipId());
    }


}
