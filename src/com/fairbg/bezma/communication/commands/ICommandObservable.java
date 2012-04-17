package com.fairbg.bezma.communication.commands;


/**
 * Интерфейс объектов для транслятора сообщений о пользовательских командах
 */
public interface ICommandObservable {

	public abstract void notifyObservers(UserCommand aCommand);

	public abstract void addObserver(ICommandObserver aCommandObserver);

	public abstract void removeObserver(com.fairbg.bezma.communication.commands.ICommandObserver aCommandObserver);

	/** Запускает цикл получения пользовательских команд */
	public abstract boolean start();

	/** Останавливает цикл получения пользовательских команд */
	public abstract void stop();

}
