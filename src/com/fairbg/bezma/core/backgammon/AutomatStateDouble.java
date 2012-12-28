package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateDouble implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameBox, ModelCommand command)
    {
        if (!gameBox.isCurrentPlayer(command.player))
        {
            return false;
        }

        if (gameBox.isDoubleAccepted(command.getPosition()))
        {
            gameBox.acceptDouble();
            
            gameBox.setAutomatState(AutomatStates.MOVE);
            
            return true;
        }

        return false;
    }

}
