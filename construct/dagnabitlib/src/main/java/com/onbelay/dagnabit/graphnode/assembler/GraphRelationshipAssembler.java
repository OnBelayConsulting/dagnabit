package com.onbelay.dagnabit.graphnode.assembler;

import com.onbelay.dagnabit.common.assembler.AbstractEntityAssembler;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class GraphRelationshipAssembler extends AbstractEntityAssembler {
    
    public GraphRelationshipSnapshot assemble(GraphRelationship relationship) {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        initialize(snapshot, relationship);

        snapshot.setFromNodeId(relationship.getFromGraphNode().getGraphNodeId());
        snapshot.setFromNodeName(relationship.getFromGraphNode().getDetail().getName());
        snapshot.setFromCategory(relationship.getFromGraphNode().getDetail().getCategory());

        snapshot.setToNodeId(relationship.getToGraphNode().getGraphNodeId());
        snapshot.setToNodeName(relationship.getToGraphNode().getDetail().getName());
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
