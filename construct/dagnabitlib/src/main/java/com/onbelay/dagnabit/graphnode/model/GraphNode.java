package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.core.entity.component.ApplicationContextFactory;
import com.onbelay.core.entity.enums.EntityState;
import com.onbelay.core.entity.model.AbstractEntity;
import com.onbelay.core.entity.snapshot.EntityId;
import com.onbelay.dagnabit.graphnode.repository.GraphNodeRepository;
import com.onbelay.dagnabit.graphnode.repositoryimpl.GraphNodeRepositoryBean;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeDetail;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;

import javax.persistence.*;

@Entity
@Table(name = "GRAPH_NODE")
@NamedQueries({
        @NamedQuery(
                name = GraphNodeRepositoryBean.FIND_NODE_BY_NAME,
                query = "SELECT node " +
                        "  FROM GraphNode node " +
                      "   WHERE node.detail.name = :name "),

        @NamedQuery(
                name = GraphNodeRepositoryBean.FIND_BY_EXTERNAL_REFERENCE,
                query = "SELECT node " +
                        "  FROM GraphNode node " +
                      "   WHERE node.detail.externalReferenceId = :externalReferenceId ")
})
public class GraphNode extends AbstractEntity {

    private Integer id;

    private GraphNodeDetail detail = new GraphNodeDetail();


    @Id
    @Column(name="GRAPH_NODE_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="graphnodegen", sequenceName="GRAPH_NODE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "graphnodegen")
    public Integer getId() {
        return id;
    }

    public void setId(Integer graphNodeId) {
        this.id = graphNodeId;
    }

    @Embedded
    public GraphNodeDetail getDetail() {
        return detail;
    }

    public void setDetail(GraphNodeDetail detail) {
        this.detail = detail;
    }


    public void createWith(GraphNodeSnapshot snapshot) {
        detail.applyDefaults();
        detail.shallowCopyFrom(snapshot.getDetail());
        save();
    }


    public void createWith(String name, String data) {
        detail.applyDefaults();
        detail.setName(name);
        detail.setData(data);
        save();
    }

    public void updateWith(GraphNodeSnapshot snapshot) {
        if (snapshot.getEntityState() == EntityState.MODIFIED) {
            detail.shallowCopyFrom(snapshot.getDetail());
            update();
        } else if (snapshot.getEntityState() == EntityState.DELETE) {
            getEntityRepository().delete(this);
        }
    }

    @Override
    public EntityId generateEntityId() {
        return new EntityId(
                getId(),
                detail.getName(),
                detail.getCategory(),
                false);
    }


    public void validate() {
        detail.validate();
    }

    @Transient
    public static  GraphNodeRepository getGraphNodeRepository() {
        return (GraphNodeRepository) ApplicationContextFactory.getBean(GraphNodeRepository.NAME);
    }
}
