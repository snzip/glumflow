package org.thingsboard.server.actors.stats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.actors.ActorSystemContext;
import org.thingsboard.server.actors.service.ContextAwareActor;
import org.thingsboard.server.actors.service.ContextBasedCreator;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.Event;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;

@Slf4j
public class StatsActor extends ContextAwareActor {

    private final ObjectMapper mapper = new ObjectMapper();

    public StatsActor(ActorSystemContext context) {
        super(context);
    }

    @Override
    protected boolean process(TbActorMsg msg) {
        //TODO Move everything here, to work with TbActorMsg\
        return false;
    }

    @Override
    public void onReceive(Object msg) {
        log.debug("Received message: {}", msg);
        if (msg instanceof StatsPersistMsg) {
            try {
                onStatsPersistMsg((StatsPersistMsg) msg);
            } catch (Exception e) {
                log.warn("Failed to persist statistics: {}", msg, e);
            }
        }
    }

    public void onStatsPersistMsg(StatsPersistMsg msg) throws Exception {
        Event event = new Event();
        event.setEntityId(msg.getEntityId());
        event.setTenantId(msg.getTenantId());
        event.setType(DataConstants.STATS);
        event.setBody(toBodyJson(systemContext.getDiscoveryService().getCurrentServer().getServerAddress(), msg.getMessagesProcessed(), msg.getErrorsOccurred()));
        systemContext.getEventService().save(event);
    }

    private JsonNode toBodyJson(ServerAddress server, long messagesProcessed, long errorsOccurred) {
        return mapper.createObjectNode().put("server", server.toString()).put("messagesProcessed", messagesProcessed).put("errorsOccurred", errorsOccurred);
    }

    public static class ActorCreator extends ContextBasedCreator<StatsActor> {
        private static final long serialVersionUID = 1L;

        public ActorCreator(ActorSystemContext context) {
            super(context);
        }

        @Override
        public StatsActor create() {
            return new StatsActor(context);
        }
    }
}
