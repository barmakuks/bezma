package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.backgammon.IAutomatState.AutomatStates;
import com.fairbg.bezma.core.backgammon.Position.CubePosition;
import com.fairbg.bezma.core.model.IGameAutomat;
import com.fairbg.bezma.core.model.IGameBox;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;

public class BackgammonAutomat implements IBackgammonAutomat, IGameAutomat
{
	private Position.Direction m_StartPositionDirection = Position.Direction.None;
	private Position.Direction DefaultDirection		 = Position.Direction.BlackCW;

	private BackgammonRules	  m_Rules  = new BackgammonRules();
	private IGameBox		  m_GameBox;

	private Position		  m_LastPosition;
	private PlayerColors	  m_CurrentPlayer;
	
	private CubeAutomat		  m_CubeAutomat;

	IAutomatState			  m_CurrentState;

	public BackgammonAutomat(IGameBox gameBox)
	{
		m_GameBox = gameBox;
		m_CubeAutomat = new CubeAutomat(this);

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
		m_StartPositionDirection = direction;
		m_GameBox.startGame(DefaultDirection);
		m_LastPosition = new Position();
		m_LastPosition.setCheckers(BackgammonRules.getStartPosition(DefaultDirection));
	}

	boolean canDouble(Position position)
	{
		return false;
	}

	void proposeDouble()
	{
	    MoveAbstract move = new MoveCubeDouble();
		m_GameBox.appendMove(move);
	}

	boolean isDoubleAccepted(Position position)
	{
		// TODO Auto-generated method stub
		return true;
	}

	void acceptDouble()
	{
        MoveAbstract move = new MoveCubeTake();
        m_GameBox.appendMove(move);
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
	public boolean processCube(Position position)
	{
		if (m_LastPosition.getCubePosition() != position.getCubePosition())
		{
		    return m_CubeAutomat.processNextState(position.getCubePosition());
		}

		return false;
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
			m_CurrentPlayer = PlayerColors.getAltColor(m_CurrentPlayer);
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
		switch (state)
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
			ModelCommand normalizedCommand = NormalizePosition(modelCommand);
			BezmaLog.i("BEZMA", "processCommand in BackgammonAutomat" + modelCommand.getPosition().toString());
			res = m_CurrentState.processCommand(this, normalizedCommand);
		}

		return res;
	}

	/** Returns Model Command's position in Black Counterclockwise direction */
	private ModelCommand NormalizePosition(ModelCommand modelCommand)
	{
		ModelCommand normalizedCommand = modelCommand;

		try
		{
			normalizedCommand = (ModelCommand) modelCommand.clone();
			normalizedCommand.getPosition().ChangeDirection(m_StartPositionDirection, DefaultDirection);
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return normalizedCommand;
	}

	@Override
	public Position getCurrentPosition()
	{
		return m_LastPosition;
	}

    // IGameWithCube functions

	@Override
    public boolean canProposeDouble(PlayerColors player)
    {
	    return m_LastPosition != null && m_LastPosition.getCubePosition() == CubePosition.Center && m_LastPosition.getCubeValue() != 64;
    }

    @Override
    public boolean proposeDouble(PlayerColors player)
    {
        if (player == PlayerColors.BLACK)
        {
            m_LastPosition.setCubePosition(CubePosition.Left);
            return true;
        }
        else if (player == PlayerColors.WHITE)
        {
            m_LastPosition.setCubePosition(CubePosition.Right);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean take(PlayerColors player)
    {
        if (player == PlayerColors.BLACK)
        {
            m_LastPosition.setCubePosition(CubePosition.Black);
            return true;
        }
        else if (player == PlayerColors.WHITE)
        {
            m_LastPosition.setCubePosition(CubePosition.White);
            return true;
        }
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pass(PlayerColors player)
    {
        this.finishGame();

        return true;
    }

    @Override
    public PlayerColors getCurrentPlayer()
    {
        return m_CurrentPlayer;
    }

	
}
