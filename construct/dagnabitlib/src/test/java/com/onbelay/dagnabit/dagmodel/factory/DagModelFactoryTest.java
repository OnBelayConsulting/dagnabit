package com.onbelay.dagnabit.dagmodel.factory;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.dagmodel.factory.DagModelFactory;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DagModelFactoryTest extends DagnabitSpringTestCase {

    private GraphNode fromGraphNode;
    private GraphNode toGraphNode;

    @Autowired
    private DagModelFactory dagModelFactory;


    public void setUp() {
        super.setUp();
        fromGraphNode = GraphNodeFixture.createSavedGraphNode("from");
        toGraphNode = GraphNodeFixture.createSavedGraphNode("to");
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(toGraphNode, fromGraphNode, "parent");
        flush();
    }

    @Test
    public void createModelWithQuery() {
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(toGraphNode, fromGraphNode, "boss");
        flush();

        GraphRelationship created = GraphRelationship.load(relationship.getGraphRelationshipId());

        DagModel dagModel = dagModelFactory.newModel(
                "myModel",
                "WHERE type = 'boss'");

        assertEquals(1, dagModel.getRelationships().size());
        DagRelationship dagRelationship = dagModel.getRelationships().get(0);
        assertEquals("boss", dagRelationship.getRelationshipType().getName());
    }

    @Test
    public void createModelAll() {
        GraphRelationship relationship = new GraphRelationship();
        relationship.createWith(toGraphNode, fromGraphNode, "boss");
        flush();

        GraphRelationship created = GraphRelationship.load(relationship.getGraphRelationshipId());

        DagModel dagModel = dagModelFactory.newModel(
                "myModel",
                "WHERE ");

        assertEquals(2, dagModel.getRelationships().size());
    }


}
