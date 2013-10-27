package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.backgammon.IAutomatState.AutomatStates;
import com.fairbg.bezma.core.model.IGameAutomat;
import com.fairbg.bezma.core.model.IGameBox;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;

public class BackgammonAutomat implements IBackgammonAutomat, IGameAutomat
{
    private BackgammonRules m_Rules = new BackgammonRules();
    private IGameBox m_GameBox;
    
    private Position m_LastPosition;
    private PlayerColors m_CurrentPlayer;
    
    IAutomatState m_CurrentState;

    public BackgammonAutomat(IGameBox gameBox)
    {
        m_GameBox = gameBox;
        setAutomatState(AutomatStates.START);
    }
    
    @Override
    public Position.Direction getStartPositionDirection(Position position)
    {
        return m_Rules.getStartPositionDirection(position);
    }

    @Override
    public void nextGame()
    {
        m_GameBox.nextGame();
    }

    @Override
    public void startGame(Position.Direction direction)
    {
	BezmaLog.i("BEZMA", "startGame");
        m_GameBox.startGame(direction);
        m_LastPosition = new Position();
        m_LastPosition.setCheckers(BackgammonRules.getStartPosition(direction));
    }
    
    @Override
    public boolean canDouble(Position position)
    {
	return false;
        // TODO Implement. Currently implemented simple method
//        return m_CurrentPlayer != PlayerColors.NONE;
    }

    @Override
    public void proposeDouble()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDoubleAccepted(Position position)
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void acceptDouble()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isGameFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void finishGame()
    {
        // TODO Auto-generated method stub
        m_GameBox.finishGame();
    }

    @Override
    public boolean isMatchFinished()
    {
        // TODO Auto-generated method stub
        return m_GameBox.isMatchFinished();
    }

    @Override
    public void finishMatch()
    {
        m_GameBox.finishMatch();
    }

    @Override
    public boolean findAndAcceptMove(Position position)
    {
	BezmaLog.i("BEZMA", "findAndAcceptMove");
        int die1 = 0;
        int die2 = 0;

        Move move = null;
        if (m_CurrentPlayer == null || m_CurrentPlayer == PlayerColors.NONE)
        {
            m_CurrentPlayer = PlayerColors.BLACK;
            
            move = m_Rules.findMove(die1, die2, m_LastPosition, position, m_CurrentPlayer);

            if (move == null)
            {
        	m_CurrentPlayer = PlayerColors.WHITE;
                move = m_Rules.findMove(die1, die2, m_LastPosition, position, PlayerColors.WHITE);        	
            }
            
            if (move == null)
            {
        	m_CurrentPlayer = PlayerColors.NONE;        	
            }
        }
        else
        {
            move = m_Rules.findMove(die1, die2, m_LastPosition, position, m_CurrentPlayer);
        }
        

        if (move != null)
        {
            BezmaLog.i("BEZMA", "found move");
            m_LastPosition.applyMove(move);
            m_GameBox.appendMove(move);
            BezmaLog.i("BEZMA", "move accepted");            
            return true;            
        }
        else
        {
            BezmaLog.i("BEZMA", "move not found");            
        }
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IAutomatState getAutomatState()
    {
        return m_CurrentState;
    }

    @Override
    public boolean setAutomatState(AutomatStates state)
    {
	BezmaLog.i("BEZMA", "set Automat State: " + state.toString());
	switch(state)
	{
	case DOUBLE:
	    m_CurrentState = new AutomatStateDouble();
	    break;
	case MOVE:
	    m_CurrentState = new AutomatStateMove();
	    break;
	case START:
	    m_CurrentState = new AutomatStateStart();
	    break;
	case END:
	    m_CurrentState = new AutomatStateEnd();
	    break;
	}

	return true;
    }

    @Override
    public boolean isCurrentPlayer(PlayerColors player)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean processCommand(IGameBox gameBox, ModelCommand modelCommand)
    {
	boolean res = false;

	if (m_CurrentState != null)
	{
	    BezmaLog.i("BEZMA", "processCommand in BackgammonAutomat" + modelCommand.getPosition().toString());
	    res = m_CurrentState.processCommand(this, modelCommand);
	}
	
        return res;
    }

    @Override
    public Position getCurrentPosition()
    {
	return m_LastPosition;
    }

}
