package com.fairbg.bezma.core.backgammon;

import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

import com.fairbg.bezma.core.backgammon.Position.CubePosition;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;

interface ICubeState
{
	public ICubeState getNextState(CubePosition position);
}

interface IGameWithCube
{
	public boolean canProposeDouble(PlayerId player);

	public boolean proposeDouble(PlayerId player);

	public boolean take(PlayerId player);

	public boolean pass(PlayerId player);

	public PlayerId getCurrentPlayer();
	
	public int getCubeValue();
	
    public PlayerId getSouthPlayer();

    public PlayerId getNorthPlayer();

    public void playMove(MoveAbstract move);
}

interface ICubeAutomat extends IGameWithCube
{
	public boolean processNextState(CubePosition position);
}

class SouthCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	SouthCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;
		final PlayerId southPlayer = m_automat.get().getSouthPlayer();
		final PlayerId northPlayer = m_automat.get().getNorthPlayer();
		IGameWithCube automat = m_automat.get();

		switch (position)
		{
		case Center:
		{		    
			if (automat.proposeDouble(southPlayer) && automat.pass(northPlayer))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case North:
		{
			if (automat.proposeDouble(southPlayer) && automat.take(northPlayer))
			{
				nextState = new NorthCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (automat.proposeDouble(southPlayer))
			{
				nextState = new ProposeCubeState(m_automat);
			}
			break;
		}
		default:
			break;
		}

		return nextState;
	}
}

class NorthCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	NorthCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;
		final PlayerId southPlayer = m_automat.get().getSouthPlayer();
	    final PlayerId northPlayer = m_automat.get().getNorthPlayer();
	    IGameWithCube automat = m_automat.get();

		switch (position)
		{
		case South:
		{
			if (automat.proposeDouble(northPlayer) && automat.take(southPlayer))
			{
				nextState = new SouthCubeState(m_automat);
			}
			break;
		}
		case Center:
		{
			if (automat.proposeDouble(northPlayer) && automat.pass(southPlayer))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (automat.proposeDouble(northPlayer))
			{
				nextState = new ProposeCubeState(m_automat);
			}
			break;
		}
		default:
			break;
		}

		return nextState;
	}
}

class MiddleCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	MiddleCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;
		final PlayerId southPlayer = m_automat.get().getSouthPlayer();
		final PlayerId northPlayer = m_automat.get().getNorthPlayer();
		IGameWithCube automat = m_automat.get();

		switch (position)
		{
		case North:
		{
			if (automat.proposeDouble(southPlayer) && automat.take(northPlayer))
			{
				nextState = new NorthCubeState(m_automat);
			}
			break;
		}
		case South:
		{
			if (automat.proposeDouble(northPlayer) && automat.take(southPlayer))
			{
				nextState = new SouthCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (automat.proposeDouble(automat.getCurrentPlayer()))
			{
				nextState = new ProposeCubeState(m_automat);
			}
			break;
		}
		default:
			break;
		}

		return nextState;
	}
}

class ProposeCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	ProposeCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;
		final PlayerId southPlayer = m_automat.get().getSouthPlayer();
	    final PlayerId northPlayer = m_automat.get().getNorthPlayer();
	    IGameWithCube automat = m_automat.get();

		switch (position)
		{
		case North:
		{
			if (automat.take(northPlayer))
			{
				nextState = new NorthCubeState(m_automat);
			}
			break;
		}
		case Center:
		{
			if (automat.pass(automat.getCurrentPlayer()))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case South:
		{
			if (automat.take(southPlayer))
			{
				nextState = new SouthCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			break;
		}
        default:
            throw new NoSuchElementException();
		}

		return nextState;
	}
}

public class CubeAutomat implements ICubeAutomat
{
	private ICubeState	m_currentState;
	private IGameWithCube m_game;

	CubeAutomat(IGameWithCube game)
	{
		m_game = game;
		m_currentState = new MiddleCubeState(new WeakReference<IGameWithCube>(this));
	}

	@Override
	public boolean processNextState(CubePosition position)
	{
		if (m_currentState != null)
		{
			ICubeState nextState = m_currentState.getNextState(position);

			if (nextState != null)
			{
				m_currentState = nextState;
				return true;
			}
		}
		return false;
	}

	@Override
	public PlayerId getCurrentPlayer()
	{
		return m_game.getCurrentPlayer();
	}

	@Override
	public boolean proposeDouble(PlayerId player)
	{
		return m_game.proposeDouble(player);
	}

	@Override
	public boolean pass(PlayerId player)
	{
		return m_game.pass(player);
	}

	@Override
	public boolean take(PlayerId player)
	{
		return m_game.take(player);
	}

	@Override
	public boolean canProposeDouble(PlayerId player)
	{
		return m_game.canProposeDouble(player);
	}
	
	@Override
	public int getCubeValue()
	{	    
	    return m_game.getCubeValue();
	}

    @Override
    public PlayerId getNorthPlayer()
    {
        return m_game.getNorthPlayer();
    }

    @Override
    public PlayerId getSouthPlayer()
    {
        return m_game.getSouthPlayer();
    }

    class CubePlayer implements IMoveVisitor
    {
        private WeakReference<IGameWithCube> m_automat;

        CubePlayer(IGameWithCube automat)
        {
            m_automat = new WeakReference<IGameWithCube>(automat);
        }
        
        @Override
        public void visit(Movement movement)
        {            
        }

        @Override
        public void visit(Move move)
        {
        }

        @Override
        public void visit(MoveCubeDouble move)
        {
            m_currentState = new ProposeCubeState(m_automat);
        }

        @Override
        public void visit(MoveCubeTake move)
        {
            if (m_game.getSouthPlayer() == move.getPlayer())
            {
                m_currentState = new SouthCubeState(m_automat);
            }
            else
            {
                m_currentState = new NorthCubeState(m_automat);
            }
        }

        @Override
        public void visit(MoveCubePass move)
        {
            m_currentState = new MiddleCubeState(m_automat);
        }

        @Override
        public void visit(MoveFinishGame move)
        {
        }

        @Override
        public void visit(MoveStartGame move)
        {
        }
    }
    
    @Override
    public void playMove(MoveAbstract move)
    {
        if (m_movePlayer == null)
        {
            m_movePlayer = new CubePlayer(this);            
        }
        if (move != null)
        {
            move.accept(m_movePlayer);
        }
    }
    
    private IMoveVisitor m_movePlayer = null;
}
