package com.fairbg.bezma.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Match parameters
 */
public class MatchParameters implements Serializable
{

	public enum RollTypes {
		Manual, Generate, CommonDice, FromFile
	}

	public enum MatchWinConditions {
		Scores, FixedGames, MoneyGame
	}

	public enum GameType {
		Backgammon("Backgammon"), GulBara("Gul Bara");

		private final String name;

		GameType(final String name)
		{
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}

	public MatchIdentifier	matchId		  = null;

	private static final long serialVersionUID = 5580660730954427945L;

	public String			 bPlayerName	  = "White player";

	public String			 wPlayerName	  = "Black player";

	public GameType		   gameType		 = GameType.Backgammon;

	public MatchWinConditions winConditions	= MatchWinConditions.Scores;

	public int				matchLength	  = 3;
	public boolean			isCrawford	   = true;
	public RollTypes		rollType		 = RollTypes.Generate;
	public boolean			calculateRolls   = false;
	public boolean			exactRolls	   = true;
	public boolean			useTimer		 = false;
	public int				matchTimeLimit   = 30;
	public int				moveTimeLimit	= 30;

	public boolean			confirmDouble	= true;
	
	public Date             eventDate = new Date();
	
	public String           defaultDir = null; 
}
