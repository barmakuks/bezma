package com.fairbg.bezma.core.backgammon;

import java.util.Random;

import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;

/** Describes checkers positions and cube state */
public class Position implements Cloneable
{
	/** Describes direction of checkers movement for bottom player */
	public enum Direction {
		/** Board direction is not defined */
		None,
		/** Board for white player (player in the bottom place), movements counter clockwise */
		GrayCCW,
		/** Board for white player, movements clockwise */
		GrayCW,
		/** Board for black player, movements clockwise */
		RedCW,
		/** Board for black player, movements counter clockwise */
		RedCCW
	}

	public enum CubePosition {
		None, //> No cube on board
		Center, //> Cube in the middle position
		West, //>Cube is proposed to the south player and placed into the west part of the board
		East, //> Cube is proposed to the north player and placed into the east part of the board
		South, //> South player owns the cube in the bottom of the board
		North, //> North player owns cube on the top of the board
        Error //> Error position (more then one cube on board)
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
	
	private Direction       m_direction = Direction.None;

	/**
	 * Get normalized checker position (move direction from 1 to 24)
	 * @param position current checker position according to player
	 * @param direction move direction
	 * @return checker index in m_Checkers
	 */
	private int getIndex(int position, PlayerId direction)
	{
		if (position <= 0)
		{
			return direction == PlayerId.RED ? 26 : 27;
		} 
		else
		{
			return direction == PlayerId.RED ? position : BAR_POSITION - position;
		}
	}

	private int getPlayerSign(PlayerId player)
	{
		return player == PlayerId.SILVER ? 1 : player == PlayerId.RED ? -1 : 0;
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

    public void setCubeValue(int cubeValue)
    {
        m_CubeValue = cubeValue;
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
		pos.m_CubePosition = (new CubePosition[] { CubePosition.None, CubePosition.Center, CubePosition.West,
				CubePosition.East, CubePosition.North, CubePosition.South })[rnd.nextInt(6)];
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
		clone.m_direction = this.m_direction;
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
	public void applayMovement(int moveFrom, int moveTo, PlayerId player)
	{
		moveFrom = getIndex(moveFrom, player);
		moveTo = getIndex(moveTo, player);
		final int OPP_BAR_POSITION = getIndex(Position.BAR_POSITION, PlayerId.getOppositeId(player));

		final int ONE_CHECKER = getPlayerSign(player);

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
	public int getPips(PlayerId forPlayer)
	{
		int [] pos = PositionUtils.ChangeDirection(Direction.RedCCW, m_direction, m_Checkers);
	    int pips = 0;

	    for (int i = 1; i < 26; ++i)
	    {
			int index = getIndex(i, forPlayer);

			int color = pos[index] * getPlayerSign(PlayerId.SILVER);

			PlayerId player = color == 0 ? PlayerId.NONE : color > 0 ? PlayerId.SILVER : PlayerId.RED;

	        if (player == forPlayer)
	        {
	            int cnt = Math.abs(pos[index]);
	            pips += i * cnt;
	        }
	    }
		return pips;
	}

	/** Return the color of the checker on the specified position */
	public PlayerId getCheckerColor(int position, PlayerId player)
	{
		final int index = getIndex(position, player);
		int color = m_Checkers[index] * getPlayerSign(PlayerId.SILVER);

		return color == 0 ? PlayerId.NONE : color > 0 ? PlayerId.SILVER : PlayerId.RED;
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
			int count = getCheckerCount(i, PlayerId.RED);
			int other_count = other.getCheckerCount(i, PlayerId.RED);
			PlayerId color = getCheckerColor(i, PlayerId.RED);
			PlayerId other_color = other.getCheckerColor(i, PlayerId.RED);

			equals = equals && (other_color == color || other_color == PlayerId.NONE || color == PlayerId.NONE);

			equals = equals && (count == other_count)
					|| (count >= other_count && (other_count == MAX_CHECKERS_IN_POSITION));
		}

		return equals;
	}

	public int getCheckerCount(int position, PlayerId player)
	{
		return Math.abs(m_Checkers[getIndex(position, player)]);
	}

	public boolean hasCheckerInBar(PlayerId player)
	{
		return m_Checkers[getIndex(BAR_POSITION, player)] != 0;
	}

	public enum LossType{Normal, Gammon, Backgammon};
	public LossType getLossType(PlayerId looser)
	{
	    final PlayerId winner = PlayerId.getOppositeId(looser);
	    int count = 0;
	    for (int i = 0; i <= 25; ++i)
	    {
	        if (getCheckerColor(i, winner) == winner)
	        {
	            return LossType.Normal;
	        }
	        if (getCheckerColor(i, looser) == looser)
	        {
	            count += getCheckerCount(i, looser);
	        }	        
	    }
	    
	    if (count < 15)
	    {
	        return LossType.Normal;
	    }

        for (int i = 19; i <= 25; ++i)
        {
            if (getCheckerColor(i, looser) == looser)
            {
                return LossType.Backgammon;
            }           
        }
	    
	    return LossType.Gammon;	    
	}
	
    public int checkersLeft(PlayerId player)
    {
        int count = 0;
        for (int i = 0; i < 25; ++i)
        {
            if (getCheckerColor(i, player) == player)
            {
                count += getCheckerCount(i, player);
            }           
        }
        return count;       
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
//			m_CubePosition = PositionUtils.ChangeDirection(currentDirection, destDirection, m_CubePosition);
		}
		
		m_direction = destDirection;
	}
	
	public Direction getDirection()
	{
	    return m_direction;
	}
}
