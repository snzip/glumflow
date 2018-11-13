package org.thingsboard.server.dao.model.type;

import com.datastax.driver.extras.codecs.json.JacksonJsonCodec;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonCodec extends JacksonJsonCodec<JsonNode> {

    public JsonCodec() {
        super(JsonNode.class);
    }

}
