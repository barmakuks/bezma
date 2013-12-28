package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IControllersFactory;
import com.fairbg.bezma.core.model.IGameController;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.IModelEventNotifier;

public class BgControllersFactory implements IControllersFactory
{
    public IMatchController createMatchController(IModelEventNotifier aModelNotifier)
    {
        return new BgMatchController(aModelNotifier);
    }

    public IGameController  createGameController(IMatchController matchController)
    {
        return new BgGameController(matchController);
    }
}
