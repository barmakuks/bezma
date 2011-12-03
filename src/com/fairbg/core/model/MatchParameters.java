package com.fairbg.core.model;
/**
 * Paramers of match
 * @author Vitalik
 */
public class MatchParameters {
	/****************************************************************************/
	/** ��� ����� "�������� �����" */
	public static final int BACKGAMMON  = 0;
	/** ��� ����� "������� �����" ��� Gul Bara*/
	public static final int GULBARA  = 1;
	/** ������������� ���������� ������*/
	public static final int FIXED_GAMES = 0;
	/** ������������� ���������� �����*/
	public static final int FIXED_POINTS = 1;
	
	/** ���� �������� ������ ������� ��� �� ���������� */
	public static final int MANUAL_ROLLS = 0;
	/** ������������ ��������� ������� */
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
