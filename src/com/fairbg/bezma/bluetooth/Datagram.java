package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.log.BezmaLog;

/** Абстрактный класс описывающий датаграммы, посылаемые и получаемые внешними устройствами
 */
public abstract class Datagram {
	/** Тип датаграммы*/
	public enum DatagramType{
		A(0x41),G(0x47),L(0x4C);
		private final byte code;
		DatagramType(int code){
			this.code = (byte)code;
		}
		public byte toByte(){
			return code;
		}
	}
	
	/**Возвращает тип датаграммы*/
	abstract public DatagramType getDatagramType();
	
	/**Конвертирует датаграмму в массив байт для передаци еговнешнему устройству*/
	abstract public byte[] toByteArray();
	
	/**Возвращает размер датаграммы в бинарном виде*/
	abstract protected int getBufferLength();
	
	/**Выделяет массив байт для бинарного представления датаграммы и 
	 * инициализирует начальные и конечные байты массива*/
	protected  byte[] createBuffer(){
		byte [] array = new byte[getBufferLength()];
		array[0] = (byte)0xFE;
		array[1] = this.getDatagramType().toByte();
		array[array.length - 1] = (byte)0xFF;
		return array;		
	}
	
	/**Статическая функция конвертирует массив байт в датаграмму
	 * @details Проверяет начало и конец датаграммы
	 * Определяет тип датаграммы по второму байту
	 * Передает исходный массив байт соответствующему парсеру для дальнейшей обработки
	 * Если тип датаграммы не определен возвращает объект WrongDatagram*/
	public static Datagram parse(byte[] array){
		// Check first and last bytes
		if(array == null || array.length < 3 || array[0] != (byte)0xFE || array[array.length - 1] != (byte)0xFF)
			return null;
		
		if(array[1] == DatagramType.A.code)
			return StateDatagram.parse(array);
		if(array[1] == DatagramType.G.code)
			return RequestDatagram.parse(array);
		if(array[1] == DatagramType.L.code)
			return LedDatagram.parse(array);

		BezmaLog.i("PARSE", "NOT FOUND for symbol : " + array[1]);
		return WrongDatagram.parse(array);
	}
}
