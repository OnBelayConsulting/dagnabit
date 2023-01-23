package com.onbelay.dagnabit.graph.adapter;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graphnode.factory.DagModelFactory;
import com.onbelay.dagnabitapp.graph.adapter.GraphingRestAdapter;
import com.onbelay.dagnabitapp.graph.snapshot.DagNodeCollection;
import com.onbelay.dagnabitapp.graph.snapshot.DagNodePathCollection;
import com.onbelay.dagnabitapp.graph.snapshot.GraphModelCollection;
import com.onbelay.dagnabitapp.graph.snapshot.GraphModelSnapshot;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GraphingRestAdapterTest extends DagnabitSpringTestCase {

    @Autowired
    private DagModelFactory dagModelFactory;

    @Autowired
    private GraphingRestAdapter graphingRestAdapter;

    private DagModel dagModel;

    public void setUp() {
        super.setUp();
        dagModel = dagModelFactory.newModel("myModel");

        dagModel.addNode("firstNode", "family");
        dagModel.addNode("secondNode", "family");
        dagModel.addNode("thirdNode", "family");
        dagModel.addNode("fourthNode", "family");
        dagModel.addNode("fifthNode", "family");

        dagModel.addRelationship(
                dagModel.getNode("firstNode"),
                "parentOf",
                dagModel.getNode("secondNode"));
        dagModel.addRelationship(
                dagModel.getNode("firstNode"),
                "parentOf",
                dagModel.getNode("thirdNode"));
        dagModel.addRelationship(
                dagModel.getNode("secondNode"),
                "parentOf",
                dagModel.getNode("fourthNode"));
        dagModel.addRelationship(
                dagModel.getNode("secondNode"),
                "parentOf",
                dagModel.getNode("fifthNode"));



    }

    @Override
    public void afterRun() throws Throwable {
        super.afterRun();
        dagModelFactory.cleanUp();
    }

    @Test
    public void findModels() {
         GraphModelCollection collection = graphingRestAdapter.findGraphModels(0, 100, null);
         assertEquals(1, collection.getItems().size());
         GraphModelSnapshot snapshot = collection.getItems().get(0);
         assertEquals("myModel", snapshot.getName());
    }

    @Test
    public void findDescendents() {
        DagNodeCollection collection = graphingRestAdapter.findDescendents(
                "myModel",
                "firstNode",
                "parentOf",
                0,
                100);
        assertEquals(4, collection.getItems().size());
    }

    @Test
    public void findCycles() {
        dagModel.addRelationship(
                dagModel.getNode("fifthNode"),
                "parentOf",
                dagModel.getNode("firstNode"));

        DagNodePathCollection collection = graphingRestAdapter.fetchCycleReport(
                "myModel",
                "parentOf");

        assertTrue(collection.isCyclic());

        assertEquals(3, collection.getItems().size());
        DagNodePath path = collection.getItems().get(0);

    }


}
