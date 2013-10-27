package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateStart implements IAutomatState
{
    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {
        // Check if this is start position
	Position.Direction direction = gameAutomat.getStartPositionDirection(command.getPosition()); 

	if (direction != Position.Direction.None)
        {
            gameAutomat.startGame(direction);
            // Change current state to MOVE
            gameAutomat.setAutomatState(AutomatStates.MOVE);
            
            return true;
        }
	
	return false;
    }
}
