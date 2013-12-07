package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.backgammon.Position.Direction;

public class PositionUtils
{
    static public int[] ChangeDirection(Position.Direction srcDirection, Position.Direction destDirection, int srcCheckers[])
    {
	if (srcDirection == destDirection)
	{
	    return srcCheckers;
	}

	int dest[] = new int[srcCheckers.length];

	int commonPos = srcCheckers.length - 3;
	dest[commonPos] = srcCheckers[commonPos];
	dest[commonPos + 1] = srcCheckers[commonPos + 1];
	
	if (srcDirection == Direction.BlackCCW && destDirection == Direction.BlackCW ||
		srcDirection == Direction.BlackCW && destDirection == Direction.BlackCCW ||
		srcDirection == Direction.WhiteCCW && destDirection == Direction.WhiteCW ||
		srcDirection == Direction.WhiteCW && destDirection == Direction.WhiteCCW)
	{
	    for (int i = 0; i != commonPos; ++i)
	    {
		dest[i] = srcCheckers[commonPos - i];
	    }
	}
	else
	{
	    for (int i = 1; i <= 12; ++i)
	    {
		dest[i + 12] = srcCheckers[i];
		dest[i] = srcCheckers[i + 12];
	    }
	    
	    if (srcDirection == Direction.BlackCW && destDirection == Direction.WhiteCCW ||
		srcDirection == Direction.BlackCCW && destDirection == Direction.WhiteCW ||
		srcDirection == Direction.WhiteCW && destDirection == Direction.BlackCCW ||
		srcDirection == Direction.WhiteCCW && destDirection == Direction.BlackCW)
	    {
		for (int i = 1; i <= 12; ++i)
		{
		    int tmp = dest[i];
		    dest[i] = dest[25 - i];
		    dest[25 - i] = tmp;
		}
	    }
	}

	return dest;
    }

    static public boolean EqualPositions(int src[], int dest[])
    {
	if (src == null || dest == null || src.length != dest.length)
	{
	    return false;
	}

        for (int i = 0; i < src.length; ++i)
        {
            if (src[i] != dest[i])
            {
        	return false;
            }
        }		

        return true;
    }

    private static void testChangeDirection(Direction from, Direction to)
    {
	final int src[] = BackgammonRules.getStartPosition(from);
	final int expected[] = BackgammonRules.getStartPosition(to);

	final int dest[] = ChangeDirection(from, to, src);
	
	System.out.println(from + " >> " + to + " : " + EqualPositions(expected, dest));
    }
    
    public static void main(String[] args)
    {
	testChangeDirection(Direction.BlackCW, Direction.BlackCCW);
	testChangeDirection(Direction.BlackCW, Direction.WhiteCW);
	testChangeDirection(Direction.BlackCW, Direction.WhiteCCW);

	testChangeDirection(Direction.BlackCCW, Direction.BlackCW);
	testChangeDirection(Direction.BlackCCW, Direction.WhiteCW);
	testChangeDirection(Direction.BlackCCW, Direction.WhiteCCW);

	testChangeDirection(Direction.WhiteCW, Direction.BlackCW);
	testChangeDirection(Direction.WhiteCW, Direction.BlackCCW);
	testChangeDirection(Direction.WhiteCW, Direction.WhiteCCW);

	testChangeDirection(Direction.WhiteCCW, Direction.BlackCW);
	testChangeDirection(Direction.WhiteCCW, Direction.BlackCCW);
	testChangeDirection(Direction.WhiteCCW, Direction.WhiteCW);
    }

}
