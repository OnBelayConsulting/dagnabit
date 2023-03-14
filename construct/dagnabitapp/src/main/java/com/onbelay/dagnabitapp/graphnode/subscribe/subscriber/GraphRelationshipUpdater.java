package com.onbelay.dagnabitapp.graphnode.subscribe.subscriber;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphRelationshipSnapshot;

import java.util.List;

public interface GraphRelationshipUpdater {

    TransactionResult updateGraphRelationships(List<SubGraphRelationshipSnapshot> nodesIn);



}
