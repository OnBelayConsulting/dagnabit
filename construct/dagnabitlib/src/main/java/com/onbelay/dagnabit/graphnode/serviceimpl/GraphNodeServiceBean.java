package com.onbelay.dagnabit.graphnode.serviceimpl;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.enums.EntityState;
import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.assembler.GraphNodeAssembler;
import com.onbelay.dagnabit.graphnode.exception.GraphNodeException;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.repository.GraphNodeRepository;
import com.onbelay.dagnabit.graphnode.service.GraphNodeService;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import com.onbelay.dagnabit.query.snapshot.QuerySelectedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GraphNodeServiceBean implements GraphNodeService {

    @Autowired
    private GraphNodeRepository graphNodeRepository;

    @Override
    public TransactionResult save(GraphNodeSnapshot snapshot) {

        if (snapshot.getEntityState() == EntityState.NEW) {
            GraphNode graphNode = new GraphNode();
            graphNode.createWith(snapshot);
            return new TransactionResult(graphNode.getEntityId());
        } else if (snapshot.getEntityState() == EntityState.MODIFIED || snapshot.getEntityState() == EntityState.DELETE)  {
            GraphNode graphNode = GraphNode.load(snapshot.getId());

            if (graphNode == null)
                throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_NODE.getCode());
            graphNode.updateWith(snapshot);
            return new TransactionResult(graphNode.getGraphNodeId());
        } else {
            return new TransactionResult();
        }
    }

    @Override
    public TransactionResult save(List<GraphNodeSnapshot> snapshots) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (GraphNodeSnapshot snapshot : snapshots) {
            TransactionResult child = save(snapshot);
            if (child.getId() != null)
                ids.add(child.getId());
        }
        return new TransactionResult(ids);
    }

    @Override
    public GraphNodeSnapshot load(Integer id) {
        GraphNode node = GraphNode.load(id);
        if (node == null)
            throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_NODE.getCode());

        GraphNodeAssembler assembler = new GraphNodeAssembler();
        return assembler.assemble(node);
    }

    @Override
    public List<GraphNodeSnapshot> findByDefinedQuery(DefinedQuery definedQuery) {
        List<Integer> ids = graphNodeRepository.findGraphNodeIds(definedQuery);
        GraphNodeAssembler assembler = new GraphNodeAssembler();
        return assembler.assemble(
                graphNodeRepository.fetchByIds(
                        new QuerySelectedPage(
                                ids,
                                definedQuery.getOrderByClause())));
    }

    @Override
    public QuerySelectedPage findIdsByDefinedQuery(DefinedQuery definedQuery) {
        List<Integer> ids = graphNodeRepository.findGraphNodeIds(definedQuery);
        return new QuerySelectedPage(ids, definedQuery.getOrderByClause());
    }

    @Override
    public List<GraphNodeSnapshot> findByIds(QuerySelectedPage page) {
        GraphNodeAssembler assembler = new GraphNodeAssembler();
        return assembler.assemble(
                graphNodeRepository.fetchByIds(page));
    }
}
