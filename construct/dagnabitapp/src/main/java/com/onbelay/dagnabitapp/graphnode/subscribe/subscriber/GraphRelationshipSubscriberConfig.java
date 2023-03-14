package com.onbelay.dagnabitapp.graphnode.subscribe.subscriber;

import com.onbelay.core.entity.model.AuditManager;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphRelationshipSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;


@Configuration
public class GraphRelationshipSubscriberConfig {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private AuditManager auditManager;

    @Autowired
    private GraphRelationshipUpdater graphRelationshipUpdater;

    @Bean
    @Profile("messaging")
    public Consumer<Message<List<SubGraphRelationshipSnapshot>>> graphRelationshipConsumer() {

        return msg -> {
            logger.info("consumer: msg = {}", msg);

            auditManager.setCurrentAuditUserName("obupdate");
            auditManager.setCurrentAuditComments("via messaging");

            List<SubGraphRelationshipSnapshot> externalSnapshots = msg.getPayload();
            graphRelationshipUpdater.updateGraphRelationships(externalSnapshots);
            try {
            } catch (OBRuntimeException e) {
                logger.error("Graph Relationship save failed");
                throw new NonTransientDataAccessResourceException("GraphRelationship save failed.");
            }

        };
    }



}
