package com.onbelay.dagnabit.graphnode.factoryimpl;

import com.onbelay.dagnabit.graph.components.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagRelationship;
import com.onbelay.dagnabit.graphnode.factory.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabit.query.parsing.DefinedQueryBuilder;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DagModelFactoryBean implements DagModelFactory {

    @Autowired
    private GraphRelationshipService graphRelationshipService;

    private ConcurrentHashMap<String, DagModel> modelMap = new ConcurrentHashMap<>(5, .75f, 10);


    @Override
    public void removeModel(String modelName) {
        DagModel dagModel = modelMap.remove(modelName);

    }

    @Override
    public void cleanUp() {
        modelMap.clear();
    }

    @Override
    public DagModel findModel(String modelName) {
        return modelMap.get(modelName);
    }

    @Override
    public List<DagModel> findModels() {
        ArrayList<DagModel> models = new ArrayList<>();
        modelMap.values().forEach(c -> models.add(c));
        return models;
    }

    @Override
    public DagModel newModel(String modelName) {
        DagModelImpl dagModel = new DagModelImpl(modelName);
        modelMap.put(modelName, dagModel);
        return dagModel;
    }

    @Override
    public DagModel newModel(
            String modelName,
            String selectingQuery) {

        DagModelImpl dagModel = new DagModelImpl(modelName);
        modelMap.put(modelName, dagModel);

        DefinedQueryBuilder builder = new DefinedQueryBuilder("GraphRelationship", selectingQuery);
        DefinedQuery definedQuery = builder.build();

        List<GraphRelationshipSnapshot> relationships =  graphRelationshipService.findByDefinedQuery(definedQuery);

        relationships.forEach( c->
                {
                   DagNode node = dagModel.addNode(c.getFromNodeName(), c.getFromCategory());
                   node.setReferenceNo(c.getFromNodeId());

                   node = dagModel.addNode(c.getToNodeName(), c.getToCategory());
                   node.setReferenceNo(c.getToNodeId());

                   DagRelationship relationship = dagModel.addRelationship(
                           dagModel.getNode(c.getFromNodeName()),
                           c.getDetail().getType(),
                           dagModel.getNode(c.getToNodeName()));

                   relationship.setWeight(c.getDetail().getWeight());
                   relationship.setReferenceNo(c.getId());
                }
        );


        return dagModel;
    }
}
