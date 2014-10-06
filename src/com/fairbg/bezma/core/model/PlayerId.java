package com.fairbg.bezma.core.model;

public enum PlayerId { 
	RED, SILVER, NONE;

	public static PlayerId getOppositeId(PlayerId color)
	{
		return color == NONE ? NONE :
			color == RED ? SILVER : RED;
	}
};