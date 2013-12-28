package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IGameController;
import com.fairbg.bezma.core.model.ModelCommand;

public interface IGameAutomat
{
    boolean processCommand(IGameController gameBox, ModelCommand modelCommand);
    Position getCurrentPosition();
}
