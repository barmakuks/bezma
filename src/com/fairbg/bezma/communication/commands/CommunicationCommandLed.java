package com.fairbg.bezma.communication.commands;

/**пользовательская команда для изменения состояния кнопки*/
public class CommunicationCommandLed extends CommunicationCommand {

	public byte button;
	
	public byte state;
}