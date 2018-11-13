package org.thingsboard.server.actors.service;

import akka.japi.Creator;
import org.thingsboard.server.actors.ActorSystemContext;

public abstract class ContextBasedCreator<T> implements Creator<T> {

    private static final long serialVersionUID = 1L;

    protected final transient ActorSystemContext context;

    public ContextBasedCreator(ActorSystemContext context) {
        super();
        this.context = context;
    }
}
