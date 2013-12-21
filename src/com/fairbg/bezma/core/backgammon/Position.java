package com.fairbg.bezma.core.backgammon;

import java.util.Random;

import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;

/** Describes checkers positions and cube state */
public class Position implements Cloneable
{
	/** Describes direction of checkers movement for bottom player */
	public enum Direction {
		/** Board direction is not defined */
		None,
		/** Board for white player (player in the bottom place), movements counter clockwise */
		WhiteCCW,
		/** Board for white player, movements clockwise */
		WhiteCW,
		/** Board for black player, movements counter clockwise */
		BlackCCW,
		/** Board for black player, movements clockwise */
		BlackCW
	}

	public enum CubePosition {
		/** No cube on board */
		None,
		/** Cube in the middle position */
		Center,
		/** Cube is proposed to the white player and placed into the left part of the board */
		Left,
		/** Cube is proposed to the black player and placed into the right part of the board */
		Right,
		/** White player owns the cube in the bottom of the board */
		White,
		/** Black player owns cube on the top of the board */
		Black
	}

	public static final int MAX_CHECKERS_IN_POSITION = 5;

	/** Bar index. Used for movement search. Checkers moved from less index to lager */
	public static final int BAR_POSITION			 = 25;

	/**
	 * Position of checkers on the board
	 * @details 1-24 - Checker's positions ( >0 - white, <0 - black, 0 - empty), 0 - white bar, 25 - black bar, 26,27 -
	 *          off position for black and white. White checkers moved from 1 to 24, black - from 24 to 1
	 */
	private int[]		   m_Checkers			   = new int[28];

	/** Current cube value. Possible values 1, 2, 4, 8, 16, 32, 64 */
	private int			 m_CubeValue			  = 1;

	/** Current cube position */
	private CubePosition	m_CubePosition		   = CubePosition.Center;

	/**
	 * Get normalized checker position (move direction from 1 to 24)
	 * @param position current checker position according to player
	 * @param direction move direction
	 * @return checker index in m_Checkers
	 */
	private int getIndex(int position, PlayerColors direction)
	{
		if (position <= 0)
		{
			return direction == PlayerColors.BLACK ? 26 : 27;
		} else
		{
			return direction == PlayerColors.BLACK ? position : BAR_POSITION - position;
		}
	}

	private int getPLayerSign(PlayerColors player)
	{
		return player == PlayerColors.WHITE ? 1 : player == PlayerColors.BLACK ? -1 : 0;
	}

	/**
	 * Возвращает массив содержащий положение и цвет фишек на доске если значение >0, то количество белых, если <0, то
	 * количество черных позиция с 1 по 24 - количество фишек на игровых полях позиция 25 - бар белых фишек позиция 0 -
	 * бар черных фишек 26 - зона выброса белых фишек 27 - зона выброса черных фишек
	 * */
	public int[] getCheckers()
	{
		return m_Checkers;
	}

	public void setCheckers(int[] checkers)
	{
		if (checkers.length != m_Checkers.length)
		{
			BezmaLog.e("SET CHECKERS", "Invalide size of incomming checkers array");
			Thread.currentThread().interrupt();
		}
		for (int i = 0; i < checkers.length; ++i)
		{
			m_Checkers[i] = checkers[i];
		}

	}

	/** Get current cube value */
	public int getCubeValue()
	{
		return m_CubeValue;
	}

	/** Get current cube position on board */
	public CubePosition getCubePosition()
	{
		return m_CubePosition;
	}

	/** Set current cube position on board */
	public void setCubePosition(CubePosition value)
	{
		m_CubePosition = value;
	}

	@Override
	public String toString()
	{
		final char BLACK = 'X';
		final char WHITE = '0';
		final char SPACE = '-';
		StringBuilder res = new StringBuilder();
		res.append('\n');
		for (int i = 0; i < 5; ++i)
		{
			res.append("|");
			for (int j = 13; j <= 18; ++j)
			{
				if (Math.abs(m_Checkers[j]) > i)
				{
					res.append(m_Checkers[j] > 0 ? WHITE : BLACK);
				} else
				{
					res.append(SPACE);
				}
			}
			res.append("|");
			for (int j = 19; j <= 24; ++j)
			{
				if (Math.abs(m_Checkers[j]) > i)
				{
					res.append(m_Checkers[j] > 0 ? WHITE : BLACK);
				} else
				{
					res.append(SPACE);
				}
			}
			res.append("|\n");
		}

		for (int i = 4; i >= 0; --i)
		{
			res.append("|");
			for (int j = 12; j >= 7; --j)
			{
				if (Math.abs(m_Checkers[j]) > i)
				{
					res.append(m_Checkers[j] > 0 ? WHITE : BLACK);
				} else
				{
					res.append(SPACE);
				}
			}
			res.append("|");
			for (int j = 6; j >= 1; --j)
			{
				if (Math.abs(m_Checkers[j]) > i)
				{
					res.append(m_Checkers[j] > 0 ? WHITE : BLACK);
				} else
				{
					res.append(SPACE);
				}
			}
			res.append("|\n");
		}
		res.append("Bar : ");

		int count = m_Checkers[0];
		for (int i = 0; i < Math.abs(count); i++)
		{
			res.append(count > 0 ? WHITE : BLACK);
		}

		res.append(" : ");
		count = m_Checkers[25];

		for (int i = 0; i < Math.abs(count); i++)
		{
			res.append(count > 0 ? WHITE : BLACK);
		}

		/* for (int i = 0; i < m_Checkers.length; i++) { res.append(m_Checkers[i]); res.append(','); } */
		return res.toString();
	}

