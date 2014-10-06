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
        dest[0] = srcCheckers[0];

        if (srcDirection == Direction.RedCW && destDirection == Direction.GrayCW ||
                srcDirection == Direction.RedCCW && destDirection == Direction.GrayCCW ||
                srcDirection == Direction.GrayCCW && destDirection == Direction.RedCCW ||
                srcDirection == Direction.GrayCW && destDirection == Direction.RedCW)
        {
            // inverse copying 
            for (int i = 0; i != commonPos; ++i)
            {
                dest[i] = srcCheckers[commonPos - i];
            }
        }
        else
        {
            // swap left and right parts
            for (int i = 1; i <= 12; ++i)
            {
                dest[i + 12] = srcCheckers[i];
                dest[i] = srcCheckers[i + 12];
            }

            if (srcDirection == Direction.RedCW && destDirection == Direction.RedCCW ||
                    srcDirection == Direction.RedCCW && destDirection == Direction.RedCW ||
                    srcDirection == Direction.GrayCW && destDirection == Direction.GrayCCW ||
                    srcDirection == Direction.GrayCCW && destDirection == Direction.GrayCW)
            {
                // inverse copying
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
        testChangeDirection(Direction.RedCCW, Direction.RedCW);
        testChangeDirection(Direction.RedCCW, Direction.GrayCW);
        testChangeDirection(Direction.RedCCW, Direction.GrayCCW);

        testChangeDirection(Direction.RedCW, Direction.RedCCW);
        testChangeDirection(Direction.RedCW, Direction.GrayCW);
        testChangeDirection(Direction.RedCW, Direction.GrayCCW);

        testChangeDirection(Direction.GrayCW, Direction.RedCCW);
        testChangeDirection(Direction.GrayCW, Direction.RedCW);
        testChangeDirection(Direction.GrayCW, Direction.GrayCCW);

        testChangeDirection(Direction.GrayCCW, Direction.RedCCW);
        testChangeDirection(Direction.GrayCCW, Direction.RedCW);
        testChangeDirection(Direction.GrayCCW, Direction.GrayCW);
    }

    // public static CubePosition ChangeDirection(Direction srcDirection, Direction destDirection, CubePosition
    // cubePosition)
    // {
    // if ( ((srcDirection == Direction.BlackCW || srcDirection == Direction.BlackCCW)
    // && (destDirection == Direction.WhiteCCW || destDirection == Direction.WhiteCCW))
    // ||
    // ((srcDirection == Direction.WhiteCW || srcDirection == Direction.WhiteCCW)
    // && (destDirection == Direction.BlackCCW || destDirection == Direction.BlackCCW)))
    // {
    // if (cubePosition == CubePosition.North)
    // {
    // return CubePosition.South;
    // }
    // if (cubePosition == CubePosition.South)
    // {
    // return CubePosition.North;
    // }
    // }
    //
    // return cubePosition;
    // }

}
