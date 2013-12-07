package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public interface IAutomatState
{
    public static enum AutomatStates
    {
        START, MOVE, DOUBLE, END
    }

    boolean processCommand(IBackgammonAutomat gameBox, ModelCommand command);
}
