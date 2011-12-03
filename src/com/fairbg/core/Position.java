package com.fairbg.core;

import java.util.Random;

/** ��������� ��������� ����� � ���� ��������� �� ����� */
public class Position {
	/** ����� ��������� ����������� */
	public static final int CUBE_CRAWFORD = -1;
	/** ����� ��������� � ������ ����� */
	public static final int CUBE_CENTER = 0;
	/** ����� ��������� � ������ ������, � ������ ����� ����� */
	public static final int CUBE_WHITE = 1;
	/** ����� ��������� � ������� ������, � ������� ����� ����� */
	public static final int CUBE_BLACK = 2;
	/**
	 * ����� ��������� ��������� ������� ������ � ��������� �� ������ �������
	 * �����
	 */
	public static final int CUBE_RIGHT = 3;
	/**
	 * ����� ��������� ��������� ������ ������ � ��������� �� ����� �������
	 * �����
	 */
	public static final int CUBE_LEFT = 4;

	private int[] _checkers = new int[28]; // 0-23 - ������� �����( >0 - �����,
											// <0 - ������, 0 - �����), 24 - ���
											// �����, 25 - ��� ������, 26,27 -
											// ������
	private int _cube_value = 1; // ������� �������� ������ ���������
	private int _cube_position = CUBE_CENTER; // ������� ��������� ������
												// ��������� �� �����

	/**
	 * ���������� ������ ���������� ��������� � ���� ����� �� ����� ����
	 * �������� >0, �� ���������� �����, ���� <0, �� ���������� ������ ������� �
	 * 0 �� 23 - ���������� ����� �� ������� ����� ������� 24 - ��� ����� �����
	 * ������� 25 - ��� ������ ����� 26 - ���� ������� ����� ����� 27 - ����
	 * ������� ������ �����
	 * */
	public int[] getPosiiton() {
		return _checkers;
	}

	/** ���������� ������� �������� ������ ��������� */
	public int getCubeValue() {
		return _cube_value;
	}

	/** ���������� ������� ��������� ������ ��������� �� ����� */
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
	 * ���������� �������� ��������������� �������
	 * */
	public static Position getRandomPosition() {
		Position pos = new Position();
		for (int i = 0; i < pos._checkers.length; i++) {
			pos._checkers[i] = 0;
		}
		Random rnd = new Random();
		int checkers_left = 15;
		while (checkers_left > 0) { // ����������� ����� �����
			int p = rnd.nextInt(28);
			if (p != 27 && p != 25 && pos._checkers[p] >= 0) {
				pos._checkers[p]++;
				checkers_left--;
			}
		}
		checkers_left = 15;
		while (checkers_left > 0) { // ����������� ������ �����
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
