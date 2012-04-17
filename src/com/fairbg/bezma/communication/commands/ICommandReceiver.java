package com.fairbg.bezma.communication.commands;

/**
 * Интерфейс, позволяет объекту отсылать пользовательский команды 
 */
public interface ICommandReceiver {
	
	/**
	 * Отослать пользовательскую команду
	 * @param command User command
	 */
	public abstract void sendCommand(UserCommand command);

}