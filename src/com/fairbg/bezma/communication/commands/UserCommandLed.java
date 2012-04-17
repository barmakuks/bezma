package com.fairbg.bezma.communication.commands;

/**пользовательская команда для изменения состояния кнопки*/
public class UserCommandLed extends UserCommand {

	public byte button;
	
	public byte state;
}