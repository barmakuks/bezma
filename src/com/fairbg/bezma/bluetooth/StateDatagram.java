package com.fairbg.bezma.bluetooth;

import java.util.HashMap;

import android.util.Log;

/**Датаграмма состояния доски*/
public class StateDatagram extends Datagram {
	
	/**Идентификатор устройства*/
	public int id;
	
	/**Карта датчиков на доске*/
	public final HashMap<Integer, Integer> sensors = new HashMap<Integer, Integer>();
	
	/** Check positions: 0 - empty; >0 - white; <0 - black
	 * positions[0] - black bar
	 * positions[25] - white bar
	 * */
	public final short[] positions = new short[28];
	
	/** Button pressed: 0-no button; >0 - white player; <0 - black player*/
	public short button = 0;	
	/**Is cube on  the board*/
	public boolean has_cube = true;
	/**Cube: 0 - cube in center; >0 - white player has cube; <0 - black player has cube*/
	public short cube = 0;
	
	@Override
	public DatagramType getDatagramType() {
		return DatagramType.A;
	}
	
	@Override
	public byte[] toByteArray() {
		byte[] buffer = createBuffer();
		buffer[2] = (byte) (id >> 24); 
		buffer[3] = (byte) ((id >> 16) & 0xFF); 
		buffer[4] = (byte) ((id >> 8) & 0xFF); 
		buffer[5] = (byte) (id & 0xFF);
		for(int i = 0;i < 131;i++){
			buffer[i * 2 + 5] = (byte)i;
			buffer[i*2 + 6] = 0;
		}
		return buffer;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("POSITION:\n");
		sb.append(String.format("%1$04d ", id));
		for(int i = 0 ; i < positions.length;i++){
			sb.append(String.format("%1$02d ", positions[i]));
		}
		return sb.toString();
	}
	
	public static Datagram parse(byte[] array){
		
		if(array.length != 269)
		{
			return WrongDatagram.parse(array);
		}
		
		StateDatagram dg = new StateDatagram();
		dg.id = (array[2] << 24) | (array[3] << 16) | (array[4] << 8) | (array[5]);
		for(int i = 6; i < array.length - 1 && (int)(array[i]) != (byte)0xFF ; i+=2){
			int index = 0xFF & array[i];
			int value = 0xFF & array[i+1];
			Log.i("Index + value", Integer.toString(index) + " : " + Integer.toString(value));
			dg.sensors.put(index, value);
		}
		parsePositions(dg);
		return dg;		
	}
	
	@Override
	protected int getBufferLength() {		
		return 269;
	}	

	/**Связь датчика на доске с соответсвующей позицией. В каждой позиции до 5 датчиков*/
	private static class Pair{
		public int begin;
		public int end;
		public int value;
		public Pair(int begin, int end, int index){
			if(begin > end){
				this.begin = end; this.end = begin;				
			}
			else{
				this.begin = begin; this.end = end;
			}
			this.value = index; 
			
		}
	} 
	/**Конкретная привязка датчиков к позиции*/
	private static final Pair[] _indexes = new Pair[]{
		new Pair(129, 134 , 0),
		new Pair(64, 68, 1),
		new Pair(69, 73, 2),
		new Pair(74, 78, 3),
		new Pair(79, 83, 4),
		new Pair(84, 88, 5),
		new Pair(89, 93, 6),
		new Pair(25, 29, 7),
		new Pair(20, 24, 8),
		new Pair(19, 15, 9),
		new Pair(10, 14, 10),
		new Pair(5, 9, 11),
		new Pair(0, 4, 12),
		new Pair(32, 36, 13),
		new Pair(37, 41, 14),
		new Pair(42, 46, 15),
		new Pair(47, 51, 16),
		new Pair(52, 56, 17),
		new Pair(57, 61, 18),
		new Pair(121, 125, 19),
		new Pair(116, 120, 20),
		new Pair(111, 115, 21),
		new Pair(106, 110, 22),
		new Pair(105, 101, 23),
		new Pair(100, 96, 24)
	};
	
	private static void parsePositions(StateDatagram dg){
		// Parse checker positions
		for(int i = 0 ; i < _indexes.length; i++){
			if(_indexes[i].value != 0){
				dg.positions[_indexes[i].value] = 0;
				for(int index = _indexes[i].begin; index <= _indexes[i].end ; index++){
					switch(dg.sensors.get(index)){
					case 2:
						dg.positions[_indexes[i].value]++;
						break;
					case 1:
						dg.positions[_indexes[i].value]--;
						break;
					}				
				}
			}
			else{
				dg.positions[0] = 0;
				dg.positions[25] = 0;
				for(int index = _indexes[0].begin; index < _indexes[0].end ; index++){
					//Log.i("INdex", Integer.toString(index));
					switch(dg.sensors.get(index)){
					case 2:
						dg.positions[25]++;
						break;
					case 1:
						dg.positions[0]--;
						break;
					}				
				}
				
			}
		}
		// Parse button
		dg.button = 0;
		if(dg.sensors.get(138) != 0)
			dg.button = -1;
		if(dg.sensors.get(139) != 0)
			dg.button = 1;
		// Parse cube
		dg.has_cube = false;
		dg.cube = 0;
		if(dg.sensors.get(135) != 0){
			dg.has_cube = true;
			dg.cube = 1;
		}
		if(dg.sensors.get(137) != 0){
			dg.has_cube = true;
			dg.cube = -1;
		}
		if(dg.sensors.get(136) != 0){
			dg.has_cube = true;
			dg.cube = 0;
		}
		
	}
}
