package com.fairbg.bezma.communication.commands;

/**
 * User command, describes the position of checkers on board, cube position and user id who press the button
 */
public class CommunicationCommandState extends CommunicationCommand
{
	/** player ID */
	public short   playerId;

	/**
	 * current cube position
	 * @details cubePosition possible values:
	 *          -1 - there is no cube on any position,
	 *          0 - cube in the middle position,
	 *          1 - cube on black side,
	 *          2 - cube on white side
	 */
	public short   cubePosition;

	/** Checkers positions */
	public short[] checkers = new short[28];
}