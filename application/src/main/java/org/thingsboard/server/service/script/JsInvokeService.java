package org.thingsboard.server.service.script;

import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.server.common.data.id.EntityId;

import java.util.UUID;

public interface JsInvokeService {

    ListenableFuture<UUID> eval(JsScriptType scriptType, String scriptBody, String... argNames);

    ListenableFuture<Object> invokeFunction(UUID scriptId, Object... args);

    ListenableFuture<Void> release(UUID scriptId);

}
