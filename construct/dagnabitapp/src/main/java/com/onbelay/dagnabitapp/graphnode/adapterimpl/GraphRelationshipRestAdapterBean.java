package com.onbelay.dagnabitapp.graphnode.adapterimpl;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.core.query.parsing.DefinedQueryBuilder;
import com.onbelay.core.query.snapshot.QuerySelectedPage;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphRelationshipRestAdapter;
import com.onbelay.dagnabitapp.graphnode.filereader.GraphRelationshipFileReader;
import com.onbelay.dagnabitapp.graphnode.filewriter.GraphRelationshipFileWriter;
import com.onbelay.dagnabitapp.graphnode.snapshot.FileResult;
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
    public FileResult generateCSVFile(String query) {
        DefinedQueryBuilder queryBuilder = new DefinedQueryBuilder("GraphRelationship", query);
        List<GraphRelationshipSnapshot> relationships = graphRelationshipService.findByDefinedQuery(queryBuilder.build());
        GraphRelationshipFileWriter writer = new GraphRelationshipFileWriter(relationships);

        return new FileResult(
                "graphrelationships.csv",
                writer.getContents());
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
