package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateMove implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {

        Position position = command.getPosition();
        
        if (gameAutomat.canDouble(position)) // Cube position is changed?
        {
            gameAutomat.proposeDouble();
            gameAutomat.setAutomatState(AutomatStates.DOUBLE);
            
            return true;
        }        
        else // Cube position is not changed
        {
            if (gameAutomat.findAndAcceptMove(position))
            {
                if (gameAutomat.isGameFinished())
                {
                    gameAutomat.finishGame();
                    
                    if (gameAutomat.isMatchFinished())
                    {
                        gameAutomat.finishMatch();
                        gameAutomat.setAutomatState(AutomatStates.END);
                    }
                    else
                    {
                        gameAutomat.nextGame();
                        gameAutomat.setAutomatState(AutomatStates.START);
                    }
                }
                else
                {
                    gameAutomat.setAutomatState(AutomatStates.MOVE);                    
                }                
            }
            
            /*Move move = gameBox.findMove(command.getPosition(), command.player);

            if (move != null)
            {
                // Change current state to MOVE
                gameBox.appendMove(move);

                IAutomatState state = gameBox.selectAutomatState(AutomatStates.MOVE);
                gameBox.setCurrentAutomatState(state);

                currentPosition.applyMove(move);

                ModelSituation modelState = new ModelSituation(currentPosition, "");
                gameBox.setModelState(modelState);

                return true;
            }*/
            
        }
        return false;
    }

}
