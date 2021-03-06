package com.fairbg.bezma.core;

import com.fairbg.bezma.communication.IBroadcast;
import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.core.model.IControllersFactory;
import com.fairbg.bezma.store.IModelSerializer;


/**Интерфейс для фабрики конфигуратора*/
public interface IConfigurator {

	/** 
	 * По текущей конфигурации создает коммуникатор - объект ответственны за передачу сообщений от модели к устройствам ввода/отображения 
	 */
	public abstract ICommunicator createCommunicator(Configuration configuration) throws WrongConfigurationException;

	/**Создает объект базы данных*/
	public abstract IModelSerializer createDatabase(Configuration configuration) throws WrongConfigurationException;

	/**Создает объект для передачи данных в Интернет*/
	public abstract IBroadcast createBroadcastModule(Configuration configuration) throws WrongConfigurationException;
	
	public abstract IControllersFactory createControllersFactory() throws WrongConfigurationException;

}