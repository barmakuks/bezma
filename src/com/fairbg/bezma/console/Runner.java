package com.fairbg.bezma.console;

import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.core.model.Model;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.log.ConsoleLogger;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Log.i("START", "CONSOLE");
		
		BezmaLog.setLogger(new ConsoleLogger());
		BezmaLog.allowTag("MOVE");
		
		Model model = new Model();
		model.create(null, null);
		
		CommunicationCommandState commCommand = new CommunicationCommandState();
		ModelCommand command = ModelCommand.createCommand(commCommand);	

		class Move{
			int move[];
			PlayerColors player;
			Move(int m[], PlayerColors pl) {move = m; player = pl;}
		};
		
/*		Move moves[] = {
				new Move(new int[]{0,	2,0,0,0,0,-5, 	0,-3,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.NONE), // start position
				new Move(new int[]{0,	2,0,0,0,-2,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.BLACK),
				new Move(new int[]{0,	0,1,1,0,-2,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.WHITE),
				new Move(new int[]{2,	0,-1,0,0,-1,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.BLACK),
				//new Move(new int[]{0,	0,-1,1,1,-1,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.WHITE),
				};
				*/
		Move moves[] = {
				new Move(new int[]{0,	2,0,0,0,0,-5, 	0,-3,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.NONE), // start position
				new Move(new int[]{0,	2,0,0,-1,-1,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	0,0,0}, PlayerColors.BLACK),
				new Move(new int[]{0,	1,0,0,0,1,-4,	0,-2,0,0,0,5,	-5,0,0,0,3,0,	5,0,0,0,0,-2,	-2,0,0}, PlayerColors.WHITE),
				};
		
		//int start[] = { 0,5,0,0,0,-3,0,-5,0,0,0,0,2,-2,0,0,0,0,5,0,3,0,0,0,-5,0,0,0};
		for (Move move : moves)
		{
			command.getPosition().setCheckers(move.move);
			command.player = move.player;
			if (! model.acceptCommand(command))
			{
				BezmaLog.allowTag("FOUND STATE");
				model.acceptCommand(command);
				BezmaLog.disableTag("FOUND STATE");				
				BezmaLog.i("MOVE", "MOVE NOT FOUND");
			}
			
		}

	}

}
