package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.model.AbstractEntity;
import com.onbelay.core.entity.snapshot.EntitySlot;
import com.onbelay.core.exception.OBValidationException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.repository.GraphRelationshipRepository;
import com.onbelay.dagnabit.graphnode.repositoryimpl.GraphRelationshipRepositoryBean;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipDetail;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

import javax.persistence.*;

@Entity
@Table(name = "GRAPH_RELATIONSHIP")
@NamedQueries({
        @NamedQuery(
                name = GraphRelationshipRepositoryBean.FIND_BY_NAME,
                query = "SELECT relationship " +
                        "  FROM GraphRelationship relationship " +
                      "   WHERE relationship.detail.name = :name "),

        @NamedQuery(
                name = GraphRelationshipRepositoryBean.FIND_BY_EXTERNAL_REFERENCE,
                query = "SELECT relationship " +
                        "  FROM GraphRelationship relationship " +
                      "   WHERE relationship.detail.externalReferenceId = :externalReferenceId ")
})
public class GraphRelationship extends AbstractEntity {

    private Integer id;

    private GraphNode fromGraphNode;

    private GraphNode toGraphNode;

    private GraphRelationshipDetail detail = new GraphRelationshipDetail();

    @Id
    @Column(name="GRAPH_RELATIONSHIP_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="graphrelgen", sequenceName="GRAPH_RELATIONSHIP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "graphrelgen")
    public Integer getId() {
        return id;
    }

    public void setId(Integer graphNodeId) {
        this.id = graphNodeId;
    }


    @ManyToOne
    @JoinColumn(name = "FROM_GRAPH_NODE_ID")
    public GraphNode getFromGraphNode() {
        return fromGraphNode;
    }

    public void setFromGraphNode(GraphNode fromGraphNode) {
        this.fromGraphNode = fromGraphNode;
    }

    @ManyToOne
    @JoinColumn(name = "TO_GRAPH_NODE_ID")
    public GraphNode getToGraphNode() {
        return toGraphNode;
    }

    public void setToGraphNode(GraphNode toGraphNode) {
        this.toGraphNode = toGraphNode;
    }

    @Embedded
    public GraphRelationshipDetail getDetail() {
        return detail;
    }

    public void setDetail(GraphRelationshipDetail detail) {
        this.detail = detail;
    }

    public void createWith(GraphRelationshipSnapshot snapshot) {
        setRelationships(snapshot);
        detail.applyDefaults();
        detail.shallowCopyFrom(snapshot.getDetail());
        if (detail.getName() == null)
            detail.setName(
                    generateRelationshipName(
                            fromGraphNode,
                            toGraphNode,
                            detail.getType()));
        save();
    }

    public void updateWith(GraphRelationshipSnapshot snapshot) {
        if (snapshot.getEntityState() == EntityState.MODIFIED) {
            setRelationships(snapshot);
            detail.shallowCopyFrom(snapshot.getDetail());
            update();
        } else if (snapshot.getEntityState() == EntityState.DELETE) {
            getEntityRepository().delete(this);
        }
    }

    private void setRelationships(GraphRelationshipSnapshot snapshot) {

        if (snapshot.getFromNodeName() != null) {
            this.fromGraphNode = GraphNode.getGraphNodeRepository().findByName(snapshot.getFromNodeName());
        } else if (snapshot.getFromNodeId() != null)
            this.fromGraphNode = GraphNode.getGraphNodeRepository().load(snapshot.getFromNodeId().getEntityId());

        if (snapshot.getToNodeName() != null) {
            this.toGraphNode = GraphNode.getGraphNodeRepository().findByName(snapshot.getToNodeName());
        } else if (snapshot.getToNodeId() != null)
            this.toGraphNode = GraphNode.getGraphNodeRepository().load(snapshot.getToNodeId().getEntityId());
    }

    public void createWith(
            GraphNode fromGraphNode,
            GraphNode toGraphNode,
            String relationshipType) {

        this.fromGraphNode = fromGraphNode;
        this.toGraphNode = toGraphNode;
        detail.applyDefaults();
        detail.setType(relationshipType);

        detail.setName(generateRelationshipName(
                fromGraphNode,
                toGraphNode,
                relationshipType ));
        save();
    }


    public EntitySlot generateSlot() {
        return new EntitySlot(
                getEntityId(),
                detail.getName());
    }


    private String generateRelationshipName(
            GraphNode from,
            GraphNode to,
            String typeName) {

        return from.getDetail().getName() + " - " + typeName + " -> " + to.getDetail().getName();
    }

    public void validate() {
        if (fromGraphNode == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_NODE.getCode(), "From");
        if (toGraphNode == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_NODE.getCode(), "To");
        detail.validate();
    }

    @Transient
    public static  GraphRelationshipRepository getGraphRelationshipRepository() {
        return (GraphRelationshipRepository) ApplicationContextFactory.getBean(GraphRelationshipRepository.NAME);
    }



}
