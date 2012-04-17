package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.bluetooth.Datagram;

/**Интерфейс для объектов, которые могут извещать другие объекты о получении датаграмм*/
public interface IDatagramObservable {

	public abstract void addObserver(IDatagramObserver observer);

	public abstract void removeObserver(IDatagramObserver observer);

	public abstract void notifyAll(Datagram datagram);

}