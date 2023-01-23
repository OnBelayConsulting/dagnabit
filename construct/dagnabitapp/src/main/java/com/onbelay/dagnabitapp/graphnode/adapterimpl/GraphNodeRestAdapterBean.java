package com.onbelay.dagnabitapp.graphnode.adapterimpl;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.service.GraphNodeService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.query.parsing.DefinedQueryBuilder;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphNodeRestAdapter;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphNodeRestAdapterBean implements GraphNodeRestAdapter {

    @Autowired
    private GraphNodeService graphNodeService;

    @Override
    public TransactionResult saveGraphNode(GraphNodeSnapshot snapshot) {
        return graphNodeService.save(snapshot);
    }

    @Override
    public TransactionResult saveGraphNodes(List<GraphNodeSnapshot> snapshots) {
        return graphNodeService.save(snapshots);
    }

    @Override
    public GraphNodeCollection findGraphNodes(
            int start,
            int limit,
            String query) {

        DefinedQueryBuilder queryBuilder = new DefinedQueryBuilder("GraphNode", query);

        QuerySelectedPage page = graphNodeService.findIdsByDefinedQuery(queryBuilder.build());

        if (page.getIds().size() == 0 || start >= page.getIds().size()) {
            return new GraphNodeCollection(
                    start,
                    limit,
                    page.getIds().size());
        }

        int toIndex = start + limit;

        if (toIndex > page.getIds().size())
            toIndex =  page.getIds().size();
        int fromIndex = start;

        List<Integer> selected = page.getIds().subList(fromIndex, toIndex);
        QuerySelectedPage limitedPageSelection = new QuerySelectedPage(
                selected,
                page.getOrderByClause());

        List<GraphNodeSnapshot> snapshots = graphNodeService.findByIds(limitedPageSelection);

        return new GraphNodeCollection(
                start,
                limit,
                page.getIds().size(),
                snapshots);
    }
}
