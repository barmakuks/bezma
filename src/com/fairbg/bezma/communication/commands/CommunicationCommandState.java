package com.fairbg.bezma.communication.commands;

import com.fairbg.bezma.bluetooth.StateDatagram;

/**
 * User command, describes the position of checkers on board, cube position and user id who press the button
 */
public class CommunicationCommandState extends CommunicationCommand
{
	/** player ID */
	public short   playerId;

	public StateDatagram.CubeState   cubePosition;

	/** Checkers positions */
	public short[] checkers = new short[28];
}