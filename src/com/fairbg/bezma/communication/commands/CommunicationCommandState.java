package com.fairbg.bezma.communication.commands;

/** Пользовательская команда, описывающая состояние доски
 */
public class CommunicationCommandState extends CommunicationCommand {
	/** player ID*/
	public short playerId;
	/** current cube position*/
	public short cubePosition;	
	/** Checkers positions*/
	public short[] checkers = new short[28];
}