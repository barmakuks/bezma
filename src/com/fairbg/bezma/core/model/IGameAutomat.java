package com.fairbg.bezma.core.model;

public interface IGameAutomat
{
    boolean processCommand(IGameBox gameBox, ModelCommand modelCommand);
}
