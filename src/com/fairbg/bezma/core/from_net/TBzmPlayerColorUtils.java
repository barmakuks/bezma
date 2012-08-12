package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmPlayerColorUtils {
    public static PlayerColors OtherPlayer(PlayerColors aPlayerId)
    {
        switch (aPlayerId)
        {
            case WHITE:
                return PlayerColors.BLACK;
            case BLACK:
                return PlayerColors.WHITE;
            default:
                return PlayerColors.NONE;
        }
    }
    public static int GetBitsCount(int aMask, int startPos, int endPos)
    {
        int res = 0;
        int mask = 1 << startPos;
        for (int i = startPos; i <= endPos; i++, mask <<= 1)
        {
            if ((aMask & mask) != 0) res++;
        }
        return res;
    }
    public static String GetPlayerColorString(PlayerColors aPlayerId)
    {
        switch (aPlayerId)
        {
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            case NONE:
                return "None";
            default:
                return "Unknown";
        }
    }
    public static String GetPlayerShortColorString(PlayerColors aPlayerId)
    {
        switch (aPlayerId)
        {
            case BLACK:
                return "B";
            case WHITE:
                return "W";
            case NONE:
                return "N";
            default:
                return "Un";
        }
    }

}
