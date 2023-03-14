package com.onbelay.dagnabitapp.graphnode.subscribe.subscriber;

import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.subscribe.converter.GraphRelationshipConverter;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphRelationshipSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphRelationshipUpdaterBean implements GraphRelationshipUpdater{

    @Autowired
    private GraphRelationshipService graphRelationshipService;


    @Override
    public TransactionResult updateGraphRelationships(List<SubGraphRelationshipSnapshot> nodesIn) {

        GraphRelationshipConverter converter = new GraphRelationshipConverter();
        List<GraphRelationshipSnapshot> snapshots = converter.convert(nodesIn);

        for (GraphRelationshipSnapshot snapshot : snapshots) {
            if (snapshot.getEntityState() == EntityState.MODIFIED || snapshot.getEntityState() == EntityState.NEW) {
                if (snapshot.getEntityId() == null) {
                    GraphRelationshipSnapshot existing = graphRelationshipService.findByExternalReference(snapshot.getDetail().getExternalReferenceId());
                    if (existing == null) {
                        snapshot.setEntityState(EntityState.NEW);
                    } else {
                        snapshot.setEntityState(EntityState.MODIFIED);
                        snapshot.setEntityId(existing.getEntityId());
                    }
                }

            } else if (snapshot.getEntityState() == EntityState.DELETE) {
                if (snapshot.getEntityId() == null) {
                    GraphRelationshipSnapshot existing = graphRelationshipService.findByExternalReference(snapshot.getDetail().getExternalReferenceId());
                    if (existing == null) {
                        snapshot.setEntityState(EntityState.UNMODIFIED);
                    } else {
                        snapshot.setEntityId(existing.getEntityId());
                    }
                }
            }
        }


        return graphRelationshipService.save(snapshots);
    }
}
