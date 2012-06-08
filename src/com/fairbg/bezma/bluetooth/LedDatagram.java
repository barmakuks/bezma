package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.log.BezmaLog;

/**Датаграммы для включения/выключения лампочки на внешнем устройстве*/
public class LedDatagram extends Datagram {

	@Override
	public DatagramType getDatagramType() {
		return DatagramType.L;
	}
	/** Идентификатор кнопки, на которой расположена лампочка*/
	public byte button = 0;
	
	/** Состояние лампочки: ON = 1, OFF = 0*/
	public byte state = 0;
	
	
	@Override
	public byte[] toByteArray() {
		byte[] buffer = createBuffer();
		buffer[2] = button;
		buffer[3] = state;
		return buffer;
	}
	
	@Override
	public String toString() {		
		return "LED";
	}
	
	public static Datagram parse(byte[] array){
		BezmaLog.i("PARSE", "LED DATAGRAM");		
		return new LedDatagram();		
	}
	
	@Override
	protected int getBufferLength() {
		return 5;
	}	
}
