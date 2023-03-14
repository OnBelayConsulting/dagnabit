package com.onbelay.dagnabitapp.graphnode.subscribe.subscriber;

import com.onbelay.core.entity.model.AuditManager;
import com.onbelay.core.exception.OBRuntimeException;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphNodeSnapshot;
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
public class GraphNodeSubscriberConfig {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private AuditManager auditManager;

    @Autowired
    private GraphNodeUpdater graphNodeUpdater;

    @Bean
    @Profile("messaging")
    public Consumer<Message<List<SubGraphNodeSnapshot>>> graphNodeConsumer() {

        return msg -> {
            logger.info("consumer: msg = {}", msg);

            auditManager.setCurrentAuditUserName("obupdate");
            auditManager.setCurrentAuditComments("via messaging");

            List<SubGraphNodeSnapshot> externalSnapshots = msg.getPayload();
            graphNodeUpdater.updateGraphNodes(externalSnapshots);
            try {
            } catch (OBRuntimeException e) {
                logger.error("Graph Node save failed");
                throw new NonTransientDataAccessResourceException("GraphNode save failed.");
            }

        };
    }



}
