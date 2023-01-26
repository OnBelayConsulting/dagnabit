package com.onbelay.dagnabitapp.graphnode.adapterimpl;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabit.query.parsing.DefinedQueryBuilder;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphRelationshipRestAdapter;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphRelationshipRestAdapter;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphNodeFileReader;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphRelationshipFileReader;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphRelationshipCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class GraphRelationshipRestAdapterBean implements GraphRelationshipRestAdapter {

    @Autowired
    private GraphRelationshipService graphRelationshipService;

    @Override
    public TransactionResult saveGraphRelationship(GraphRelationshipSnapshot snapshot) {
        return graphRelationshipService.save(snapshot);
    }

    @Override
    public TransactionResult saveGraphRelationships(List<GraphRelationshipSnapshot> snapshots) {
        return graphRelationshipService.save(snapshots);
    }

    @Override
    public GraphRelationshipCollection findGraphRelationships(
            int start,
            int limit,
            String query) {

        DefinedQueryBuilder queryBuilder = new DefinedQueryBuilder("GraphRelationship", query);
        
        QuerySelectedPage page = graphRelationshipService.findIdsByDefinedQuery(queryBuilder.build());

        if (page.getIds().size() == 0 || start >= page.getIds().size()) {
            return new GraphRelationshipCollection(
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

        List<GraphRelationshipSnapshot> snapshots = graphRelationshipService.findByIds(limitedPageSelection);

        return new GraphRelationshipCollection(
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

        GraphRelationshipFileReader reader = new GraphRelationshipFileReader(fileStream);
        List<GraphRelationshipSnapshot> snapshots = reader.readFile();
        return graphRelationshipService.save(snapshots);
    }

}
