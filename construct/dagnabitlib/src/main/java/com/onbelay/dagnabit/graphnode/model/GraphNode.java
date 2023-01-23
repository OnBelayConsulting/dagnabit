package com.onbelay.dagnabit.graphnode.model;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;
import com.onbelay.dagnabit.common.model.AbstractEntity;
import com.onbelay.dagnabit.enums.EntityState;
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
                      "   WHERE node.detail.name = :name ")

})
public class GraphNode extends AbstractEntity<GraphNode> {

    private Integer graphNodeId;

    private GraphNodeDetail detail = new GraphNodeDetail();

    @Transient
    public Integer getEntityId() {
        return graphNodeId;
    }

    @Id
    @Column(name="GRAPH_NODE_ID", insertable = false, updatable = false)
    @SequenceGenerator(name="graphnodegen", sequenceName="GRAPH_NODE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "graphnodegen")
    public Integer getGraphNodeId() {
        return graphNodeId;
    }

    public void setGraphNodeId(Integer graphNodeId) {
        this.graphNodeId = graphNodeId;
    }

    public static GraphNode load(Integer id) {
        return getEntityRepository().load(GraphNode.class, id);
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

    public static GraphNode findByName(String name) {
        return getEntityRepository().findByName(name);
    }

    public void updateWith(GraphNodeSnapshot snapshot) {
        if (snapshot.getEntityState() == EntityState.MODIFIED) {
            detail.shallowCopyFrom(snapshot.getDetail());
            update();
        } else if (snapshot.getEntityState() == EntityState.DELETE) {
            delete();
        }
    }

    public void validate() {
        detail.validate();
    }

    @Transient
    public static  GraphNodeRepository getEntityRepository() {
        return (GraphNodeRepository) ApplicationContextFactory.getBean(GraphNodeRepository.NAME);
    }
}
