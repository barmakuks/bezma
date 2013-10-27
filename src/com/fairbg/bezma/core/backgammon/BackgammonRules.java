package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;

import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;

public class BackgammonRules
{

    private ArrayList<Move> m_FoundMoves = new ArrayList<Move>();

    public static int[] getStartPosition(Position.Direction direction)
    {
	switch(direction)
	{
	case BlackCCW:
	    return new int[] { 0, -2, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2, 0, 0, 0 };
	case BlackCW:
	    return new int[] { 0, 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5, -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 };
	case WhiteCW:
	    return new int[] { 0, -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5, 0, 0, 0 }; 
	case WhiteCCW:
	    return new int[] { 0, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, -5, 0, 0, 0 }; 
	    default:
		return null;
	}
    } 
    
    /**
     * Check if position is start position, and returns board position direction
     * if it is not a start position returns BoardDirection.None
     * @param position Position to check
     */
    public Position.Direction getStartPositionDirection(Position position)
    {
	int pos[] = position.getCheckers();
	
	for (Position.Direction direction : Position.Direction.values())
	{
	    int start[] = BackgammonRules.getStartPosition(direction);	    
	    
	    if (start != null)
	    {
		boolean equal = true;
	        for (int i = 0; equal && i < pos.length; ++i)
	        {
	            equal = equal && (start[i] == pos[i]);
	        }		
	        if (equal)
	        {
	            return direction;
	        }
	    }
	}
	
	return Position.Direction.None;
    }

