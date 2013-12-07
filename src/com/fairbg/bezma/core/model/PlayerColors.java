package com.fairbg.bezma.core.model;

public enum PlayerColors { 
	BLACK, WHITE, NONE;

	public static PlayerColors getAltColor(PlayerColors color)
	{
		return color == NONE ? NONE :
			color == BLACK ? WHITE : BLACK;
	}
};