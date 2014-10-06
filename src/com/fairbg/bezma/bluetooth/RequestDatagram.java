package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.log.BezmaLog;

/**Датаграмма для опроса внешнего устройства.
 * @details Внешнее устройство при получении такой датаграммы должно откликнутся датаграммой состояния доски*/
public class RequestDatagram extends Datagram {
	
	@Override
	public DatagramType getDatagramType() {
		return DatagramType.G;
	}
	
	@Override
	public byte[] toByteArray() {		
		return createBuffer();
	}
	@Override
	public String toString() {		
		return "REQUEST";
	}
	public static Datagram parse(byte[] array){
		BezmaLog.i("PARSE", "REQUEST DATAGRAM");		
		return new RequestDatagram();		
	}
	@Override
	protected int getBufferLength() {
		// TODO Auto-generated method stub
		return 3;
	}	

}
