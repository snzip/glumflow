package org.thingsboard.rule.engine.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.thingsboard.server.common.msg.TbMsg;

import javax.script.ScriptException;
import java.util.Set;

public interface ScriptEngine {

    TbMsg executeUpdate(TbMsg msg) throws ScriptException;

    TbMsg executeGenerate(TbMsg prevMsg) throws ScriptException;

    boolean executeFilter(TbMsg msg) throws ScriptException;

    Set<String> executeSwitch(TbMsg msg) throws ScriptException;

    JsonNode executeJson(TbMsg msg) throws ScriptException;

    String executeToString(TbMsg msg) throws ScriptException;

    void destroy();

}
