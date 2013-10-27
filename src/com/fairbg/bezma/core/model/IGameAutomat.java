package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Position;

public interface IGameAutomat
{
    boolean processCommand(IGameBox gameBox, ModelCommand modelCommand);
    Position getCurrentPosition();
}
