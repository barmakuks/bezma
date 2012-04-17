package com.fairbg.core.model;

import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.communication.commands.UserCommandState;
import com.fairbg.bezma.communication.commands.UserCommandWrongData;

/**
 * Класс описывающий текущее состояние модели
 * @todo Переместить туда, где будет находиться модель
 * */
public class ModelState {
	
	/**Текущая позиция на доске*/
	private Position _position = null;
	/**Последнее сообщение об ошибке*/
	private String _error_msg = "";

	/**Функция обработки пользовательской команды
	 * @todo переместить эту функцию туда, где будет устанавливаться новое состояние модели
	 * */
	public void processCommand(UserCommand userCommand) {
		
		if (userCommand instanceof UserCommandState) {
		
			_position = new Position();
			_error_msg = "";
			int[] pos = _position.getPosiiton();
			short[] c_pos = ((UserCommandState) userCommand).checkers;
			
			for (int i = 0; i < c_pos.length; i++) {
				pos[i] = c_pos[i];
			}
		}
		
		if (userCommand instanceof UserCommandWrongData)
		{
			_error_msg = ((UserCommandWrongData)userCommand).message;
		}
	}

	/**Возвращает текущую позицию на доске*/
	public Position getCurrentPosition() {
		return _position;
	}
	/**Возвращает последнее сообщение об ошибке*/
	public String getErrorMessage(){
		return _error_msg;
	}
	/**Возвращает TRUE, если в процессе обработки пользовательской команды возникла ошибка*/
	public boolean isErrorState()
	{
		return _error_msg != "";
	}
}
