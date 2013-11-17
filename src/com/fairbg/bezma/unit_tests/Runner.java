package com.fairbg.bezma.unit_tests;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.core.backgammon.BackgammonRules;
import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.Movement;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.Model;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerColors;

class MovePrinter implements IMoveVisitor
{

    @Override
    public void visit(Move move)
    {
	System.out
		.print(move.getPlayer() == PlayerColors.WHITE ? "W: " : "B: ");
	System.out.print("[" + move.getDie1() + ":" + move.getDie2() + "]");

	if (move.getMovements().length == 0)
	{
	    System.out.print(" skip move");
	} else
	{
	    for (Movement movement : move.getMovements())
	    {
		movement.accept(this);
	    }
	}

	System.out.println();
    }

    @Override
    public void visit(Movement movement)
    {
	System.out.print(" " + movement.MoveFrom + "/"
		+ (movement.MoveTo > 0 ? movement.MoveTo : "off"));
	System.out.print(movement.Strike ? "* " : " ");
    }
};

public class Runner
{
    private class MoveData
    {
	int die1, die2;
	int position[];
	PlayerColors player;

	MoveData(int p[], PlayerColors pl)
	{
	    die1 = die2 = 0;
	    position = p;
	    player = pl;
	}

	MoveData(int d1, int d2, int p[], PlayerColors pl)
	{
	    die1 = d1;
	    die2 = d2;
	    position = p;
	    player = pl;
	}
    };

