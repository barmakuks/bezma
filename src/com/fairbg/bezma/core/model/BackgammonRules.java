package com.fairbg.bezma.core.model;

import java.util.ArrayList;

import com.fairbg.bezma.log.BezmaLog;

public class BackgammonRules {

    private ArrayList<Move> m_FoundMoves = new ArrayList<Move>();

    /**
     * Check if position is start position
     * @param position position to check
     * @return true if position is start position
     */
	public boolean checkStartPosition(Position position)
	{
		int startA[] = { 0,-2,0,0,0,0,5,0,3,0,0,0,-5,5,0,0,0,-3,0,-5,0,0,0,0,2,0,0,0};
		int startB[] = { 0,2,0,0,0,0,-5,0,-3,0,0,0,5,-5,0,0,0,3,0,5,0,0,0,0,-2,0,0,0};
		int startAi[] = { 0,-5,0,0,0,3,0,5,0,0,0,0,-2,2,0,0,0,0,-5,0,-3,0,0,0,5,0,0,0};
		int startBi[] = { 0,5,0,0,0,-3,0,-5,0,0,0,0,2,-2,0,0,0,0,5,0,3,0,0,0,-5,0,0,0};
		
		boolean equalA = true;
		boolean equalB = true;
		boolean equalAi = true;
		boolean equalBi = true;
		
		int pos[] = position.getCheckers();
		
		BezmaLog.i("RULES", "Checking start position :\n" + position.toString());
		for (int i = 0; i < pos.length; ++i)
		{
			equalA = equalA && (startA[i] == pos[i]);
			equalB = equalB && (startB[i] == pos[i]);
			equalAi = equalAi && (startAi[i] == pos[i]);
			equalBi = equalBi && (startBi[i] == pos[i]);
		}		
		
		return equalA || equalB || equalAi || equalBi;
	}

	/**
	 * Tries to find move
	 * @param from previous position 
	 * @param to final position
	 * @param player player, who press the button
	 * @return SkipMove, DoubleMove or CheckerMove
	 */
	public Move findMove(int die1, int die2, Position from, Position to, PlayerColors player)
	{		
        m_FoundMoves.clear();
        
		// Check if this skip move, return SkipMove
        // Если ни одна фишка не передвинута, то смотрим возможность пропуска хода
        /*if (aPositionFrom.AbsEqual(aPositionTo) && !StillCanDoMovement(move, aPositionFrom))
            return move;*/

		// Check if this doubling move, return DoubleMove
		
		
		// Check if this move return CheckerMove
        try {
        	if (die1 * die2 != 0)
        	{
                Move move = new Move();
                move.setPlayer(player);
                move.setDice(die1, die2);
        		findPosibleMoves(move, from, to, player);        		
        	}
        	else
        	{
        		final int MAX_DIE = 6;
        		for (die1 = 1; die1 <= MAX_DIE; ++die1)
        		{
        			for (die2 = die1; die2 <= MAX_DIE; ++die2)
        			{
        		        Move move = new Move();
        		        move.setPlayer(player);
        		        move.setDice(die1, die2);        				
        				findPosibleMoves(move, from, to, player);
        			}
        		}
        	}
        	
			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (m_FoundMoves.size() > 0)
        {
        	return getBestMove(from, player);
        }

        return null;		
	}
	
	private Move getBestMove(Position from, PlayerColors player) {
        PlayerColors otherPlayer = PlayerColors.getAltColor(player);
        Move best_move = null, temp_move = null;
        int aMinMovesLeft = -1000, aMovesLeft = 0;
        Position aPositionCopy;
        try {
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
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
        return best_move;
	}

	private boolean findPosibleMoves(final Move move, final Position fromPosition, final Position toPosition, PlayerColors player) throws CloneNotSupportedException {
        Position aPositionFromCopy = null;
        Move move_copy = null;
        int from_index = 26;
        while (true)
        {
            from_index = findFirstMovedChecker(fromPosition, toPosition, player, from_index);
            if (from_index == -1) 
            {
            	return false;
            }
            for (int i = 0; i <= 3; i++)
            {
                move_copy = (Move) move.clone();
                aPositionFromCopy = (Position) fromPosition.clone();
                if (move_copy.getDieValue(i) != 0 && move_copy.getMovement(i) == null)
                {
                	final int dest_index = from_index - move.getDieValue(i);
                	
                    if (canApplayMovement(fromPosition, from_index, dest_index, player))
                    {

                        boolean isStrike = false;
                    	
                    	if (dest_index > 0)
                        {
                            PlayerColors color = aPositionFromCopy.getCheckerColor(dest_index, player);
                            isStrike = color != player && color != PlayerColors.NONE;
                        }

                        move_copy.setMovement(i, from_index, dest_index, isStrike);

                        BezmaLog.i("FOUND STATE", "("+move_copy.getDieValue(0) + ":" + move_copy.getDieValue(1) + ")");
                    	BezmaLog.i("FOUND STATE", aPositionFromCopy.toString());
                        aPositionFromCopy.applayMovement(from_index, dest_index, player);
                    	BezmaLog.i("FOUND STATE", aPositionFromCopy.toString());

                    	if (stillCanDoMovement(move_copy, aPositionFromCopy))
                        {
                        	BezmaLog.i("FOUND STATE", "search again...");
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

	private boolean stillCanDoMovement(Move move, Position position) {

		if (position.hasCheckerInBar(move.getPlayer()))
        {
            for (int i = 0; i < 4; i++)
            {
                if (move.getDieValue(i) != 0 && move.getMovement(i) == null &&
                    canApplayMovement(position, 0, move.getDieValue(i), move.getPlayer()))
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
                for (int i = 0; i < 4; i++)
                {
                    if (move.getDieValue(i) != 0 && move.getMovement(i) == null &&
                        canApplayMovement(position, j, j - move.getDieValue(i), move.getPlayer()))
                        return true;
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
            
        if (position.hasCheckerInBar(player) && from_index != 0) // Если есть шашка на баре и ход не из бара
        {
        	return false;
        }
            
        if (to_index >= 25)
        {
            if (!canOffChecker(position, srcCheckerColor))
            {
            	return false;
            }
                
            if (to_index != 25 && hasLefterCheckerInHome(position, from_index, player))
            {
            	return false;
            }                
        }
        else
        {
            if ((srcCheckerColor != destCheckerColor &&
                 destCheckerColor != PlayerColors.NONE) &&
                 destCheckerCount > 1)
            {
            	return false;
            }                
        }
        return true;
	}

	private boolean hasLefterCheckerInHome(Position position, int from_index, PlayerColors player) {
        for (int i = from_index - 1; i >= 19 ;i--)
        {
            if (position.getCheckerColor(from_index, player) == position.getCheckerColor(i, player))
            {
            	return true;
            }                
        }
        return false;
	}

	private boolean canOffChecker(Position position, PlayerColors checkerColor) {
        
		for (int i = 0; i <= 18; i++)
		{
            if (position.getCheckerColor(i, checkerColor) == checkerColor && position.getCheckerCount(i, checkerColor) != 0)
            {
            	return false;
            }
		}                
        return true;
	}

	private int findFirstMovedChecker(Position from, Position to, PlayerColors player, int start_from) {
        
		for (int i = start_from - 1; i >= 0; i--)
        {
            if (from.getCheckerColor(i, player) == player &&
                (from.getCheckerCount(i, player) > to.getCheckerCount(i, player) ||
                 from.getCheckerCount(i, player) >= Position.MAX_CHECKERS_IN_POSITION))
            {
                return i;
            }
        }
        return -1;
	}

}
