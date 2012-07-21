package com.fairbg.bezma.communication;

import java.util.ArrayList;

import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.core.model.ModelState;

/**Базовая реализация интерфеса ICommunicator*/
public abstract class CommunicatorBase implements ICommunicator {
	
	protected ArrayList<ICommandObserver> m_Observers = new ArrayList<ICommandObserver>();
	
	@Override
	public void notifyObservers(UserCommand aCommand) {
		for (ICommandObserver observer : m_Observers) {
			observer.handeEvent(aCommand);
		}
	}

	@Override
	public void addObserver(ICommandObserver aCommandObserver) {
		m_Observers.add(aCommandObserver);
	}

	@Override
	public void handeEvent(UserCommand userCommand) {
		notifyObservers(userCommand);
	}

	@Override
	public void removeObserver(ICommandObserver aCommandObserver) {
		m_Observers.remove(aCommandObserver);		
	}

	protected ArrayList<IModelView> m_Views = new ArrayList<IModelView>();
	
	/**Отображает на всех устройствах текущее состояние модели*/
	@Override
	public void setModelState(ModelState modelState) {
		for(IModelView view : m_Views)
		{
			view.setModelState(modelState);
		}
	}

	/**Запускает у всех устройств отображения и ввода цикл прослушивания сообщений*/
	@Override
	public boolean start() {
		boolean result = true;
		for(IModelView view : m_Views)
		{
			view.addObserver(this);
			result = result && view.start();
		}
		return result;
	}

	/**Останавливает у всех устройств отображения и ввода цикл прослушивания сообщений*/
	@Override
	public void stop() {
		for(IModelView view : m_Views)
		{
			view.stop();
			view.removeObserver(this);
		}
	}
	
	@Override
	public void addView(IModelView displayView) {		
		m_Views.add(displayView);
	}
	
	@Override
	public void removeView(IModelView displayView) {
		displayView.removeObserver(this);
		m_Views.remove(displayView);		
	}
	
	/**Отсылает всем устройствам отображения пользовательскую комманду*/
	@Override
	public void sendCommand(UserCommand command) {

		for(IModelView view: m_Views)
		{
			view.sendCommand(command);
		}
		
	}
}