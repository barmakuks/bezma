package com.fairbg.core.commands;
/**
 * Умеет поучать комманды
 * */
public interface ICommandListener {
	/** Вызывается при получении команды 
	 * @param commander объект пославший команду
	 * @param command команда
	 */
	void onCommand(ICommander commander, UserCommand command);
}
