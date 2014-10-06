package com.fairbg.bezma.bluetooth;

/**Фейковая датаграмма. Служит для указания того, что пришедшая датаграмма не распознана правильно */
public class WrongDatagram extends Datagram {

	@Override
	public DatagramType getDatagramType() {
		return null;
	}
	/**Содержит в себе всю нераспознанную датаграмму*/
	private byte[] array;
	
	@Override
	public byte[] toByteArray() {
		return array;
	}

	@Override
	protected int getBufferLength() {
		if(array != null)
		{
			return array.length;
		}
		return 0;
	}

	public static Datagram parse(byte[] array){
		WrongDatagram dg = new WrongDatagram();
		dg.array = array;
		return dg;		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("");		
		sb.append(String.format("RECIVED %1$04d bytes : ", getBufferLength()));
		for(int i = 0 ; i < getBufferLength();i++){
			sb.append(String.format("%1$03d ", array[i]));
		}
		return sb.toString();
	}
}
