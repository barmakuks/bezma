package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.ModelCommand;

public class AutomatStateMove implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameBox, ModelCommand command)
    {

        Position position = command.getPosition();
        
        if (gameBox.canDouble(position)) // Посмотреть, изменилось ли положение кубика стоимости
        {
            gameBox.proposeDouble();
            gameBox.setAutomatState(AutomatStates.DOUBLE);
            
            return true;
        }        
        else // Кубик стоимости не переместился 
        {
            if (gameBox.findAndAcceptMove(position))
            {
                if (gameBox.isGameFinished())
                {
                    gameBox.finishGame();
                    
                    if (gameBox.isMatchFinished())
                    {
                        gameBox.finishMatch();
                        gameBox.setAutomatState(AutomatStates.END);
                    }
                    else
                    {
                        gameBox.nextGame();
                        gameBox.setAutomatState(AutomatStates.START);
                    }
                }
                else
                {
                    gameBox.setAutomatState(AutomatStates.MOVE);                    
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
