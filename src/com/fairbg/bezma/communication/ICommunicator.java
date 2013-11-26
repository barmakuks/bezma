package com.fairbg.bezma.communication;

import com.fairbg.bezma.communication.commands.ICommandObservable;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.ICommandReceiver;

/** Интерфейс связи модели с устройствами отображения */
public interface ICommunicator extends ICommandObserver, IModelStateListener,
	ICommandObservable, ICommandReceiver, IModelView
{
    /**
     * Добавляет устройство отображения
     * @param displayView
     */
    void addView(IModelView displayView);

    /**
     * Удаляет устройство отображения
     * @param displayView
     */
    void removeView(IModelView displayView);

}
