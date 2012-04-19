package com.fairbg.bezma.bluetooth;

import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.communication.commands.UserCommandLed;
import com.fairbg.bezma.communication.commands.UserCommandRequest;
import com.fairbg.bezma.communication.commands.UserCommandState;
import com.fairbg.bezma.core.model.ModelState;

/** Класс-конвертор. Конвертирует датаграммы посылаемые внешними устройствами во внутренние команды и наоборот.
 * Используются только статические функции, Объект класса не может быть создан
 * */
final public class DatagramConverter {
	
	private DatagramConverter ()
	{
		
	}
	
	/**Конвертирует команду в датаграмму */
	public static Datagram commandToDatagram(UserCommand command)
	{		
		if (command instanceof UserCommandRequest)
		{
			return new RequestDatagram();
		}
		
		if (command instanceof UserCommandLed)
		{
			UserCommandLed led_command = (UserCommandLed)command;
			LedDatagram dg = new LedDatagram();
			dg.button = led_command.button;
			dg.state = led_command.state;
			System.out.println("LED : " + Byte.toString(dg.button) + " STATE : " + Byte.toString(dg.state));
			return dg;
		}
		return null;
	}
	
	/**Конвертирует датаграмму в команду*/
	public static UserCommand datagramToCommand(Datagram datagram)	
	{
		switch (datagram.getDatagramType()){
		case A:
			UserCommandState state_cmd = new UserCommandState();
			StateDatagram st_datagram = (StateDatagram) datagram;
			state_cmd.playerId = st_datagram.button;
			state_cmd.cubePosition = st_datagram.cube;
			for(int i = 0; i < st_datagram.positions.length; i++)
			{
				state_cmd.checkers[i] = st_datagram.positions[i];				
			}
			return state_cmd;
		case L:
			UserCommandLed led_cmd = new UserCommandLed();
			LedDatagram led_datagram = (LedDatagram) datagram;
			led_datagram.button = led_cmd.button;
			led_datagram.state = led_cmd.state;
			return led_cmd;
		}
		return null;
	}
	
	/**Возвращает массив датаграм для отображения текущего состояния модели на внешнем устройстве*/
	public static Datagram[] modelStateToDatagrams(ModelState modelState)
	{
		return null;
	} 
}
