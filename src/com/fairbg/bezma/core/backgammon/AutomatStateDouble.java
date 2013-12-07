package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateDouble implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {
        if (!gameAutomat.isCurrentPlayer(command.player))
        {
            return false;
        }

        if (gameAutomat.isDoubleAccepted(command.getPosition()))
        {
            gameAutomat.acceptDouble();
            
            gameAutomat.setAutomatState(AutomatStates.MOVE);
            
            return true;
        }

        return false;
    }

}
