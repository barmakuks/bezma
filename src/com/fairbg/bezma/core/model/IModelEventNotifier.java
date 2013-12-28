package com.fairbg.bezma.core.model;

public interface IModelEventNotifier
{
    void notifyAll(IModelObserver.Event event);
}
