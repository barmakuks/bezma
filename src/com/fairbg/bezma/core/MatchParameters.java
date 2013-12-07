package com.fairbg.bezma.core;

import java.io.Serializable;

import com.fairbg.bezma.core.from_net.enums.RollTypes;
import com.fairbg.bezma.core.from_net.enums.MatchWinConditions;

/**
 * Параметры матча
 * @author Vitalik
 */
public class MatchParameters implements Serializable {
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
	
	public MatchIdentifier matchId = null;
	
	private static final long serialVersionUID = 5580660730954427945L;
	/**
	 * Имя первого игрока
	 */
	public String Player1 = "White player";
	/**
	 * Имя второго игрока
	 */
	public String Player2 = "Black player";
	/**
	 * Тип игры
	 */
	public int GameType = BACKGAMMON;
	/**
	 * Тип матча
	 */
	public MatchWinConditions MatchType = MatchWinConditions.Scores;
	/**
	 * Продолжительность матча (кол-во партий или очков)
	 */
	public int MatchLength = 3;
	/**
	 * использовать правило Кроуфорда?
	 */
	public boolean Crawford = true;
	/**
	 * Тип генерации бросков
	 */
	public RollTypes RollType = RollTypes.Generate;
	/**
	 * Вычислять значения кубиков автоматически?
	 */
	public boolean CalculateRolls = false;
	/**
	 * Вычислять значения кубиков точно (true) или приблизительно (false)?
	 */
	public boolean ExactRolls = true;
	/**
	 * Испольовать таймер?
	 */
	public boolean UseTimer = false; 
	/**
	 * Количество минут на матч
	 */
	public int MatchLimit = 30; 
	/**
	 * Количество секунд на ход
	 */
	public int MoveLimit = 30; 
	
	/**
	 * Требовать подтверждение удвоения кубика
	 */
	public boolean ConfirmDouble = true;
}
