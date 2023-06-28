package com.onbelay.dagnabit.dagmodel.factoryimpl;

import com.onbelay.dagnabit.dagmodel.components.DagModelImpl;
import com.onbelay.dagnabit.dagmodel.factory.DagModelFactory;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DagModelFactoryBean implements DagModelFactory {

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
}
