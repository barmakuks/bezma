package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.VictoryTypes;
import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmGameResult implements Cloneable {
    private PlayerColors FWinner = PlayerColors.NONE;
    private int FCheckersRemained;
    private byte FDoubler = 1;
    private VictoryTypes FWinType;
    public TBzmGameResult(PlayerColors aWinner, int aCheckersRemained, byte aDoubler, VictoryTypes aWinType)
    {
        FWinType = aWinType;
        FWinner = aWinner;
        FDoubler = aDoubler;
        FCheckersRemained = aCheckersRemained;
    }
    public PlayerColors getWinner()
    {
        return FWinner; 
    }
    public int getCheckersRemained()
    {
        return FCheckersRemained;
    }
    public byte getDoubler()
    {
        return FDoubler; 
    }
    public boolean isPropose() 
    {
        return FWinType == VictoryTypes.DropDouble;
    }
    public boolean isBackgammon() 
    {
        return FWinType == VictoryTypes.Backgammon;
    }
    public VictoryTypes getWinType()
    {
        return FWinType; 
    }
    public byte getWinnerPoints()
    {
            switch (FWinType)
            {
                case Backgammon:
                    return (byte)(FDoubler * 3);
                case Gammon:
                    return (byte)(FDoubler * 2);
                default:
                    return FDoubler;
            }
    }
    public Object Clone()
    {
        return new TBzmGameResult(FWinner, FCheckersRemained, FDoubler, FWinType);
    }

}
