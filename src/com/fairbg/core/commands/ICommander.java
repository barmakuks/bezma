package com.fairbg.core.commands;
/** Умеет отсылать команды слушателям */
public interface ICommander {
	/**
	 * Регистрирует слушателя команд
	 * @param listener слушатель команд
	 */
	void addListener(ICommandListener listener);
	/** рассылает слушателям комманды 
	 * @param command
	 */
	void sendCommand(Command command);
	/** Начинает прослушивание внешних устройств и отсылку сообщений слушателю */
	void start();
	/** Заканчивает прослушивание внешних устройств и отсылку сообщений слушателю */	
	void stop();
}
