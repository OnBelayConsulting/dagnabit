package com.onbelay.dagnabit.graphnode.serviceimpl;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.service.GraphNodeService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.query.enums.ExpressionOperator;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.DefinedWhereExpression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeServiceTest extends DagnabitSpringTestCase {

    @Autowired
    private GraphNodeService graphNodeService;

    private GraphNode firstNode;
    private GraphNode secondNode;

    @Override
    public void setUp() {
        super.setUp();

        firstNode = GraphNodeFixture.createSavedGraphNode(
                "HarryNode",
                "elements");
        secondNode = GraphNodeFixture.createSavedGraphNode(
                "secondNode",
                "metals");
        flush();
    }

    @Test
    public void findNodeByName() {

        DefinedQuery definedQuery = new DefinedQuery("GraphNode");
        definedQuery.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "name",
                        ExpressionOperator.EQUALS,
                        "HarryNode"));

        List<GraphNodeSnapshot> nodes = graphNodeService.findByDefinedQuery(definedQuery);
        assertEquals(1, nodes.size());
        GraphNodeSnapshot node = nodes.get(0);
        assertEquals("elements", node.getDetail().getCategory());
    }

    @Test
    public void findNodeByCategory() {

        DefinedQuery definedQuery = new DefinedQuery("GraphNode");
        definedQuery.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "category",
                        ExpressionOperator.EQUALS,
                        "elements"));

        List<GraphNodeSnapshot> nodes = graphNodeService.findByDefinedQuery(definedQuery);
        assertEquals(1, nodes.size());
        GraphNodeSnapshot node = nodes.get(0);
        assertEquals("HarryNode", node.getDetail().getName());
    }

    @Test
    public void findNodeByNodeId() {

        DefinedQuery definedQuery = new DefinedQuery("GraphNode");
        definedQuery.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "id",
                        ExpressionOperator.EQUALS,
                        firstNode.getGraphNodeId()));

        List<GraphNodeSnapshot> nodes = graphNodeService.findByDefinedQuery(definedQuery);
        assertEquals(1, nodes.size());
        GraphNodeSnapshot node = nodes.get(0);
        assertEquals("HarryNode", node.getDetail().getName());
    }


    @Test
    public void createNodes() {

        ArrayList<GraphNodeSnapshot> snapshots = new ArrayList<>();

        GraphNodeSnapshot node = new GraphNodeSnapshot();
        node.getDetail().setName("MyMyNode");
        node.getDetail().setCategory("MyCategory");
        node.getDetail().setData("data");
        snapshots.add(node);

        TransactionResult result = graphNodeService.save(snapshots);
        flush();

        GraphNode graphNode = GraphNode.findByName("MyMyNode");
        assertNotNull(graphNode);
        assertEquals("MyCategory", graphNode.getDetail().getCategory());
        assertEquals("data", graphNode.getDetail().getData());
        assertEquals(1, graphNode.getDetail().getWeight().intValue());
    }



}
