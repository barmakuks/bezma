package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Actions;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;

public abstract class TBzmMoveBase extends TBzmActionBase {

    public PlayerColors PlayerId;
    public byte MoveNo;
    public short Seconds;
    protected TBzmMoveBase() 
    {
    }
    protected TBzmMoveBase(PlayerColors aPlayerId, byte aMoveNo, short aSeconds) 
    {
        PlayerId = aPlayerId; MoveNo = aMoveNo; Seconds = aSeconds;
    }

	public String getPositionString() {
		return m_PositionString;
	}

	public Actions getActionType()
	{
		return m_ActionType;
	}
}
