package org.thingsboard.rule.engine.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.rule.engine.api.TbNodeConfiguration;
import org.thingsboard.rule.engine.api.TbNodeException;
import org.thingsboard.server.common.msg.TbMsgMetaData;

import java.util.Map;

/**
 * Created by ashvayka on 19.01.18.
 */
public class TbNodeUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String VARIABLE_TEMPLATE = "${%s}";


    public static <T> T convert(TbNodeConfiguration configuration, Class<T> clazz) throws TbNodeException {
        try {
            return mapper.treeToValue(configuration.getData(), clazz);
        } catch (JsonProcessingException e) {
            throw new TbNodeException(e);
        }
    }

    public static String processPattern(String pattern, TbMsgMetaData metaData) {
        String result = new String(pattern);
        for (Map.Entry<String,String> keyVal  : metaData.values().entrySet()) {
            result = processVar(result, keyVal.getKey(), keyVal.getValue());
        }
        return result;
    }

    private static String processVar(String pattern, String key, String val) {
        String varPattern = String.format(VARIABLE_TEMPLATE, key);
        return pattern.replace(varPattern, val);
    }

}
