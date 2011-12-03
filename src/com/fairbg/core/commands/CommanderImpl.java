package com.fairbg.core.commands;

import java.util.ArrayList;

/** 
 * Реализация интерфейса ICommander 
 */
public class CommanderImpl implements ICommander {
    /**
     * Владелец объекта CommanderImpl от имени которого будут посылаться команды
     */
    private ICommander _owner;
    /**
     * Список слушателей
     */
    private ArrayList<ICommandListener> _listeners = new ArrayList<ICommandListener>();

    @Override
    public void addListener(ICommandListener listener) {
        if (!_listeners.contains(listener)) {
            _listeners.add(listener);
        }
    }

    @Override
    public void sendCommand(Command command) {
        for (ICommandListener listener : _listeners) {
            if (listener instanceof ICommandListener) {
                listener.onCommand(_owner, command);
            }
        }
    }

    public CommanderImpl(ICommander owner) {
        _owner = owner;
    }

    /** В данной реализации ничего не делает*/
    @Override
    public void start() {
    }

    /** В данной реализации ничего не делает*/
    @Override
    public void stop() {
    }

    public void recieveCommand(Command command) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
