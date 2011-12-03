package com.fairbg.core;

import java.util.Random;

/** Описывает положение фишек и куба стоимости на доске */
public class Position {
	/** Кубик стоимости отсутствует */
	public static final int CUBE_CRAWFORD = -1;
	/** Кубик стоимости в центре доски */
	public static final int CUBE_CENTER = 0;
	/** Кубик стоимости у белого игрока, в нижней части доски */
	public static final int CUBE_WHITE = 1;
	/** Кубик стоимости у черного игрока, в верхней части доски */
	public static final int CUBE_BLACK = 2;
	/**
	 * Кубик стоимости предложен черному игроку и находится на правой стороне
	 * доски
	 */
	public static final int CUBE_RIGHT = 3;
	/**
	 * Кубик стоимости предложен белому игроку и находится на левой стороне
	 * доски
	 */
	public static final int CUBE_LEFT = 4;

	private int[] _checkers = new int[28]; // 0-23 - позиции фишек( >0 - белые,
											// <0 - черные, 0 - пусто), 24 - бар
											// белых, 25 - бар черных, 26,27 -
											// выброс
	private int _cube_value = 1; // текущее значение кубика стоимости
	private int _cube_position = CUBE_CENTER; // текущее положение кубика
												// стоимости на столе

	/**
	 * Возвращает массив содержащий положение и цвет фишек на доске если
	 * значение >0, то количество белых, если <0, то количество черных позиция с
	 * 0 по 23 - количество фишек на игровых полях позиция 24 - бар белых фишек
	 * позиция 25 - бар черных фишек 26 - зона выброса белых фишек 27 - зона
	 * выброса черных фишек
	 * */
	public int[] getPosiiton() {
		return _checkers;
	}

	/** Возвращает текущее значение кубика стоимости */
	public int getCubeValue() {
		return _cube_value;
	}

	/** Возвращает текущее положение кубика стоимости на доске */
	public int getCubePosition() {
		return _cube_position;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < _checkers.length; i++) {
			res.append(_checkers[i]);
			res.append(',');
		}
		return res.toString();
	}

	/**
	 * Возвращает случайно сгенерированную позицию
	 * */
	public static Position getRandomPosition() {
		Position pos = new Position();
		for (int i = 0; i < pos._checkers.length; i++) {
			pos._checkers[i] = 0;
		}
		Random rnd = new Random();
		int checkers_left = 15;
		while (checkers_left > 0) { // расставляем белые фишки
			int p = rnd.nextInt(28);
			if (p != 27 && p != 25 && pos._checkers[p] >= 0) {
				pos._checkers[p]++;
				checkers_left--;
			}
		}
		checkers_left = 15;
		while (checkers_left > 0) { // расставляем черные фишки
			int p = rnd.nextInt(28);
			if (p != 26 && p != 24 && pos._checkers[p] <= 0) {
				pos._checkers[p]--;
				checkers_left--;
			}
		}
		pos._cube_value = (new int[]{ 2, 4, 8, 16, 32, 64 })[rnd.nextInt(6)];
		pos._cube_position =  rnd.nextInt(6) - 1;
		return pos;
	}
}
