package org.thingsboard.rule.engine.filter;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.thingsboard.rule.engine.api.*;
import org.thingsboard.server.common.data.id.RuleChainId;
import org.thingsboard.server.common.data.id.RuleNodeId;
import org.thingsboard.server.common.msg.TbMsg;
import org.thingsboard.server.common.msg.TbMsgMetaData;

import javax.script.ScriptException;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TbJsFilterNodeTest {

    private TbJsFilterNode node;

    @Mock
    private TbContext ctx;
    @Mock
    private ListeningExecutor executor;
    @Mock
    private ScriptEngine scriptEngine;

    private RuleChainId ruleChainId = new RuleChainId(UUIDs.timeBased());
    private RuleNodeId ruleNodeId = new RuleNodeId(UUIDs.timeBased());

    @Test
    public void falseEvaluationDoNotSendMsg() throws TbNodeException, ScriptException {
        initWithScript();
        TbMsg msg = new TbMsg(UUIDs.timeBased(), "USER", null, new TbMsgMetaData(), "{}", ruleChainId, ruleNodeId, 0L);
        mockJsExecutor();
        when(scriptEngine.executeFilter(msg)).thenReturn(false);

        node.onMsg(ctx, msg);
        verify(ctx).getJsExecutor();
        verify(ctx).tellNext(msg, "False");
    }

    @Test
    public void exceptionInJsThrowsException() throws TbNodeException, ScriptException {
        initWithScript();
        TbMsgMetaData metaData = new TbMsgMetaData();
        TbMsg msg = new TbMsg(UUIDs.timeBased(), "USER", null, metaData, "{}", ruleChainId, ruleNodeId, 0L);
        mockJsExecutor();
        when(scriptEngine.executeFilter(msg)).thenThrow(new ScriptException("error"));


        node.onMsg(ctx, msg);
        verifyError(msg, "error", ScriptException.class);
    }

    @Test
    public void metadataConditionCanBeTrue() throws TbNodeException, ScriptException {
        initWithScript();
        TbMsgMetaData metaData = new TbMsgMetaData();
        TbMsg msg = new TbMsg(UUIDs.timeBased(), "USER", null, metaData, "{}", ruleChainId, ruleNodeId, 0L);
        mockJsExecutor();
        when(scriptEngine.executeFilter(msg)).thenReturn(true);

        node.onMsg(ctx, msg);
        verify(ctx).getJsExecutor();
        verify(ctx).tellNext(msg, "True");
    }

    private void initWithScript() throws TbNodeException {
        TbJsFilterNodeConfiguration config = new TbJsFilterNodeConfiguration();
        config.setJsScript("scr");
        ObjectMapper mapper = new ObjectMapper();
        TbNodeConfiguration nodeConfiguration = new TbNodeConfiguration(mapper.valueToTree(config));

        when(ctx.createJsScriptEngine("scr")).thenReturn(scriptEngine);

        node = new TbJsFilterNode();
        node.init(ctx, nodeConfiguration);
    }

    private void mockJsExecutor() {
        when(ctx.getJsExecutor()).thenReturn(executor);
        doAnswer((Answer<ListenableFuture<Boolean>>) invocationOnMock -> {
            try {
                Callable task = (Callable) (invocationOnMock.getArguments())[0];
                return Futures.immediateFuture((Boolean) task.call());
            } catch (Throwable th) {
                return Futures.immediateFailedFuture(th);
            }
        }).when(executor).executeAsync(Matchers.any(Callable.class));
    }

    private void verifyError(TbMsg msg, String message, Class expectedClass) {
        ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
        verify(ctx).tellFailure(same(msg), captor.capture());

        Throwable value = captor.getValue();
        assertEquals(expectedClass, value.getClass());
        assertEquals(message, value.getMessage());
    }
}