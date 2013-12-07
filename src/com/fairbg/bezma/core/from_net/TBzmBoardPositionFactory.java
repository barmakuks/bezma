package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Rules;

public class TBzmBoardPositionFactory {
    static public IBzmBoardPosition Create(Rules rulesType) 
    {
        switch (rulesType) 
        {
            case Backgammon:
                return new TBzmBackgammonBoardPosition();
            case RussianBackgammon:
                return new TBzmLongBackgammonBoardPosition();
        }
        return null;
    }
    static public IBzmBoardPosition Create(Rules rulesType, boolean reflect)
    {
        switch (rulesType)
        {
            case Backgammon:
                return new TBzmBackgammonBoardPosition(reflect);
            case RussianBackgammon:
                return new TBzmLongBackgammonBoardPosition();
        }
        return null;
    }
    public static String NextPositionString(Rules rulesType, String currentPositionStr, TBzmMoveBase move)
    {
        IBzmBoardPosition position = null;
        switch (rulesType) 
        {
            case Backgammon:
                position = new TBzmBackgammonBoardPosition();
                break;
            case RussianBackgammon:
                position = new TBzmLongBackgammonBoardPosition();
                break;
        } 
        TBzmRateDie rateDie = new TBzmRateDie();
        rateDie.setStateString(currentPositionStr);
        position.setStateString(currentPositionStr.substring(4));
        switch (move.getActionType())
        {
            case Move:
                TBzmMoveMove temp_move_move = (TBzmMoveMove)move;
                for (int i = 0; i < 4; i++)
                {
                	position.ApplayMovement(temp_move_move.Movements[i].MoveFrom, temp_move_move.Movements[i].MoveTo, temp_move_move.PlayerId);
                }                    
                return rateDie.getStateString() + position.getStateString();
            
            case Double:
                TBzmDoubleMove temp_double_move = (TBzmDoubleMove)move;
                rateDie.Double(temp_double_move.PlayerId);
                if (temp_double_move.Beaver)
                    rateDie.Beaver();
                if (temp_double_move.Take)
                    rateDie.Take();
                if (!temp_double_move.Take && !temp_double_move.Beaver)
                    rateDie.Pass();
                return rateDie.getStateString()+ position.getStateString();
            default:
                return currentPositionStr;

        }
    }

}
