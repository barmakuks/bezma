package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Actions;
import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmMoveMove extends TBzmMoveBase {

    /// <summary>
    /// Возвращает запись одного передвижения фишки в формате SGF
    /// </summary>
    /// <param name="IsBlack">Если ходит черный игрок</param>
    /// <param name="MoveFrom">Откуда взять фишку</param>
    /// <param name="MoveTo">Куда переместить фишку</param>
    /// <returns></returns>
    private String GetSgfMoveString(boolean IsBlack, int MoveFrom, int MoveTo)
    {
        String result = "";
        /** @todo
        if (IsBlack)
        {
            if (MoveTo != 0)
            {
                if (MoveFrom == 0) result = "y";
                else result = Convert.ToString((char)(MoveFrom - 1 + 'a'));
                if (MoveTo >= 25) result = result + "z";
                else result = result + (char)('a' + MoveTo - 1);
            }
        }
        else
        {
            if (MoveTo != 0)
            {
                if (MoveFrom == 0) result = "y";
                else result = Convert.ToString((char)('a' + (24 - MoveFrom)));
                if (MoveTo >= 25) result = result + "z";
                else result = result + (char)('a' + 24 - MoveTo);
            }
        }
        */
        return result;
    }

    private int ConvertSgfCharToInt(char aSymbol) 
    {
        int res = -1;
        if (aSymbol == 'y') return 0;
        if (aSymbol == 'z') return 25;
        if (aSymbol >= 'a' && aSymbol <= 'x') 
        {
            res = (byte)aSymbol - (byte)('a') + 1;
            if (PlayerId == PlayerColors.WHITE)
                return 25 - res;
            else
                return res;
        }
        else
            return -1;
    }
    public byte Die1, Die2;
    public boolean HasAlternativeDice = false;
    public String PositionBeforeMove = "";
    public String PositionAfterMove = "";

    public TBzmMovement[] Movements = new TBzmMovement[4];
    
    public TBzmMoveMove() 
    {
        m_ActionType = Actions.Move;
        
        for (int i = 0; i < 4; i++)
        {
        	Movements[i] = new TBzmMovement();
        }            
    }
    public TBzmMoveMove(PlayerColors aPlayerId, byte aMoveNo, short aSeconds, TBzmMove aMove) 
    {
    	super(aPlayerId, aMoveNo, aSeconds);
        m_ActionType = Actions.Move;
        
        if (aMove != null)
        {
            Die1 = aMove.GetDie(1);
            Die2 = aMove.GetDie(2);
            HasAlternativeDice = aMove.HasAlternativeDice;
            if (aMove.PositionAfterMove != null)
                PositionAfterMove = aMove.PositionAfterMove.getStateString();
            if (aMove.PositionBeforeMove != null)
                PositionBeforeMove = aMove.PositionBeforeMove.getStateString();
            for (int i = 0; i < 4; i++)
            {
                Movements[i] = new TBzmMovement(aMove.Movement(i).MoveFrom, aMove.Movement(i).MoveTo, aMove.Movement(i).Color, aMove.Movement(i).Strike);
            }
        }
        else 
        {
            for (int i = 0; i < 4; i++)
            {
            	Movements[i] = new TBzmMovement();
            }
                
        }
    }
    public void Change(TBzmMove aMove) 
    {
        Die1 = aMove.GetDie(1);
        Die2 = aMove.GetDie(2);
        for (int i = 0; i < 4; i++)
            Movements[i] = new TBzmMovement(aMove.Movement(i).MoveFrom, aMove.Movement(i).MoveTo, aMove.Movement(i).Color, aMove.Movement(i).Strike);
    }
    
    @Override
    public String toString() {
    	/** @todo
        char separator = ' ';
        String format_string = "{0:D2}:{1}({2},{3})";
        if (HasAlternativeDice)
            format_string = "{0:D2}:{1}({2},{3})*";
        String res = String.Format(format_string, MoveNo, TBzmPlayerColorUtils.GetPlayerShortColorString(PlayerId), Die1, Die2);
        for (int i = 0; i < 4; i++) 
        {
            if (!Movements[i].IsEmpty) 
            {
                if (Movements[i].MoveFrom == 0)
                    res = res + String.Format("{0}bar/{1}{2}", separator, 25 - Movements[i].MoveTo, Movements[i].Strike ? "*" : "");
                else
                    if (Movements[i].MoveTo > 24)
                        res = res + String.Format("{0}{1}/off", separator, 25 - Movements[i].MoveFrom);
                    else
                        res = res + String.Format("{0}{1}/{2}{3}", separator, 25 - Movements[i].MoveFrom, 25 - Movements[i].MoveTo, Movements[i].Strike ? "*" : "");
                separator = ';';
            }
        }
        return res;
        */ return "";
    }
    @Override
    public String[] ToSgfString() 
    {
        String format = "";
        switch (PlayerId) 
        {
            case BLACK:
                format = ";B[{0}{1}{2}]BL[{3}]"; break;
            case WHITE:
                format = ";W[{0}{1}{2}]WL[{3}]"; break;
        }
        String moves = GetSgfMoveString(PlayerId == PlayerColors.BLACK, Movements[0].MoveFrom, Movements[0].MoveTo);
        moves += GetSgfMoveString(PlayerId == PlayerColors.BLACK, Movements[1].MoveFrom, Movements[1].MoveTo);
        moves += GetSgfMoveString(PlayerId == PlayerColors.BLACK, Movements[2].MoveFrom, Movements[2].MoveTo);
        moves += GetSgfMoveString(PlayerId == PlayerColors.BLACK, Movements[3].MoveFrom, Movements[3].MoveTo);
        
        /// @todo return new string[]{String.Format(format, Die1, Die2, moves, Seconds)};
        return null;
    }
    @Override
    public String ToJFString() {
    /** @todo	
        char separator = ' ';
        String moves = "";
        for (int i = 0; i < 4; i++)
        {
            if (!Movements[i].isEmpty())
            {
                if (Movements[i].MoveFrom == 0)
                    moves = moves + String.Format("{0}25/{1}{2}", separator, 25 - Movements[i].MoveTo, Movements[i].Strike ? "*" : "");
                else
                    if (Movements[i].MoveTo > 24)
                        moves = moves + String.Format("{0}{1}/0", separator, 25 - Movements[i].MoveFrom);
                    else
                        moves = moves + String.Format("{0}{1}/{2}{3}", separator, 25 - Movements[i].MoveFrom, 25 - Movements[i].MoveTo, Movements[i].Strike ? "*" : "");
                separator = ' ';
            }
        }
        return String.Format("{0}{1}: {2}", Die1, Die2, moves);
        
        */
    	return "";
    }

    @Override
    public boolean ParseSgfString(String aStr) {
    	/** @todo
        Die1 = Byte.Parse(aStr.substring(0, 1));
        Die2 = Byte.Parse(aStr.substring(1, 1));
        aStr = aStr.substring(2);
        int i = 0;
        while (aStr.length() >= 2 && i < 4) 
        {
            Movements[i].MoveFrom = ConvertSgfCharToInt(aStr[0]);
            Movements[i].MoveTo = ConvertSgfCharToInt(aStr[1]);
            Movements[i].Color = PlayerId;
            aStr = aStr.Substring(2);
            i++;
        }
        */
        return true;
    }

}
