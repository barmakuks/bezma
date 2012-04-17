package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.bluetooth.Datagram;
/**Объект прослушивающий события о получении датаграмм*/
public interface IDatagramObserver {
	
	/**Вызывается когда прослушиваемый объект получил датаграмму*/
	void handleEvent(Datagram datagram);
}
