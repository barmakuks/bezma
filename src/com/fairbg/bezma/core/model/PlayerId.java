package com.fairbg.bezma.core.model;

public enum PlayerId { 
	BLACK, WHITE, NONE;

	public static PlayerId getOppositeId(PlayerId color)
	{
		return color == NONE ? NONE :
			color == BLACK ? WHITE : BLACK;
	}
};