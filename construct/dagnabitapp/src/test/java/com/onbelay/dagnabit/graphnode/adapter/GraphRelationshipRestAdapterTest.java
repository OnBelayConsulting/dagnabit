package com.onbelay.dagnabit.graphnode.adapter;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.model.GraphRelationshipFixture;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphRelationshipRestAdapter;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphRelationshipCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GraphRelationshipRestAdapterTest extends DagnabitSpringTestCase {

    @Autowired
    private GraphRelationshipRestAdapter graphRelationshipRestAdapter;

    private GraphNode firstNode;
    private GraphNode secondNode;


    public void setUp() {

        super.setUp();

        firstNode = GraphNodeFixture.createSavedGraphNode("firstNode");
        secondNode = GraphNodeFixture.createSavedGraphNode("secondNode");
        flush();

        GraphRelationshipFixture.createSavedRelationship(
                firstNode,
                secondNode,
                "specialRelationship");

        flush();
    }

    @Test
    public void fetchRelationshipsUsingFirstNode() {
        GraphRelationshipCollection collection = graphRelationshipRestAdapter.findGraphRelationships(
                0,
                10,
                "WHERE fromNodeName = 'firstNode'");

        assertEquals(1, collection.getItems().size());
        GraphRelationshipSnapshot snapshot = collection.getItems().get(0);
        assertEquals("firstNode", snapshot.getFromNodeName());
        assertEquals("secondNode", snapshot.getToNodeName());
        assertEquals("specialRelationship", snapshot.getDetail().getType());
    }

    @Test
    public void fetchRelationshipsUsingRelationshipType() {
        GraphRelationshipCollection collection = graphRelationshipRestAdapter.findGraphRelationships(
                0,
                10,
                "WHERE type = 'specialRelationship'");

        assertEquals(1, collection.getItems().size());
        GraphRelationshipSnapshot snapshot = collection.getItems().get(0);
        assertEquals("firstNode", snapshot.getFromNodeName());
        assertEquals("secondNode", snapshot.getToNodeName());
        assertEquals("specialRelationship", snapshot.getDetail().getType());
    }


    @Test
    public void createRelationship() {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        snapshot.getDetail().setType("childOf");
        snapshot.setFromNodeId(firstNode.getGraphNodeId());
        snapshot.setToNodeId(secondNode.getGraphNodeId());
        snapshot.getDetail().setData("mydata");

        TransactionResult result = graphRelationshipRestAdapter.saveGraphRelationship(snapshot);
        assertTrue(result.wasSuccessful());
        GraphRelationship graphRelationship = GraphRelationship.load(result.getId());
        assertEquals("childOf", graphRelationship.getDetail().getType());
        assertEquals("mydata", graphRelationship.getDetail().getData());
        assertEquals("firstNode", graphRelationship.getFromGraphNode().getDetail().getName());
        assertEquals("secondNode", graphRelationship.getToGraphNode().getDetail().getName());
    }

}
