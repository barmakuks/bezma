package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;

public interface IBzmBoardPosition {
    //void SetPositionValues(int aPos, byte aCount, TBzmPlayerColor aColor);
    boolean getReflect();
    void setReflect(boolean reflcet);

    TBzmCheckerPosition getCheckersPosition(int aPos, PlayerColors aPlayerId);
    boolean ApplayMovement(int aMoveFrom, int aMoveTo, PlayerColors aPlayerId);
    
    void ClearBoard();
    IBzmBoardPosition Clone();
    void Clone(IBzmBoardPosition aCopy);
    void ReadState(TBzmStateList fStates, int anIndex);
    int GetPips(PlayerColors aPlayerId);
    int OffCheckersCount(PlayerColors aPlayerId);
    boolean Like(IBzmBoardPosition anOtherBoard);
    boolean Equal(IBzmBoardPosition anOtherBoard);
    boolean AbsEqual(IBzmBoardPosition anOtherBoard);
    String getStateString();
    void setStateString(String stateString);
    boolean HasCheckerInBar(PlayerColors aPlayerId);


}
