package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateStart implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameBox, ModelCommand command)
    {
        // Check if this is start position
        if (gameBox.isStartPosition(command.getPosition()))
        {
            gameBox.nextGame();
            // Change current state to MOVE
            gameBox.setAutomatState(AutomatStates.MOVE);
        }
        return false;
    }

}