	/** Get random checkers position */
	public static Position getRandomPosition()
	{
		Position pos = new Position();
		for (int i = 0; i < pos.m_Checkers.length; i++)
		{
			pos.m_Checkers[i] = 0;
		}
		Random rnd = new Random();
		int checkers_left = 15;
		while (checkers_left > 0)
		{ // расставляем белые фишки
			int p = rnd.nextInt(28);
			if (p != 27 && p != 25 && pos.m_Checkers[p] >= 0)
			{
				pos.m_Checkers[p]++;
				checkers_left--;
			}
		}
		checkers_left = 15;
		while (checkers_left > 0)
		{ // расставляем черные фишки
			int p = rnd.nextInt(28);
			if (p != 26 && p != 24 && pos.m_Checkers[p] <= 0)
			{
				pos.m_Checkers[p]--;
				checkers_left--;
			}
		}
		pos.m_CubeValue = (new int[] { 2, 4, 8, 16, 32, 64 })[rnd.nextInt(6)];
		pos.m_CubePosition = (new CubePosition[] { CubePosition.None, CubePosition.Center, CubePosition.Left,
				CubePosition.Right, CubePosition.Black, CubePosition.White })[rnd.nextInt(6)];
		return pos;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Position clone = new Position();
		for (int i = 0; i < this.m_Checkers.length; ++i)
		{
			clone.m_Checkers[i] = this.m_Checkers[i];
		}
		clone.m_CubePosition = this.m_CubePosition;
		clone.m_CubeValue = this.m_CubeValue;
		return clone;
	}

	/** Move checkers on the board according to the all movements in the move */
	public void applyMove(Move move)
	{
		for (Movement movement : move.getMovements())
		{
			applayMovement(movement.MoveFrom, movement.MoveTo, movement.Color);
		}
	}

	/** Move checkers on the board according to the movement */
	public void applayMovement(int moveFrom, int moveTo, PlayerColors player)
	{
		moveFrom = getIndex(moveFrom, player);
		moveTo = getIndex(moveTo, player);
		final int OPP_BAR_POSITION = getIndex(Position.BAR_POSITION, PlayerColors.getAltColor(player));

		final int ONE_CHECKER = getPLayerSign(player);

		m_Checkers[moveFrom] -= ONE_CHECKER; // убираем одну фишку с позиции

		if (moveTo < Position.BAR_POSITION)
		{
			if ((m_Checkers[moveTo] == 0) || // если пустая позиция
					ONE_CHECKER * m_Checkers[moveTo] > 0) // или там стоят фишки
			// того же цвета
			{ // там стояли свои фишки
				m_Checkers[moveTo] += ONE_CHECKER;
			} else
			{
				if (ONE_CHECKER * m_Checkers[moveTo] == -1) // там стояла одна
				// фишка противника
				{
					m_Checkers[moveTo] = ONE_CHECKER;
					m_Checkers[OPP_BAR_POSITION] += -ONE_CHECKER;
				}
			}
		}
	}

	/** Calculate current PIPs for player */
	public int getPips(PlayerColors forPlayer)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/** Return the color of the checker on the specified position */
	public PlayerColors getCheckerColor(int position, PlayerColors player)
	{
		final int index = getIndex(position, player);
		int color = m_Checkers[index] * getPLayerSign(PlayerColors.WHITE);

		return color == 0 ? PlayerColors.NONE : color > 0 ? PlayerColors.WHITE : PlayerColors.BLACK;
	}

	/** Return true if all checkers has the same position */
	public boolean absoluteEquals(Position other)
	{
		for (int i = 0; i <= 25; i++)
		{
			if (this.m_Checkers[i] != other.m_Checkers[i])
			{
				return false;
			}
		}
		return true;
	}

	public boolean approxEquals(Position other)
	{
		boolean equals = true;

		for (int i = 0; equals && i <= 25; i++)
		{
			int count = getCheckerCount(i, PlayerColors.BLACK);
			int other_count = other.getCheckerCount(i, PlayerColors.BLACK);
			PlayerColors color = getCheckerColor(i, PlayerColors.BLACK);
			PlayerColors other_color = other.getCheckerColor(i, PlayerColors.BLACK);

			equals = equals && (other_color == color || other_color == PlayerColors.NONE || color == PlayerColors.NONE);

			equals = equals && (count == other_count)
					|| (count >= other_count && (other_count == MAX_CHECKERS_IN_POSITION));
		}

		return equals;
	}

	public int getCheckerCount(int position, PlayerColors player)
	{
		return Math.abs(m_Checkers[getIndex(position, player)]);
	}

	public boolean hasCheckerInBar(PlayerColors player)
	{
		return m_Checkers[getIndex(BAR_POSITION, player)] != 0;
	}

	public void ChangeDirection(Direction currentDirection, Direction destDirection)
	{
		if (currentDirection == Direction.None)
		{
			return;
		}

		if (currentDirection != destDirection)
		{
			m_Checkers = PositionUtils.ChangeDirection(currentDirection, destDirection, m_Checkers);
		}
	}
}
