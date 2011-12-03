package com.fairbg.core;

import com.fairbg.core.commands.UserCommand;
import com.fairbg.core.commands.ICommandListener;
import com.fairbg.core.commands.ICommander;

public class Corel implements ICommandListener{
	private IBoardWindow _window = null;
	private Board _board = null;
	private Automat _automat = null;
	private Match _match = null;	
	private UsbCommander _usbCommander = null;
	
	/** Устанавливает окно для отображения матча
	 * @param window окно отображения матча
	 */
	public void setBoardWindow(IBoardWindow window){
		_window = window;
		_window.addListener(this);
		_board.setWindow(window);		
	}
	private Corel(){
		_usbCommander = new UsbCommander(this);
		_board = new Board();
	}
	
	@Override
	public void onCommand(ICommander commander, UserCommand command) {
		_board.setPosition(Position.getRandomPosition());				
	}
	public void startListenUsb(){
		_usbCommander.start();
	}
	public void stopListenUsb(){
		_usbCommander.stop();
	}

	public static final Corel corel = new Corel();
}
