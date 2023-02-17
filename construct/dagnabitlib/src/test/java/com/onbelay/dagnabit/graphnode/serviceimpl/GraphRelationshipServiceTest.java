package com.onbelay.dagnabit.graphnode.serviceimpl;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.model.GraphRelationshipFixture;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GraphRelationshipServiceTest extends DagnabitSpringTestCase {

    @Autowired
    private GraphRelationshipService graphRelationshipService;


    private GraphNode firstNode;
    private GraphNode secondNode;

    private GraphRelationship graphRelationship;

    public void setUp() {

        super.setUp();

        firstNode = GraphNodeFixture.createSavedGraphNode(
                "firstNode",
                "elements");

        secondNode = GraphNodeFixture.createSavedGraphNode(
                "secondNode",
                "fire");
        flush();

        graphRelationship = GraphRelationshipFixture.createSavedRelationship(
                firstNode,
                secondNode,
                "specialRelationship");

        flush();
    }

    @Test
    public void findRelationshipByName() {

        DefinedQuery query = new DefinedQuery("GraphRelationship");
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "name",
                        ExpressionOperator.EQUALS,
                        graphRelationship.getDetail().getName()));

        List<GraphRelationshipSnapshot> snapshots =  graphRelationshipService.findByDefinedQuery(query);
        assertEquals(1, snapshots.size());
    }

    @Test
    public void findRelationshipByFromNodeName() {

        DefinedQuery query = new DefinedQuery("GraphRelationship");
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "fromNodeName",
                        ExpressionOperator.EQUALS,
                        "firstNode"));

        QuerySelectedPage page  =  graphRelationshipService.findIdsByDefinedQuery(query);

        List<GraphRelationshipSnapshot> snapshots =  graphRelationshipService.findByIds(page);
        assertEquals(1, snapshots.size());
    }

    @Test
    public void findRelationshipByToNodeName() {

        DefinedQuery query = new DefinedQuery("GraphRelationship");
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "toNodeName",
                        ExpressionOperator.EQUALS,
                        "secondNode"));

        QuerySelectedPage page  =  graphRelationshipService.findIdsByDefinedQuery(query);

        List<GraphRelationshipSnapshot> snapshots =  graphRelationshipService.findByIds(page);
        assertEquals(1, snapshots.size());
    }


    @Test
    public void findRelationshipByToNodeCategory() {

        DefinedQuery query = new DefinedQuery("GraphRelationship");
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "toNodeCategory",
                        ExpressionOperator.EQUALS,
                        "fire"));

        QuerySelectedPage page  =  graphRelationshipService.findIdsByDefinedQuery(query);

        List<GraphRelationshipSnapshot> snapshots =  graphRelationshipService.findByIds(page);
        assertEquals(1, snapshots.size());
    }


    @Test
    public void findRelationshipByFromNodeCategory() {

        DefinedQuery query = new DefinedQuery("GraphRelationship");
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "fromNodeCategory",
                        ExpressionOperator.EQUALS,
                        "elements"));

        QuerySelectedPage page  =  graphRelationshipService.findIdsByDefinedQuery(query);

        List<GraphRelationshipSnapshot> snapshots =  graphRelationshipService.findByIds(page);
        assertEquals(1, snapshots.size());
    }


    @Test
    public void createRelationship() {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        snapshot.getDetail().setType("childOf");
        snapshot.setFromNodeId(firstNode.generateSlot());
        snapshot.setToNodeId(secondNode.generateSlot());
        snapshot.getDetail().setData("mydata");

        TransactionResult result = graphRelationshipService.save(snapshot);
        flush();
        GraphRelationshipSnapshot saved = graphRelationshipService.load(result.getEntityId());
        assertNotNull(saved);
        assertEquals("childOf", saved.getDetail().getType());
        assertEquals("firstNode - childOf -> secondNode", saved.getDetail().getName());
    }


    @Test
    public void createRelationshipWithExplictName() {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        snapshot.getDetail().setType("childOf");
        snapshot.getDetail().setName("First is childOf Second");
        snapshot.setFromNodeId(firstNode.generateSlot());
        snapshot.setToNodeId(secondNode.generateSlot());
        snapshot.getDetail().setData("mydata");

        TransactionResult result = graphRelationshipService.save(snapshot);
        flush();
        GraphRelationshipSnapshot saved = graphRelationshipService.load(result.getEntityId());
        assertNotNull(saved);
        assertEquals("childOf", saved.getDetail().getType());
        assertEquals("First is childOf Second", saved.getDetail().getName());
    }


}
