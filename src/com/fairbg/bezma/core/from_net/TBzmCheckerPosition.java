package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmCheckerPosition {

	public PlayerColors Color;
    public int Count;
    public void Set(int aCount, PlayerColors aColor)
    {
        Color = aColor;
        Count = aCount;
    }
    @Override public String toString() 
    {
    	return Count + ":" + Color; 
    };

}