    /**
     * Tries to find move
     * 
     * @param from
     *            previous position
     * @param to
     *            final position
     * @param player
     *            player, who press the button
     * @return SkipMove, DoubleMove or CheckerMove
     */
    public Move findMove(int die1, int die2, Position from, Position to, PlayerColors player)
    {
        m_FoundMoves.clear();

        // Check if this skip move, return SkipMove
        // Если ни одна фишка не передвинута, то смотрим возможность пропуска
        // хода
        // if (from.AbsEqual(to) && !StillCanDoMovement(move, aPositionFrom))
        // {
        // Move skipMove = new Move();
        // skipMove.setDice(die1, die2);
        // return skipMove;
        // }

        // Check if this doubling move, return DoubleMove

        // Check if this move return CheckerMove
        try
        {
            if (die1 * die2 != 0)
            {
                Move move = new Move();
                move.setPlayer(player);
                move.setDice(die1, die2);

                if (canSkipMove(move, from, to, player))
                {
                    return move;
                }

                findPosibleMoves(move, from, to, player);
            }
            else
            {
                final int MAX_DIE = 6;
                for (die1 = 1; die1 <= MAX_DIE; ++die1)
                {
                    for (die2 = MAX_DIE; die2 >= die1; --die2)
                    {
                        Move move = new Move();
                        move.setPlayer(player);
                        move.setDice(die1, die2);

                        if (canSkipMove(move, from, to, player))
                        {
                            return move;
                        }

                        findPosibleMoves(move, from, to, player);
                    }
                }
            }

        }
        catch (CloneNotSupportedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (m_FoundMoves.size() > 0)
        {
            return getBestMove(from, player);
        }

        return null;
    }

    private boolean canSkipMove(Move move, Position from, Position to, PlayerColors player)
    {
        return from.approxEquals(to) && !stillCanDoMovement(move, from);
    }

    private Move getBestMove(Position from, PlayerColors player)
    {
        PlayerColors otherPlayer = PlayerColors.getAltColor(player);
        Move best_move = null, temp_move = null;
        int aMinMovesLeft = -1000, aMovesLeft = 0;
        Position aPositionCopy;
        try
        {
            for (int i = 0; i < m_FoundMoves.size(); i++)
            {
                aPositionCopy = (Position) from.clone();
                temp_move = m_FoundMoves.get(i);
                aPositionCopy.applyMove(temp_move);

                aMovesLeft = aPositionCopy.getPips(otherPlayer) - aPositionCopy.getPips(player);

                if (aMinMovesLeft < aMovesLeft)
                {
                    best_move = temp_move;
                    aMinMovesLeft = aMovesLeft;
                }
            }
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
        return best_move;
    }

    private boolean findPosibleMoves(final Move move, final Position fromPosition, final Position toPosition, PlayerColors player) throws CloneNotSupportedException
    {

        Position aPositionFromCopy = null;
        Move move_copy = null;
        int from_index = Position.BAR_POSITION + 1;// 26;

        while (true)
        {
            from_index = findFirstMovedChecker(fromPosition, toPosition, player, from_index);

            if (from_index == -1)
            {
                return false;
            }

            for (Integer currentDie : move.getPossibleDies())
            {
                move_copy = (Move) move.clone();

                aPositionFromCopy = (Position) fromPosition.clone();

                {
                    final int dest_index = from_index - currentDie;

                    if (canApplayMovement(fromPosition, from_index, dest_index, player))
                    {

                        boolean isStrike = false;

                        if (dest_index > 0)
                        {
                            PlayerColors color = aPositionFromCopy.getCheckerColor(dest_index, player);
                            isStrike = color != player && color != PlayerColors.NONE;
                        }

                        move_copy.appendMovement(from_index, dest_index, isStrike);

                        BezmaLog.i("FOUND STATE", "(" + move_copy.getDie1() + ":" + move_copy.getDie2() + ")");
                        BezmaLog.i("FOUND STATE", "Position before\n" + aPositionFromCopy.toString());
                        aPositionFromCopy.applayMovement(from_index, dest_index, player);
                        BezmaLog.i("FOUND STATE", "Position after\n" + aPositionFromCopy.toString());

                        if (stillCanDoMovement(move_copy, aPositionFromCopy))
                        {
                            BezmaLog.i("FOUND STATE", "still can do movement. continue search from : " + aPositionFromCopy);
                            BezmaLog.i("FOUND STATE", "to : " + toPosition);
                            findPosibleMoves(move_copy, aPositionFromCopy, toPosition, player);
                        }
                        else
                        {
                            BezmaLog.i("FOUND STATE", "move complete***********************************");
                            if (aPositionFromCopy.approxEquals(toPosition))
                            {
                                boolean NotFoundEqual = true;

                                for (int j = 0; j < m_FoundMoves.size(); j++)
                                {
                                    NotFoundEqual = NotFoundEqual && !(m_FoundMoves.get(j).equals(move_copy));
                                }
                                if (NotFoundEqual)
                                {
                                    m_FoundMoves.add((Move) move_copy.clone());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean stillCanDoMovement(Move move, Position position)
    {

        if (position.hasCheckerInBar(move.getPlayer()))
        {
            for (Integer currentDie : move.getPossibleDies())
            {
                if (canApplayMovement(position, Position.BAR_POSITION, Position.BAR_POSITION - currentDie, move.getPlayer()))
                {
                    return true;
                }
            }
            return false;
        }
        for (int j = 1; j <= 24; j++)
        {
            if (position.getCheckerColor(j, move.getPlayer()) == move.getPlayer())
            {
                for (Integer currentDie : move.getPossibleDies())
                {
                    if (canApplayMovement(position, j, j - currentDie, move.getPlayer()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canApplayMovement(Position position, int from_index, int to_index, PlayerColors player)
    {
        final int destCheckerCount = position.getCheckerCount(to_index, player);
        final PlayerColors destCheckerColor = position.getCheckerColor(to_index, player);
        final int srcCheckerCount = position.getCheckerCount(from_index, player);
        final PlayerColors srcCheckerColor = position.getCheckerColor(from_index, player);

        if (srcCheckerCount == 0) // Если нет шашки для хода
        {
            return false;
        }

        if (position.hasCheckerInBar(player) && from_index != Position.BAR_POSITION) // Если есть шашка на баре и ход не из бара
        {
            return false;
        }

        if (to_index <= 0) // шашка на выброс
        {
            if (!canOffChecker(position, srcCheckerColor))
            {
                return false;
            }

            if (to_index < 0 && hasLefterCheckerInHome(position, from_index, player))
            {
                return false;
            }
        }
        else
        {
            if ((srcCheckerColor != destCheckerColor && destCheckerColor != PlayerColors.NONE) && destCheckerCount > 1)
            {
                return false;
            }
        }
        return true;
    }

    private boolean hasLefterCheckerInHome(Position position, int from_index, PlayerColors player)
    {
        // check if is any checker with same color and greater indes
        for (int i = from_index + 1; i <= Position.BAR_POSITION; i++)
        {
            if (position.getCheckerColor(from_index, player) == position.getCheckerColor(i, player))
            {
                return true;
            }
        }
        return false;
    }

    private boolean canOffChecker(Position position, PlayerColors checkerColor)
    {
        // Check if is any checker out of home
        for (int i = 7; i <= Position.BAR_POSITION; i++)
        {
            if (position.getCheckerColor(i, checkerColor) == checkerColor && position.getCheckerCount(i, checkerColor) != 0)
            {
                return false;
            }
        }
        return true;
    }

    private int findFirstMovedChecker(Position from, Position to, PlayerColors player, int start_from)
    {

        for (int i = start_from - 1; i >= 0; i--)
        {
            if (from.getCheckerColor(i, player) == player && (from.getCheckerCount(i, player) > to.getCheckerCount(i, player) || from.getCheckerCount(i, player) >= Position.MAX_CHECKERS_IN_POSITION))
            {
                return i;
            }
        }
        return -1;
    }

}
