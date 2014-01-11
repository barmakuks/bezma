package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.core.backgammon.BackgammonRules;
import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.MovePrinter;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.core.model.ModelCore;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerId;

public class Runner
{
	class MoveData
	{
		int		  die1, die2;
		int		  position[];
		PlayerId player;

		MoveData(int p[], PlayerId pl)
		{
			die1 = die2 = 0;
			position = p;
			player = pl;
		}

		MoveData(int d1, int d2, int p[], PlayerId pl)
		{
			die1 = d1;
			die2 = d2;
			position = p;
			player = pl;
		}
	};

	// first 28 positions for checkers, 29th position for cube
	static short testGame[][] = {
		{ 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // NONE: start position
		{ 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // B: 1. [1:1] 8->7 7->6 6->5 6->5
        { 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0,-1 }, // W: double
        { 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 1 }, // B: take
		{ 0, 0, 1, 1, 0,-2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 1 }, // W: 2. [1:2] 24->23 24->22
		{ 1, 0, 1,-2, 0,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 1 }, // B: 3. [2:3] 5->3 6->3*
        { 0, 0, 1,-2, 1,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 1 }, // W: 4. [1:3] 25->24 24->21
        { 0, 0, 1,-2, 1,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0,-1 }, // B: double
        { 0, 0, 1,-2, 1,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 2 }, // W: take
		{ 1, 0,-2,-2, 1, 0,-2, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 2 }, // B: 5. [3:4] 5->2 6->2*
		{ 0, 0,-2,-2, 2, 0,-2, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 2 }, // W: 6. [1:3] 25->24 24->21
		{ 0, 0,-2,-2, 2, 0,-2, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-1, 0, 0, 0, 2 }, // B: 7. [5:6] 18->13 24->18
		{ 0, 0,-2,-2, 2, 0,-2, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-1, 0, 0, 0, 2 }, // W: 8. [1:1] 8->7 7->6 6->5 6->5
		{ 0, 0,-2,-2, 2, 0,-2, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-1, 0, 0, 0, 2 }, // B: 9. [2:3] 13->11 11->8
		{ 0, 0,-2,-2, 2, 0,-2, 0,-3, 0, 0, 0, 3,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, 2,-1, 0, 0, 2 }, // W: 10. [6:6] [6:6] 13->7 13->7 7->1* 7->1
		{ 0, 0,-2,-2, 2, 0,-2, 0,-3, 0, 0, 0, 3,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, 2,-1, 0, 0, 2 }, // B: 10. [6:6] skip move
		{ 0, 0,-2,-2, 2, 0,-2, 0,-3, 0, 0, 0, 1,-5, 0, 0, 0, 2, 0, 4, 2, 0, 2, 0, 2,-1, 0, 0, 2 }, // W: 11. [5:5] 13/8 13/8 8/3 8/3
								   };

	static short testGame1[][] = {
		{ 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // Start position

		{ 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // W
		{ 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0,-1,-1, 0, 0, 0, 0, 0 }, // B
		{ 0, 0, 1, 1, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0, 0 }, // W
		{ 1, 0, 1,-2, 0, 0,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0, 0 }, // B
		{ 0, 0, 0,-2, 2, 0,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 0, 0, 0, 0 }, // W
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2,-1, 4, 2, 0, 0,-1, 0, 0, 0, 0, 0 }, // B
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 4,-5, 0, 0, 0, 1, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0, 0 }, // W
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 4,-5, 0, 0, 0, 1, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0, 0 }, // B: skip move

		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0, 0 }, // W
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 4, 2, 0, 0,-1, 0,-1, 0, 0, 0 }, // B
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 2, 2, 2, 0,-1, 0,-1, 0, 0, 0 }, // W
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 3,-5, 0, 0, 0, 2, 2, 2, 2, 2,-2, 0, 0, 0, 0, 0, 0 }, // B
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0, 0, 0, 2,-5, 0, 1, 0, 2, 2, 2, 2, 2,-2, 0, 0, 0, 0, 0, 0 }, // W
		{ 0, 0, 0,-2, 2,-1,-3, 0,-2, 0,-1,-1, 2,-3, 0, 1, 0, 2, 2, 2, 2, 2,-2, 0, 0, 0, 0, 0, 0 }, // B
	};

	static short testGame2[][] = { // full game 
		{ 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // start position

		{ 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 3,-5, 1, 0, 0, 4, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // WHITE: 52: 13/8 13/11
		{ 0, 2, 0, 0, 0, 0,-5, 0,-4, 0, 0, 0, 3,-4, 1, 0, 0, 4, 0, 5, 0, 0, 0,-1,-1, 0, 0, 0, 0 },	// BLACK: 51: 24/23 13/8
		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 2,-4, 2, 0, 0, 4, 0, 3, 0, 2, 0,-1,-1, 0, 0, 0, 0 }, // WHITE: 22: 24/22 13/11 6/4 6/4
		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 2,-4, 2,-1, 0, 4, 0, 3, 0, 2, 0,-1, 0, 0, 0, 0, 0 }, // BLACK: 63: 24/18 18/15
		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 2, 1, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // WHITE: 53: 13/8 13/10*
		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 2, 1, 0, 5,-1, 3, 0, 2, 0,-1, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 1, 1, 0, 5, 0, 3, 0, 2, 0, 1, 0,-2, 0, 0, 0 }, // WHITE: 54: 11/7* 7/2*
		{ 1, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 1, 1, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // BLACK: 62: 25/23*
		{ 0, 1, 0, 2, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 0, 2, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // WHITE: 31: 25/22 11/10
		{ 0, 1, 0, 2, 0, 0,-5, 0,-4,-1, 0, 0, 0,-3, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 42: 25/23 13/9

		{ 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 1, 0, 0,-3, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 61: 22/16* 16/15
		{ 1, 1, 0, 1, 0, 0,-5, 0,-4, 0,-1, 0, 0,-2, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2,-1, 0, 0, 0, 0 }, // BLACK: 31: 25/24 13/10*
		{ 0, 0, 1, 2, 0, 0,-5, 0,-4, 0,-1, 0, 0,-2, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2,-1, 0, 0, 0, 0 }, // WHITE: 13: 25/22 24/23
		{ 0, 0, 1, 2, 0, 0,-5,-2,-4, 0,-1, 0,-1, 0, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 66: 24/18 18/12 13/7 13/7
		{ 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 36: 22/16 16/13*
		{ 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 2, 0, 5,-1, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
		{ 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 1, 0, 4, 1, 3, 0, 3, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 34: 10/7* 8/4
		{ 0, 0, 1, 1, 0, 0,-5,-3,-3, 0,-1, 0, 1, 0, 0, 1, 0, 4, 1, 3, 0, 3, 0,-3, 0, 0, 0, 0, 0 }, // BLACK: 21: 25/23 8/7
		{ 0, 0, 1, 1, 0, 0,-5,-3,-3, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // WHITE: 54: 8/3 7/3
		{ 1,-2,-2, 1, 0, 0,-5,-1,-1, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // BLACK: 66: 8/2* 8/2 7/1 7/1

		{ 0,-2,-2, 0, 2, 0,-5,-1,-1, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // WHITE: 41: 25/21 22/21
		{ 0,-2,-2,-1, 2, 0,-5,-1, 0, 0,-1, 0, 1, 0, 0, 1, 0, 3,-3, 3, 0, 3, 2, 0, 0, 0, 0, 0, 0 }, // BLACK: 55: 23/18 23/18 23/18 8/3
		{ 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 1, 0, 0, 1, 0, 1,-3, 3, 0, 3, 2, 2, 0,-1, 0, 0, 0 }, // WHITE: 66: 21/15* 21/15 8/2 8/2
		{ 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 1, 0, 0, 1, 0, 1,-3, 3, 0, 3, 2, 2, 0,-1, 0, 0, 0 }, // BLACK: 66: skip move
		{ 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-3, 3, 0, 4, 2, 2, 0,-1, 0, 0, 0 }, // WHITE: 65: 13/8 10/4
		{ 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 4, 2, 2, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
		{ 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 2, 3, 2, 1, 0, 0, 0, 0 }, // WHITE: 31: 4/1 4/3
		{ 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 2, 3, 2, 1, 0, 0, 0, 0 }, // BLACK: 42: 7/3 6/4
		{ 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 2, 0, 2, 2, 3, 2, 0, 0, 0, 0 }, // WHITE: 51: 6/1 3/2
		{ 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2,-1, 0, 0, 0, 0, 0, 2,-3, 2, 0, 2, 2, 3, 2, 0, 0, 0, 0 }, // BLACK: 61: 18/12 12/11

		{ 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2,-1, 0, 0, 0, 0, 0, 0,-3, 3, 1, 2, 2, 3, 2, 0, 0, 0, 0 }, // WHITE: 32: 8/5 8/6
		{ 0,-2,-2,-2,-2, 0,-3,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 3, 1, 2, 2, 3, 2, 0, 0, 0, 0 }, // BLACK: 42: 11/7 6/4
		{ 0,-2,-2,-2,-2, 0,-3,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 2, 4, 3, 0, 0, 0, 0 }, // WHITE: 53: 6/1 5/2
		{ 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 2, 4, 3, 0, 0, 0, 0 }, // BLACK: 51: 7/2 2/1
		{ 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 4, 4, 3, 0, 0, 0, 0 }, // WHITE: 66: 15/9 15/9 9/3 9/3
		{ 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0,-2, 2, 0, 2, 4, 4, 3, 0, 0, 0, 0 }, // BLACK: 51: 18/13 13/12
		{ 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0,-2, 1, 0, 1, 4, 4, 4, 0, 0, 0, 0 }, // WHITE: 63: 6/0 4/1
		{ 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0,-1, 1, 0, 1, 4, 4, 4, 0, 0, 0, 0 }, // BLACK: 62: 18/16 12/6
		{ 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0,-1, 1, 0, 0, 3, 4, 4, 0, 0, 0, 0 }, // WHITE: 43: 4/0 3/0
		{ 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 3, 4, 4, 0, 0, 0, 0 }, // BLACK: 61: 18/12 12/11
		
		{ 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // WHITE: 22: 2/0 2/0 2/0 2/0
		{ 0,-3,-2,-2,-3,-1,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // BLACK: 66: 16/10 11/5 10/4 6/0
		{ 0,-3,-2,-2,-3,-1,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // WHITE: 24: 6/4 4/0
		{ 0,-3,-1,-2,-3, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // BLACK: 52: 5/0 2/0
		{ 0,-3,-1,-2,-3, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0 }, // WHITE: 65: 3/0 3/0
	};
	
	static short[][] testGame3 = {
		{ 0,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5, 0, 0, 0, 0 },
		{ 0,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0, 0 },
		{ 0,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-2, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0, 0 },
		{ 0,-5, 0, 0, 0, 2, 0, 4, 2, 0,-1,-1, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0, 0 },
		{ 0,-5, 0, 0, 0, 2, 0, 2, 2, 0,-1, 2, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5,-1, 0, 0, 0 },
		{ 0,-5, 0, 0, 0, 2, 0, 2, 2, 0,-2, 2, 0, 2, 0, 0, 0,-2,-4, 0,-2, 0, 0, 0, 5, 0, 0, 0, 0 },
	};

    static short[][] testGame4 = { // Three games match
        // First game 
        { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // Start position
        
        { 0, 2, 0, 0, 0, -2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // B: 01:  
        { 0, 2, 0, 0, 0, -2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 1, 4, 0, 1, 0, 0,-2, 0, 0, 0, 0 }, // W: 02:
        { 0, 2, 0, 0, 0, -2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 1, 4, 0, 1, 0, 0,-2, 0, 0, 0,-1 }, // B: double
        { 0, 2, 0, 0, 0, -2,-4, 0,-2, 0, 0, 0, 5,-5, 0, 0, 0, 2, 1, 4, 0, 1, 0, 0,-2, 0, 0, 0, 0 }, // W: pass

        // Second game
        { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // Start position

        { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-2, 0, 0, 0, 0 }, // W:
        { 0, 2, 0, 0, 0, 0,-5,-2,-2, 0, 0, 0, 5,-4, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0,-2, 0, 0, 0, 0 }, // B:
        { 0, 2, 0, 0, 0, 0,-5,-2,-2, 0, 0, 0, 5,-4, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0, 0 }, // W:
        { 0, 2, 0, 0, 0, 0,-5,-2,-2, 0, 0, 0, 5,-4, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0,-1 }, // B: double
        { 0, 2, 0, 0, 0, 0,-5,-2,-2, 0, 0, 0, 5,-4, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0, 2 }, // W: take
        { 0, 2, 0, 0, 0, 0,-5,-2,-2,-2, 0, 0, 5,-2, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0, 2 }, // B:
        { 0, 2, 0, 0, 0, 0,-5,-2,-2,-2, 0, 0, 5,-2, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0,-1 }, // W: double
        { 0, 2, 0, 0, 0, 0,-5,-2,-2,-2, 0, 0, 5,-2, 0, 0, 0, 1, 0, 3, 2, 2, 0, 0,-2, 0, 0, 0, 0 }, // B: pass
        
        // Third game
        { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 5,-5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // start position

        { 0, 2, 0, 0, 0, 0,-5, 0,-3, 0, 0, 0, 3,-5, 1, 0, 0, 4, 0, 5, 0, 0, 0, 0,-2, 0, 0, 0, 0 }, // WHITE: 52: 13/8 13/11
        { 0, 2, 0, 0, 0, 0,-5, 0,-4, 0, 0, 0, 3,-4, 1, 0, 0, 4, 0, 5, 0, 0, 0,-1,-1, 0, 0, 0, 0 },  // BLACK: 51: 24/23 13/8
        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 2,-4, 2, 0, 0, 4, 0, 3, 0, 2, 0,-1,-1, 0, 0, 0, 0 }, // WHITE: 22: 24/22 13/11 6/4 6/4
        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 2,-4, 2,-1, 0, 4, 0, 3, 0, 2, 0,-1, 0, 0, 0, 0, 0 }, // BLACK: 63: 24/18 18/15
        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 2, 1, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // WHITE: 53: 13/8 13/10*
        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 2, 1, 0, 5,-1, 3, 0, 2, 0,-1, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 1, 1, 0, 5, 0, 3, 0, 2, 0, 1, 0,-2, 0, 0, 0 }, // WHITE: 54: 11/7* 7/2*
        { 1, 1, 0, 1, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 1, 1, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // BLACK: 62: 25/23*
        { 0, 1, 0, 2, 0, 0,-5, 0,-4, 0, 0, 0, 0,-4, 0, 2, 0, 5, 0, 3, 0, 2, 0,-1, 0,-1, 0, 0, 0 }, // WHITE: 31: 25/22 11/10
        { 0, 1, 0, 2, 0, 0,-5, 0,-4,-1, 0, 0, 0,-3, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 42: 25/23 13/9

        { 0, 1, 0, 1, 0, 0,-5, 0,-4, 0, 1, 0, 0,-3, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 61: 22/16* 16/15
        { 1, 1, 0, 1, 0, 0,-5, 0,-4, 0,-1, 0, 0,-2, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2,-1, 0, 0, 0, 0 }, // BLACK: 31: 25/24 13/10*
        { 0, 0, 1, 2, 0, 0,-5, 0,-4, 0,-1, 0, 0,-2, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2,-1, 0, 0, 0, 0 }, // WHITE: 13: 25/22 24/23
        { 0, 0, 1, 2, 0, 0,-5,-2,-4, 0,-1, 0,-1, 0, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 66: 24/18 18/12 13/7 13/7
        { 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 2, 0, 5, 0, 3, 0, 2, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 36: 22/16 16/13*
        { 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 2, 0, 5,-1, 3, 0, 2, 0,-2, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
        { 0, 0, 1, 1, 0, 0,-5,-2,-4, 0,-1, 0, 1, 0, 0, 1, 0, 4, 1, 3, 0, 3, 0,-2, 0,-1, 0, 0, 0 }, // WHITE: 34: 10/7* 8/4
        { 0, 0, 1, 1, 0, 0,-5,-3,-3, 0,-1, 0, 1, 0, 0, 1, 0, 4, 1, 3, 0, 3, 0,-3, 0, 0, 0, 0, 0 }, // BLACK: 21: 25/23 8/7
        { 0, 0, 1, 1, 0, 0,-5,-3,-3, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // WHITE: 54: 8/3 7/3
        { 1,-2,-2, 1, 0, 0,-5,-1,-1, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // BLACK: 66: 8/2* 8/2 7/1 7/1

        { 0,-2,-2, 0, 2, 0,-5,-1,-1, 0,-1, 0, 1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2,-3, 0, 0, 0, 0, 0 }, // WHITE: 41: 25/21 22/21
        { 0,-2,-2,-1, 2, 0,-5,-1, 0, 0,-1, 0, 1, 0, 0, 1, 0, 3,-3, 3, 0, 3, 2, 0, 0, 0, 0, 0, 0 }, // BLACK: 55: 23/18 23/18 23/18 8/3
        { 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 1, 0, 0, 1, 0, 1,-3, 3, 0, 3, 2, 2, 0,-1, 0, 0, 0 }, // WHITE: 66: 21/15* 21/15 8/2 8/2
        { 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 1, 0, 0, 1, 0, 1,-3, 3, 0, 3, 2, 2, 0,-1, 0, 0, 0 }, // BLACK: 66: skip move
        { 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-3, 3, 0, 4, 2, 2, 0,-1, 0, 0, 0 }, // WHITE: 65: 13/8 10/4
        { 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 4, 2, 2, 0, 0, 0, 0, 0 }, // BLACK: 61: 25/24 24/18
        { 0,-2,-2,-1, 0, 0,-5,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 2, 3, 2, 1, 0, 0, 0, 0 }, // WHITE: 31: 4/1 4/3
        { 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 3, 0, 2, 3, 2, 1, 0, 0, 0, 0 }, // BLACK: 42: 7/3 6/4
        { 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2,-4, 2, 0, 2, 2, 3, 2, 0, 0, 0, 0 }, // WHITE: 51: 6/1 3/2
        { 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2,-1, 0, 0, 0, 0, 0, 2,-3, 2, 0, 2, 2, 3, 2, 0, 0, 0, 0 }, // BLACK: 61: 18/12 12/11

        { 0,-2,-2,-2,-1, 0,-4, 0, 0, 0, 2,-1, 0, 0, 0, 0, 0, 0,-3, 3, 1, 2, 2, 3, 2, 0, 0, 0, 0 }, // WHITE: 32: 8/5 8/6
        { 0,-2,-2,-2,-2, 0,-3,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 3, 1, 2, 2, 3, 2, 0, 0, 0, 0 }, // BLACK: 42: 11/7 6/4
        { 0,-2,-2,-2,-2, 0,-3,-1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 2, 4, 3, 0, 0, 0, 0 }, // WHITE: 53: 6/1 5/2
        { 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 2, 4, 3, 0, 0, 0, 0 }, // BLACK: 51: 7/2 2/1
        { 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-3, 2, 0, 2, 4, 4, 3, 0, 0, 0, 0 }, // WHITE: 66: 15/9 15/9 9/3 9/3
        { 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0,-2, 2, 0, 2, 4, 4, 3, 0, 0, 0, 0 }, // BLACK: 51: 18/13 13/12
        { 0,-3,-2,-2,-2, 0,-3, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0,-2, 1, 0, 1, 4, 4, 4, 0, 0, 0, 0 }, // WHITE: 63: 6/0 4/1
        { 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0,-1, 1, 0, 1, 4, 4, 4, 0, 0, 0, 0 }, // BLACK: 62: 18/16 12/6
        { 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0,-1, 1, 0, 0, 3, 4, 4, 0, 0, 0, 0 }, // WHITE: 43: 4/0 3/0
        { 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 3, 4, 4, 0, 0, 0, 0 }, // BLACK: 61: 18/12 12/11
        
        { 0,-3,-2,-2,-2, 0,-4, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // WHITE: 22: 2/0 2/0 2/0 2/0
        { 0,-3,-2,-2,-3,-1,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // BLACK: 66: 16/10 11/5 10/4 6/0
        { 0,-3,-2,-2,-3,-1,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // WHITE: 24: 6/4 4/0
        { 0,-3,-1,-2,-3, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0, 0 }, // BLACK: 52: 5/0 2/0
        { 0,-3,-1,-2,-3, 0,-3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0 }, // WHITE: 65: 3/0 3/0        *******
        { 0,-3,-1,-2,-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0 }, // BLACK: 66: 6/0 6/0 6/0 4/0 
        { 0,-3,-1,-2,-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 }, // WHITE: 12: 3/1 1/0 
        { 0,-3,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0 }, // BLACK: 66: 4/0 4/0 3/0 3/0 
        { 0,-3,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 }, // WHITE: 21: 1/0 1/0 
        { 0,-3,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,-1 }, // BLACK: double 
        { 0,-3,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2 }, // WHITE: take 
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2 }, // BLACK: 22: 2/0 1/0 1/0 1/0 

        
    };
	
	void testFindMove(MoveData testData[])
	{
		System.out.println("*********** test find move ********");
		BackgammonRules rules = new BackgammonRules();

		Position from = new Position();
		from.setCheckers(testData[0].position);

		Position to = new Position();

		for (int i = 1; i < testData.length; i++)
		{
			to.setCheckers(testData[i].position);

			Move move = rules.findMove(testData[i].die1, testData[i].die2,
					from, to, testData[i].player);

			if (move != null)
			{
				System.out.print(String.format("%2d: ", i));
				System.out.println(MovePrinter.printMove(move));
				from.applyMove(move);
			} else
			{
				System.out.println("Move " + i + " not found");
			}
		}
	}

	void testModel(MoveData testData[])
	{
		System.out.println("*********** test model **************");
		ModelCore model = new ModelCore();
		model.create(null, null, null);

		CommunicationCommandState commCommand = new CommunicationCommandState();
		ModelCommand command = ModelCommand.createCommand(commCommand);

		int i = 0;

		for (MoveData testMove : testData)
		{
			command.getPosition().setCheckers(testMove.position);
			command.player = testMove.player;

			if (!model.acceptCommand(command))
			{
				System.out.println("Move " + i + " not found");
				model.acceptCommand(command);
				return;
			} else
			{
				System.out.println("Move " + i + " found");
			}
			i++;
		}
	}


	public static short[][] getDatagrams()
	{
		return testGame4;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// BezmaLog.allowTag("MOVE");
		IModelView view = new TestModelOut();
		IModelView commandsProvider = new TestModelCommandsProvider(getDatagrams(), 200);

		TestConfiguration configuration = new TestConfiguration();

		configuration.setMatchParameters(new MatchParameters());

		TestConfigurator configurator = new TestConfigurator();

		Presenter presenter = new Presenter(configurator, configuration);

		presenter.addView(view);
		presenter.addView(commandsProvider);

		presenter.start();
	}

}
