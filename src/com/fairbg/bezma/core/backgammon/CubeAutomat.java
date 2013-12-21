package com.fairbg.bezma.core.backgammon;

import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

import com.fairbg.bezma.core.backgammon.Position.CubePosition;
import com.fairbg.bezma.core.model.PlayerColors;

interface ICubeState
{
	public ICubeState getNextState(CubePosition position);
}

interface IGameWithCube
{
	public boolean canProposeDouble(PlayerColors player);

	public boolean proposeDouble(PlayerColors player);

	public boolean take(PlayerColors player);

	public boolean pass(PlayerColors player);

	public PlayerColors getCurrentPlayer();
}

interface ICubeAutomat extends IGameWithCube
{
	public boolean processNextState(CubePosition position);
}

class BlackCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	BlackCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;

		switch (position)
		{
		case Center:
		{
			if (m_automat.get().proposeDouble(PlayerColors.BLACK) && m_automat.get().pass(PlayerColors.WHITE))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case White:
		{
			if (m_automat.get().proposeDouble(PlayerColors.BLACK) && m_automat.get().take(PlayerColors.WHITE))
			{
				nextState = new WhiteCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (m_automat.get().proposeDouble(PlayerColors.BLACK))
			{
				nextState = new NoCubeState(m_automat);
			}
			break;
		}
		default:
			break;
		}

		return nextState;
	}
}

class WhiteCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	WhiteCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;

		switch (position)
		{
		case Black:
		{
			if (m_automat.get().proposeDouble(PlayerColors.WHITE) && m_automat.get().take(PlayerColors.BLACK))
			{
				nextState = new BlackCubeState(m_automat);
			}
			break;
		}
		case Center:
		{
			if (m_automat.get().proposeDouble(PlayerColors.WHITE) && m_automat.get().pass(PlayerColors.BLACK))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (m_automat.get().proposeDouble(PlayerColors.WHITE))
			{
				nextState = new NoCubeState(m_automat);
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

		switch (position)
		{
		case Black:
		{
			if (m_automat.get().proposeDouble(PlayerColors.WHITE) && m_automat.get().take(PlayerColors.BLACK))
			{
				nextState = new BlackCubeState(m_automat);
			}
			break;
		}
		case White:
		{
			if (m_automat.get().proposeDouble(PlayerColors.BLACK) && m_automat.get().pass(PlayerColors.WHITE))
			{
				nextState = new WhiteCubeState(m_automat);
			}
			break;
		}
		case None:
		{
			if (m_automat.get().proposeDouble(m_automat.get().getCurrentPlayer()))
			{
				nextState = new NoCubeState(m_automat);
			}
			break;
		}
		default:
			break;
		}

		return nextState;
	}
}

class NoCubeState implements ICubeState
{
	private WeakReference<IGameWithCube> m_automat;

	NoCubeState(WeakReference<IGameWithCube> automat)
	{
		m_automat = automat;
	}

	@Override
	public ICubeState getNextState(CubePosition position)
	{
		ICubeState nextState = null;

		switch (position)
		{
		case Black:
		{
			if (m_automat.get().take(PlayerColors.BLACK))
			{
				nextState = new BlackCubeState(m_automat);
			}
			break;
		}
		case Center:
		{
			if (m_automat.get().pass(m_automat.get().getCurrentPlayer()))
			{
				nextState = new MiddleCubeState(m_automat);
			}
			break;
		}
		case White:
		{
			if (m_automat.get().take(PlayerColors.WHITE))
			{
				nextState = new WhiteCubeState(m_automat);
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
	public PlayerColors getCurrentPlayer()
	{
		return m_game.getCurrentPlayer();
	}

	@Override
	public boolean proposeDouble(PlayerColors player)
	{
		return m_game.proposeDouble(player);
	}

	@Override
	public boolean pass(PlayerColors player)
	{
		return m_game.pass(player);
	}

	@Override
	public boolean take(PlayerColors player)
	{
		return m_game.take(player);
	}

	@Override
	public boolean canProposeDouble(PlayerColors player)
	{
		return m_game.canProposeDouble(player);
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
