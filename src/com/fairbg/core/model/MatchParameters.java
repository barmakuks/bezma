package com.fairbg.core.model;
/**
 * Paramers of match
 * @author Vitalik
 */
public class MatchParameters {
	/****************************************************************************/
	/** тип матча "Короткие нарды" */
	public static final int BACKGAMMON  = 0;
	/** тип матча "Длинные нарды" или Gul Bara*/
	public static final int GULBARA  = 1;
	/** Фиксированное количество партий*/
	public static final int FIXED_GAMES = 0;
	/** Фиксированное количество очков*/
	public static final int FIXED_POINTS = 1;
	
	/** Ввод значений кубико вручную или их вычисление */
	public static final int MANUAL_ROLLS = 0;
	/** Компьютерная генерация бросков */
	public static final int COMPUTER_ROLLS = 1;
	/****************************************************************************/
	
	/** Match identifier */
	public MatchId matchId = null;	
	/** First player name */
	public String Player1 = "White player";
	/** Second player name */
	public String Player2 = "Black player";
	/** Game type */
	public int GameType = BACKGAMMON;
	/** Match type */
	public int MatchType = FIXED_POINTS;
	/** Maximum match duration */
	public int MatchLength = 3;
	/** Use Crawford rule? */
	public boolean Crawford = true;
	/** Roll generation type */
	public int RollType = COMPUTER_ROLLS;
	/** Calculate rolls? */
	public boolean CalculateRolls = false;
	/** Calculate rolls exactly(true) or approximately(false)? */
	public boolean ExactRolls = true;
	/** Should use timer? */
	public boolean UseTimer = false; 
	/** Match time limit (minutes) */
	public int MatchLimit = 30; 
	/** Move time limit (seconds) */
	public int MoveLimit = 30; 
	
}
