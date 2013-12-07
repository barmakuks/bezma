package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;
/**Класс для одного передвижения фишки*/
public class TBzmMovement
{
    public int MoveFrom, MoveTo;
    public boolean Strike;
    public PlayerColors Color;
    public boolean isEmpty()
    {
        return Color == PlayerColors.NONE;
    }
    @Override public String toString() 
    {
    	return "";
    	/*
        if (MoveTo > 24)
            return String.Format("  <{0:D2}> off", MoveFrom);
        else
            if (MoveFrom == 0)
                return String.Format("  <BAR> to <{0:D2}><1>", 25 - MoveTo, Strike ? "*" : "");
            else
                return String.Format("  <{0:D2}> to <{1:D1}><2>", 25 - MoveFrom, 25 - MoveTo, Strike ? "*" : "");
                */    	
    };
    public TBzmMovement()
    {
        Clear();
    }
    public TBzmMovement(TBzmMovement aCopy)
    {
        MoveFrom = aCopy.MoveFrom;
        MoveTo = aCopy.MoveTo;
        Color = aCopy.Color;
        Strike = aCopy.Strike;
    }
    public TBzmMovement(int aMoveFrom, int aMoveTo, PlayerColors aColor, boolean aStrike) 
    {
        MoveFrom = aMoveFrom;
        MoveTo = aMoveTo;
        Color = aColor;
        Strike = aStrike;
    }
    public TBzmMovement Clone() 
    {
        return new TBzmMovement(MoveFrom, MoveTo, Color, Strike);
    }
    public boolean Equal(TBzmMovement aCopy) 
    {
        return (aCopy.MoveTo == MoveTo) && (aCopy.MoveFrom == MoveFrom) && (aCopy.Color == Color);
    }
    static public boolean Equal(TBzmMovement aFirst, TBzmMovement aSecond) 
    {
        return (aFirst == aSecond) || ((aFirst != null) && (aFirst.Equal(aSecond)));
    }
    public void Clear()
    {
        MoveFrom = 0;
        MoveTo = 0;
        Color = PlayerColors.NONE;
        Strike = false;
    }
}
