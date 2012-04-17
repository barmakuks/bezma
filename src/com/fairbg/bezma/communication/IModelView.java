package com.fairbg.bezma.communication;

import com.fairbg.bezma.communication.commands.ICommandObservable;
import com.fairbg.bezma.communication.commands.ICommandReceiver;

/**Интерфйс для всех устройств отображения состояния модели и ввода информации*/
public interface IModelView extends ICommandObservable, ICommandReceiver, IModelStateListener {

}
