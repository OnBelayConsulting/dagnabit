package com.onbelay.dagnabitapp.graphnode.adapter;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphRelationshipCollection;

import java.util.List;

public interface GraphRelationshipRestAdapter {

    TransactionResult saveGraphRelationship(GraphRelationshipSnapshot snapshot);

    TransactionResult saveGraphRelationships(List<GraphRelationshipSnapshot> snapshots);


    TransactionResult uploadFile(
            String name,
            byte[] bytes);


    GraphRelationshipCollection findGraphRelationships(
            int start,
            int limit,
            String query);

}
