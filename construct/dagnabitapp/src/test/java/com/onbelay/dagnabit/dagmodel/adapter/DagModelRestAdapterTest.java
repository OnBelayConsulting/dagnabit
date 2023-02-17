package com.onbelay.dagnabit.dagmodel.adapter;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.dagmodel.factory.DagModelFactory;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagNodePath;
import com.onbelay.dagnabitapp.dagmodel.adapter.DagModelRestAdapter;
import com.onbelay.dagnabitapp.dagmodel.snapshot.DagModelCollection;
import com.onbelay.dagnabitapp.dagmodel.snapshot.DagModelSnapshot;
import com.onbelay.dagnabitapp.dagmodel.snapshot.DagNodeCollection;
import com.onbelay.dagnabitapp.dagmodel.snapshot.DagNodePathCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
public class DagModelRestAdapterTest extends DagnabitSpringTestCase {

    @Autowired
    private DagModelFactory dagModelFactory;

    @Autowired
    private DagModelRestAdapter dagModelRestAdapter;

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
         DagModelCollection collection = dagModelRestAdapter.findGraphModels(0, 100, null);
         assertEquals(1, collection.getSnapshots().size());
         DagModelSnapshot snapshot = collection.getSnapshots().get(0);
         assertEquals("myModel", snapshot.getName());
    }

    @Test
    public void findDescendants() {
        DagNodeCollection collection = dagModelRestAdapter.findDescendants(
                "myModel",
                "firstNode",
                "parentOf",
                0,
                100);
        assertEquals(4, collection.getSnapshots().size());
    }

    @Test
    public void findCycles() {
        dagModel.addRelationship(
                dagModel.getNode("fifthNode"),
                "parentOf",
                dagModel.getNode("firstNode"));

        DagNodePathCollection collection = dagModelRestAdapter.fetchCycleReport(
                "myModel",
                "parentOf");

        assertTrue(collection.isCyclic());

        assertEquals(3, collection.getSnapshots().size());
        DagNodePath path = collection.getSnapshots().get(0);

    }


}
