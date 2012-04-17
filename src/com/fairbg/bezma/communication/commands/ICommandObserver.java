package com.fairbg.bezma.communication.commands;


/**
 * Интерфейс наблюдателя за пользовательскими командами
 */
public interface ICommandObserver {

	/** Вызывается когда слушатель получает сообщение о пользовательской команде*/
	public abstract void handeEvent(UserCommand aCommand);

}