    private MoveData fakeGame[] = {
	    new MoveData(new int[] { 0, 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.NONE), // 0. start position
	    new MoveData(new int[] { 0, 2, 0, 0, 0, -2, -4, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.BLACK), // 1. [1:1] 8->7 7->6 6->5 6->5
	    new MoveData(new int[] { 0, 0, 1, 1, 0, -2, -4, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.WHITE), // 2. [1:2] 24->23 24->22
	    new MoveData(new int[] { 1, 0, 1, -2, 0, -1, -3, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.BLACK), // 3. [2:3] 5->3 6->3 *
	    new MoveData(new int[] { 0, 0, 1, -2, 1, -1, -3, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.WHITE), // 4. [1:3] 25->24 24->21
	    new MoveData(new int[] { 1, 0, -2, -2, 1, 0, -2, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.BLACK), // 5. [3:4] 5->2 6->2 *
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.WHITE), // 6. [1:3] 25->24 24->21
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -1, 0, 0, 0 },
		    PlayerColors.BLACK), // 7. [5:6] 18->13 24->18
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -2, 0, 0, 0, 5,
		    -5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, -1, 0, 0, 0 },
		    PlayerColors.WHITE), // 8. [1:1] 8->7 7->6 6->5 6->5
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -3, 0, 0, 0, 5,
		    -5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, -1, 0, 0, 0 },
		    PlayerColors.BLACK), // 9. [2:3] 13->11 11->8
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -3, 0, 0, 0, 3,
		    -5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, 2, -1, 0, 0 },
		    PlayerColors.WHITE), // 10. [6:6] [6:6] 13->7 13->7 7->1 *
					 // 7->1
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -3, 0, 0, 0, 3,
		    -5, 0, 0, 0, 2, 0, 4, 2, 0, 0, 0, 2, -1, 0, 0 },
		    PlayerColors.BLACK), // 10. [6:6] skip move
	    new MoveData(new int[] { 0, 0, -2, -2, 2, 0, -2, 0, -3, 0, 0, 0, 1,
		    -5, 0, 0, 0, 2, 0, 4, 2, 0, 2, 0, 2, -1, 0, 0 },
		    PlayerColors.WHITE), // 11. [5:5] 13/8 13/8 8/3 8/3
    };

    private MoveData fornazirKrasovGame2[] = {
	    // черные - отрицательные, движение влево
	    // - BLACK |W| | 1| 2| 3| 4| 5| 6| | 7| 8| 9|10|11|12|
	    // |13|14|15|16|17|18| |19|20|21|22|23|24| | B|
	    // - WHITE |W| |24|23|22|21|20|19| |18|17|16|15|14|13| |12|11|10| 9|
	    // 8| 7| | 6| 5| 4| 3| 2| 1| | B|
	    new MoveData(new int[] { 0, 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5,
		    -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.NONE), // start position

	    new MoveData(new int[] { 0, 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 3,
		    -5, 1, 0, 0, 4, 0, 5, 0, 0, 0, 0, -2, 0, 0, 0 },
		    PlayerColors.WHITE), // 52: 13/8 13/11
	    new MoveData(new int[] { 0, 2, 0, 0, 0, 0, -5, 0, -4, 0, 0, 0, 3,
		    -4, 1, 0, 0, 4, 0, 5, 0, 0, 0, -1, -1, 0, 0, 0 },
		    PlayerColors.BLACK), // 51: 24/23 13/8
	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 2,
		    -4, 2, 0, 0, 4, 0, 3, 0, 2, 0, -1, -1, 0, 0, 0 },
		    PlayerColors.WHITE), // 22: 24/22 13/11 6/4 6/4
	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 2,
		    -4, 2, -1, 0, 4, 0, 3, 0, 2, 0, -1, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 63: 24/18 18/15
	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 0,
		    -4, 2, 1, 0, 5, 0, 3, 0, 2, 0, -1, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 53: 13/8 13/10*
	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 0,
		    -4, 2, 1, 0, 5, -1, 3, 0, 2, 0, -1, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 61: 25/24 24/18
	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 0,
		    -4, 1, 1, 0, 5, 0, 3, 0, 2, 0, 1, 0, -2, 0, 0 },
		    PlayerColors.WHITE), // 54: 11/7* 7/2*
	    new MoveData(new int[] { 1, 1, 0, 1, 0, 0, -5, 0, -4, 0, 0, 0, 0,
		    -4, 1, 1, 0, 5, 0, 3, 0, 2, 0, -1, 0, -1, 0, 0 },
		    PlayerColors.BLACK), // 62: 25/23*
	    new MoveData(new int[] { 0, 1, 0, 2, 0, 0, -5, 0, -4, 0, 0, 0, 0,
		    -4, 0, 2, 0, 5, 0, 3, 0, 2, 0, -1, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 31: 25/22 11/10
	    new MoveData(new int[] { 0, 1, 0, 2, 0, 0, -5, 0, -4, -1, 0, 0, 0,
		    -3, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 42: 25/23 13/9

	    new MoveData(new int[] { 0, 1, 0, 1, 0, 0, -5, 0, -4, 0, 1, 0, 0,
		    -3, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 61: 22/16* 16/15
	    new MoveData(new int[] { 1, 1, 0, 1, 0, 0, -5, 0, -4, 0, -1, 0, 0,
		    -2, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, -1, 0, 0, 0 },
		    PlayerColors.BLACK), // 31: 25/24 13/10*
	    new MoveData(new int[] { 0, 0, 1, 2, 0, 0, -5, 0, -4, 0, -1, 0, 0,
		    -2, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, -1, 0, 0, 0 },
		    PlayerColors.WHITE), // 13: 25/22 24/23
	    new MoveData(new int[] { 0, 0, 1, 2, 0, 0, -5, -2, -4, 0, -1, 0,
		    -1, 0, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 66: 24/18 18/12 13/7 13/7
	    new MoveData(new int[] { 0, 0, 1, 1, 0, 0, -5, -2, -4, 0, -1, 0, 1,
		    0, 0, 2, 0, 5, 0, 3, 0, 2, 0, -2, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 36: 22/16 16/13*
	    new MoveData(new int[] { 0, 0, 1, 1, 0, 0, -5, -2, -4, 0, -1, 0, 1,
		    0, 0, 2, 0, 5, -1, 3, 0, 2, 0, -2, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 61: 25/24 24/18
	    new MoveData(new int[] { 0, 0, 1, 1, 0, 0, -5, -2, -4, 0, -1, 0, 1,
		    0, 0, 1, 0, 4, 1, 3, 0, 3, 0, -2, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 34: 10/7* 8/4
	    new MoveData(new int[] { 0, 0, 1, 1, 0, 0, -5, -3, -3, 0, -1, 0, 1,
		    0, 0, 1, 0, 4, 1, 3, 0, 3, 0, -3, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 21: 25/23 8/7
	    new MoveData(new int[] { 0, 0, 1, 1, 0, 0, -5, -3, -3, 0, -1, 0, 1,
		    0, 0, 1, 0, 3, 0, 3, 0, 3, 2, -3, 0, 0, 0, 0 },
		    PlayerColors.WHITE), // 54: 8/3 7/3
	    new MoveData(new int[] { 1, -2, -2, 1, 0, 0, -5, -1, -1, 0, -1, 0,
		    1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2, -3, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 66: 8/2* 8/2 7/1 7/1

	    new MoveData(new int[] { 0, -2, -2, 0, 2, 0, -5, -1, -1, 0, -1, 0,
		    1, 0, 0, 1, 0, 3, 0, 3, 0, 3, 2, -3, 0, 0, 0, 0 },
		    PlayerColors.WHITE), // 41: 25/21 22/21
	    new MoveData(new int[] { 0, -2, -2, -1, 2, 0, -5, -1, 0, 0, -1, 0,
		    1, 0, 0, 1, 0, 3, -3, 3, 0, 3, 2, 0, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 55: 23/18 23/18 23/18 8/3
	    new MoveData(new int[] { 0, -2, -2, -1, 0, 0, -5, -1, 0, 0, 2, 0,
		    1, 0, 0, 1, 0, 1, -3, 3, 0, 3, 2, 2, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 66: 21/15* 21/15 8/2 8/2
	    new MoveData(new int[] { 0, -2, -2, -1, 0, 0, -5, -1, 0, 0, 2, 0,
		    1, 0, 0, 1, 0, 1, -3, 3, 0, 3, 2, 2, 0, -1, 0, 0 },
		    PlayerColors.BLACK), // 66: skip move
	    new MoveData(new int[] { 0, -2, -2, -1, 0, 0, -5, -1, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 2, -3, 3, 0, 4, 2, 2, 0, -1, 0, 0 },
		    PlayerColors.WHITE), // 65: 13/8 10/4
	    new MoveData(new int[] { 0, -2, -2, -1, 0, 0, -5, -1, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 2, -4, 3, 0, 4, 2, 2, 0, 0, 0, 0 },
		    PlayerColors.BLACK), // 61: 25/24 24/18
	    new MoveData(new int[] { 0, -2, -2, -1, 0, 0, -5, -1, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 2, -4, 3, 0, 2, 3, 2, 1, 0, 0, 0 },
		    PlayerColors.WHITE), // 31: 4/1 4/3
	    new MoveData(new int[] { 0, -2, -2, -2, -1, 0, -4, 0, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 2, -4, 3, 0, 2, 3, 2, 1, 0, 0, 0 },
		    PlayerColors.BLACK), // 42: 7/3 6/4
	    new MoveData(new int[] { 0, -2, -2, -2, -1, 0, -4, 0, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 2, -4, 2, 0, 2, 2, 3, 2, 0, 0, 0 },
		    PlayerColors.WHITE), // 51: 6/1 3/2
	    new MoveData(new int[] { 0, -2, -2, -2, -1, 0, -4, 0, 0, 0, 2, -1,
		    0, 0, 0, 0, 0, 2, -3, 2, 0, 2, 2, 3, 2, 0, 0, 0 },
		    PlayerColors.BLACK), // 61: 18/12 12/11
	    // - BLACK |W| | 1| 2| 3| 4| 5| 6| | 7| 8| 9|10|11|12|
	    // |13|14|15|16|17|18| |19|20|21|22|23|24| | B|
	    // - WHITE |W| |24|23|22|21|20|19| |18|17|16|15|14|13| |12|11|10| 9|
	    // 8| 7| | 6| 5| 4| 3| 2| 1| | B|
	    new MoveData(new int[] { 0, -2, -2, -2, -1, 0, -4, 0, 0, 0, 2, -1,
		    0, 0, 0, 0, 0, 0, -3, 3, 1, 2, 2, 3, 2, 0, 0, 0 },
		    PlayerColors.WHITE), // 32: 8/5 8/6
	    new MoveData(new int[] { 0, -2, -2, -2, -2, 0, -3, -1, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 0, -3, 3, 1, 2, 2, 3, 2, 0, 0, 0 },
		    PlayerColors.BLACK), // 42: 11/7 6/4
	    new MoveData(new int[] { 0, -2, -2, -2, -2, 0, -3, -1, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 0, -3, 2, 0, 2, 2, 4, 3, 0, 0, 0 },
		    PlayerColors.WHITE), // 53: 6/1 5/2
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -3, 0, 0, 0, 2, 0,
		    0, 0, 0, 0, 0, 0, -3, 2, 0, 2, 2, 4, 3, 0, 0, 0 },
		    PlayerColors.BLACK), // 51: 7/2 2/1
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -3, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, 0, 0, -3, 2, 0, 2, 4, 4, 3, 0, 0, 0 },
		    PlayerColors.WHITE), // 66: 15/9 15/9 9/3 9/3
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -3, 0, 0, 0, 0, 0,
		    -1, 0, 0, 0, 0, 0, -2, 2, 0, 2, 4, 4, 3, 0, 0, 0 },
		    PlayerColors.BLACK), // 51: 18/13 13/12
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -3, 0, 0, 0, 0, 0,
		    -1, 0, 0, 0, 0, 0, -2, 1, 0, 1, 4, 4, 4, 0, 0, 0 },
		    PlayerColors.WHITE), // 63: 6/0 4/1
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -4, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, -1, 0, -1, 1, 0, 1, 4, 4, 4, 0, 0, 0 },
		    PlayerColors.BLACK), // 62: 18/16 12/6
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -4, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, -1, 0, -1, 1, 0, 0, 3, 4, 4, 0, 0, 0 },
		    PlayerColors.WHITE), // 43: 4/0 3/0
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -4, 0, 0, 0, 0, -1,
		    0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 3, 4, 4, 0, 0, 0 },
		    PlayerColors.BLACK), // 61: 18/12 12/11
	    // - BLACK |W| | 1| 2| 3| 4| 5| 6| | 7| 8| 9|10|11|12|
	    // |13|14|15|16|17|18| |19|20|21|22|23|24| | B|
	    // - WHITE |W| |24|23|22|21|20|19| |18|17|16|15|14|13| |12|11|10| 9|
	    // 8| 7| | 6| 5| 4| 3| 2| 1| | B|
	    new MoveData(new int[] { 0, -3, -2, -2, -2, 0, -4, 0, 0, 0, 0, -1,
		    0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0 },
		    PlayerColors.WHITE), // 22: 2/0 2/0 2/0 2/0
	    new MoveData(new int[] { 0, -3, -2, -2, -3, -1, -3, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 4, 0, 0, 0 },
		    PlayerColors.BLACK), // 66: 16/10 11/5 10/4 6/0
	    new MoveData(new int[] { 0, -3, -2, -2, -3, -1, -3, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0 },
		    PlayerColors.WHITE), // 24: 6/4 4/0
	    new MoveData(new int[] { 0, -3, -1, -2, -3, 0, -3, 0, 0, 0, 0, 0,
		    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 4, 0, 0, 0 },
		    PlayerColors.BLACK), // 52: 5/0 2/0
	    new MoveData(6, 5, new int[] { 0, -3, -1, -2, -3, 0, -3, 0, 0, 0,
		    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0 },
		    PlayerColors.WHITE), // 65: 3/0 3/0

    };

    private void testFindMove(MoveData testData[])
    {
	System.out.println("*********** test find move ********");
	BackgammonRules rules = new BackgammonRules();
	MovePrinter movePrinter = new MovePrinter();

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
		move.accept(movePrinter);
		from.applyMove(move);
	    } else
	    {
		System.out.println("Move " + i + " not found");
	    }
	}

    }

    private void testModel(MoveData testData[])
    {
	System.out.println("*********** test model **************");
	Model model = new Model();
	model.create(null, null);

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

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	IModelView view = new TestModelOut();
	IModelView commandsProvider = new TestModelCommandsProvider();

	TestConfiguration configuration = new TestConfiguration();
	
	configuration.setMatchParameters(new MatchParameters());

	TestConfigurator configurator = new TestConfigurator();

	Presenter presenter = new Presenter(configurator, configuration);

	presenter.addView(view);
	presenter.addView(commandsProvider);

	presenter.start();
    }

}
