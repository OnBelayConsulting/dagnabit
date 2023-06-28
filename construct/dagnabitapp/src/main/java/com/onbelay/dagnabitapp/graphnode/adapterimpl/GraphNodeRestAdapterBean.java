package com.onbelay.dagnabitapp.graphnode.adapterimpl;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.query.parsing.DefinedQueryBuilder;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.service.GraphNodeService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphNodeRestAdapter;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphNodeFileReader;
import com.onbelay.dagnabitapp.graphnode.filewriter.GraphNodeFileWriter;
import com.onbelay.dagnabitapp.graphnode.snapshot.FileResult;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

        DefinedQuery definedQuery;
        if (query == null) {
            definedQuery = new DefinedQuery("GraphNode");
        } else {
            DefinedQueryBuilder queryBuilder = new DefinedQueryBuilder("GraphNode", query);
            definedQuery = queryBuilder.build();
        }

        if (definedQuery.getOrderByClause().hasExpressions() == false) {
            definedQuery.getOrderByClause().addOrderExpression(
                    new DefinedOrderExpression("name"));
        }

        QuerySelectedPage page = graphNodeService.findIdsByDefinedQuery(definedQuery);

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

    @Override
    public TransactionResult uploadFile(
            String name,
            byte[] fileContents) {

        ByteArrayInputStream fileStream = new ByteArrayInputStream(fileContents);

        GraphNodeFileReader reader = new GraphNodeFileReader(fileStream);
        List<GraphNodeSnapshot> snapshots = reader.readFile();
        return graphNodeService.save(snapshots);
    }

    @Override
    public FileResult generateCSVFile(String query) {
        DefinedQueryBuilder queryBuilder = new DefinedQueryBuilder("GraphNode", query);

        QuerySelectedPage page = graphNodeService.findIdsByDefinedQuery(queryBuilder.build());
        List<GraphNodeSnapshot> nodes = graphNodeService.findByIds(page);

        GraphNodeFileWriter writer = new GraphNodeFileWriter(nodes);

        return new FileResult("graphnodes.csv", writer.getContents());
    }
}
