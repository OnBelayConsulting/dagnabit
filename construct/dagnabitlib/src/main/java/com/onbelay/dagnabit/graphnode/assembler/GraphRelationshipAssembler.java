package com.onbelay.dagnabit.graphnode.assembler;

import com.onbelay.core.entity.assembler.EntityAssembler;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class GraphRelationshipAssembler extends EntityAssembler {
    
    public GraphRelationshipSnapshot assemble(GraphRelationship relationship) {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        setEntityAttributes(relationship, snapshot);

        snapshot.setFromNodeId(relationship.getFromGraphNode().generateEntityId());
        snapshot.setFromCategory(relationship.getFromGraphNode().getDetail().getCategory());

        snapshot.setToNodeId(relationship.getToGraphNode().generateEntityId());
        snapshot.setToCategory(relationship.getToGraphNode().getDetail().getCategory());


        snapshot.getDetail().shallowCopyFrom(relationship.getDetail());
        return snapshot;        
    }
    
    public List<GraphRelationshipSnapshot> assemble(List<GraphRelationship> relationships) {
        return relationships
                .stream()
                .map(c-> assemble(c))
                .collect(Collectors.toUnmodifiableList());
    }
    
}
