package com.onbelay.dagnabit.graphnode.adapter;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.repository.GraphNodeRepository;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphNodeRestAdapter;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;

@WithMockUser
public class GraphNodeRestAdapterTest extends DagnabitSpringTestCase {

    @Autowired
    private GraphNodeRestAdapter graphNodeRestAdapter;

    @Autowired
    private GraphNodeRepository graphNodeRepository;

    private GraphNode firstNode;

    @Override
    public void setUp() {
        super.setUp();

        firstNode = GraphNodeFixture.createSavedGraphNode("HarryNode");
        flush();
    }

    @Test
    public void createNodes() {
        ArrayList<GraphNodeSnapshot> snapshots = new ArrayList<>();

        GraphNodeSnapshot node = new GraphNodeSnapshot();
        node.getDetail().setName("MyMyNode");
        node.getDetail().setCategory("MyCategory");
        node.getDetail().setData("data");
        snapshots.add(node);

        TransactionResult result = graphNodeRestAdapter.saveGraphNodes(snapshots);
        assertEquals(1, result.getEntityIds().size());
        GraphNode saved = graphNodeRepository.findByName("MyMyNode");
        assertNotNull(saved);
    }

    @Test
    public void findNodes() {
        GraphNodeCollection collection = graphNodeRestAdapter.findGraphNodes(0, 100, "WHERE name like 'H%'");
        assertEquals(1, collection.getSnapshots().size());
    }
}
