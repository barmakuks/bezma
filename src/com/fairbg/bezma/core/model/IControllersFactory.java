package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.MatchParameters;

public interface IControllersFactory
{
    public IMatchController createMatchController(MatchParameters matchParameters, IModelEventNotifier modelNotifier);
    public IGameController  createGameController(IMatchController matchController);
}
