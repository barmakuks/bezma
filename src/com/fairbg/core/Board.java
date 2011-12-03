package com.fairbg.core;
/** ������� ��������� �����, ������� ��������� ����� � ������ ���������, ��������� �����, ����
 * */
public class Board {
	/**���� ��� ����������� ����� �� �����*/
	private IBoardWindow _display = null;	
	/** ������� ������� �� �����*/
	private Position _current_position = null;
	/** ��������� �������� �����*/
	private MatchParameters _match_parameters = null;
	//private Lock _lock = new ReentrantLock(); 
	
	void setWindow(IBoardWindow window){
		_display = window;
		/*if(_display instanceof BoardWindow)
			_display.setPosition(_current_position);*/
	}
	void setPosition(Position position){
		_current_position = position;
		if(_display instanceof IBoardWindow){
			_display.setPosition(_current_position);
		}					
	}
	void setMatchParameters(MatchParameters params){
		_match_parameters = params;
		if(_display instanceof IBoardWindow){
			_display.setMatchParameters(params);
		}					
		
	}
}
