package com.fairbg.bezma.communication.commands;

/** Пользовательская команда, описывающая состояние доски
 */
public class UserCommandState extends UserCommand {
	/** player ID*/
	public short playerId;
	/** current cube position*/
	public short cubePosition;	
	/** Checkers positions*/
	public short[] checkers = new short[28];
}