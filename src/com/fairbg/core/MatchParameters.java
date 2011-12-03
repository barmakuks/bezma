package com.fairbg.core;

import java.io.Serializable;

/**
 * ��������� �����
 * @author Vitalik
 */
public class MatchParameters implements Serializable {
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
	
	
	private static final long serialVersionUID = 5580660730954427945L;
	/** ��� ������� ������ */
	public String Player1 = "White player";
	/** ��� ������� ������ */
	public String Player2 = "Black player";
	/** ��� ���� */
	public int GameType = BACKGAMMON;
	/** ��� ����� */
	public int MatchType = FIXED_POINTS;
	/** ����������������� ����� (���-�� ������ ��� �����) */
	public int MatchLength = 3;
	/** ������������ ������� ���������? */
	public boolean Crawford = true;
	/** ��� ��������� ������� */
	public int RollType = COMPUTER_ROLLS;
	/** ��������� �������� ������� �������������? */
	public boolean CalculateRolls = false;
	/** ��������� �������� ������� ����� (true) ��� �������������� (false)? */
	public boolean ExactRolls = true;
	/** ����������� ������? */
	public boolean UseTimer = false; 
	/** ���������� ����� �� ���� */
	public int MatchLimit = 30; 
	/** ���������� ������ �� ��� */
	public int MoveLimit = 30; 
	
	
}
