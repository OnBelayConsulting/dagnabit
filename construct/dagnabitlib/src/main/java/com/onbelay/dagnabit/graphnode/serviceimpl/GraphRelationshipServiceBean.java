package com.onbelay.dagnabit.graphnode.serviceimpl;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.enums.EntityState;
import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.assembler.GraphRelationshipAssembler;
import com.onbelay.dagnabit.graphnode.exception.GraphNodeException;
import com.onbelay.dagnabit.graphnode.model.GraphRelationship;
import com.onbelay.dagnabit.graphnode.repository.GraphRelationshipRepository;
import com.onbelay.dagnabit.graphnode.service.GraphRelationshipService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GraphRelationshipServiceBean implements GraphRelationshipService {

    @Autowired
    private GraphRelationshipRepository graphRelationshipRepository;

    @Override
    public TransactionResult save(GraphRelationshipSnapshot snapshot) {

        if (snapshot.getEntityState() == EntityState.NEW) {
            GraphRelationship relationship = new GraphRelationship();
            relationship.createWith(snapshot);
            return new TransactionResult(relationship.getGraphRelationshipId());
        } else if (snapshot.getEntityState() == EntityState.MODIFIED || snapshot.getEntityState() == EntityState.DELETE)  {
            GraphRelationship relationship = GraphRelationship.load(snapshot.getId());

            if (relationship == null)
                throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_NODE.getCode());
            relationship.updateWith(snapshot);
            return new TransactionResult(relationship.getGraphRelationshipId());
        } else {
            return new TransactionResult();
        }
    }

    @Override
    public QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery) {
        List<Integer> ids = graphRelationshipRepository.findGraphRelationshipIds(definedQuery);
        return new QuerySelectedPage(ids, definedQuery.getOrderByClause());
    }

    @Override
    public List<GraphRelationshipSnapshot> findByIds(QuerySelectedPage page) {
        List<GraphRelationship> relationships = graphRelationshipRepository.fetchByIds(page);
        GraphRelationshipAssembler assembler = new GraphRelationshipAssembler();
        return assembler.assemble(relationships);
    }

    @Override
    public TransactionResult save(List<GraphRelationshipSnapshot> snapshots) {
        List<Integer> ids = new ArrayList<>();
        for (GraphRelationshipSnapshot snapshot : snapshots) {
            TransactionResult child = save(snapshot);
            if (child.getId() != null)
                ids.add(child.getId());
        }
        return new TransactionResult(ids);
    }

    @Override
    public GraphRelationshipSnapshot load(Integer id) {
        GraphRelationship relationship = GraphRelationship.load(id);
        if (relationship == null)
            throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_RELATIONSHIP.getCode());
        GraphRelationshipAssembler assembler = new GraphRelationshipAssembler();

        return assembler.assemble(relationship);
    }

    @Override
    public List<GraphRelationshipSnapshot> findByDefinedQuery(DefinedQuery definedQuery) {
        List<Integer> ids = graphRelationshipRepository.findGraphRelationshipIds(definedQuery);
        GraphRelationshipAssembler assembler = new GraphRelationshipAssembler();
        return assembler.assemble(
                graphRelationshipRepository.fetchByIds(
                        new QuerySelectedPage(
                                ids,
                                definedQuery.getOrderByClause())));
    }
}
