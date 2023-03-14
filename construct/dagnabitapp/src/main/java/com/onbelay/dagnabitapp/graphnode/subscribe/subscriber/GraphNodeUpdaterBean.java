package com.onbelay.dagnabitapp.graphnode.subscribe.subscriber;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.service.GraphNodeService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.subscribe.converter.GraphNodeConverter;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphNodeSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphNodeUpdaterBean implements GraphNodeUpdater{

    @Autowired
    private GraphNodeService graphNodeService;


    @Override
    public TransactionResult updateGraphNodes(List<SubGraphNodeSnapshot> nodesIn) {

        GraphNodeConverter converter = new GraphNodeConverter();
        List<GraphNodeSnapshot> snapshots = converter.convert(nodesIn);

        for (GraphNodeSnapshot snapshot : snapshots) {
            if (snapshot.getEntityState() == EntityState.MODIFIED || snapshot.getEntityState() == EntityState.NEW) {
                if (snapshot.getEntityId() == null) {
                    GraphNodeSnapshot existing = graphNodeService.findByExternalReference(snapshot.getDetail().getExternalReferenceId());
                    if (existing == null) {
                        snapshot.setEntityState(EntityState.NEW);
                    } else {
                        snapshot.setEntityState(EntityState.MODIFIED);
                        snapshot.setEntityId(existing.getEntityId());
                    }
                }

            } else if (snapshot.getEntityState() == EntityState.DELETE) {
                if (snapshot.getEntityId() == null) {
                    GraphNodeSnapshot existing = graphNodeService.findByExternalReference(snapshot.getDetail().getExternalReferenceId());
                    if (existing == null) {
                        snapshot.setEntityState(EntityState.UNMODIFIED);
                    } else {
                        snapshot.setEntityId(existing.getEntityId());
                    }
                }
            }
        }

        return graphNodeService.save(snapshots);
    }
}
