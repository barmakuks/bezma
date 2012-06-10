package com.fairbg.bezma.core.model;

import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.log.BezmaLog;

/// описание команд используемых моделью
public class ModelCommand {

	/** player who sent command*/
	public PlayerColors player;
	
	private Position m_Position = new Position();
	
	
	/// Creates Model Command from Communication command
	public static ModelCommand createCommand(CommunicationCommand command) {
		
		BezmaLog.i("COMMUNICATION TO COMMAND", "Convertation");
		
		ModelCommand modelCommand = new ModelCommand();
		if (command instanceof CommunicationCommandState)
		{
			CommunicationCommandState stateCommand  = (CommunicationCommandState) command;
			
			modelCommand.player = (stateCommand.playerId == 0) ? 
					PlayerColors.NONE :	(stateCommand.playerId > 0) ? 
										PlayerColors.BLACK : PlayerColors.WHITE;
			
			int [] pos = modelCommand.m_Position.getCheckers(); 
			
			for(int i =0 ; i < stateCommand.checkers.length ; i++)
			{
				pos[i] = stateCommand.checkers[i];				
			}		
			
		}
		
		return modelCommand;
	}

	public Position getPosition() {
		return m_Position;
	}

}
