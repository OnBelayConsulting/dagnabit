package com.onbelay.dagnabitapp.graphnode.adapter;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.snapshot.FileResult;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;

import java.util.List;

public interface GraphNodeRestAdapter {

    TransactionResult saveGraphNode(GraphNodeSnapshot snapshot);

    TransactionResult saveGraphNodes(List<GraphNodeSnapshot> snapshots);

    GraphNodeCollection findGraphNodes(
            int start,
            int limit,
            String query);

    TransactionResult uploadFile(
            String name,
            byte[] bytes);
}
