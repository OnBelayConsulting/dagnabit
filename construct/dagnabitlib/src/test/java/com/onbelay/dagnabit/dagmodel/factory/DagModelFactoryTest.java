package com.onbelay.dagnabit.dagmodel.factory;

import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DagModelFactoryTest extends DagnabitSpringTestCase {

    @Autowired
    private DagModelFactory dagModelFactory;


    @Test
    public void createModel() {

        DagModel dagModel = dagModelFactory.newModel("myModel");

        assertEquals(1, dagModelFactory.findModels().size());
    }


}
