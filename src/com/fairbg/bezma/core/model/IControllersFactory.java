package com.fairbg.bezma.core.model;

public interface IControllersFactory
{
    public IMatchController createMatchController(IModelEventNotifier modelNotifier);
    public IGameController  createGameController(IMatchController matchController);
}
