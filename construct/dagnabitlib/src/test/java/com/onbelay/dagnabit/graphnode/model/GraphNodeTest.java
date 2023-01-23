package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import org.junit.Test;

public class GraphNodeTest extends DagnabitSpringTestCase {

    @Test
    public void createGraphNode() {
        GraphNode graphNode = new GraphNode();
        graphNode.createWith("dsdd", "ddd");
        flush();

        GraphNode nodeCreated = GraphNode.load(graphNode.getGraphNodeId());
        assertNotNull(nodeCreated);

    }

}
