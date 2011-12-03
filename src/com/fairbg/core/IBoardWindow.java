package com.fairbg.core;

/** ��������� ��� ����, ������������� ��������� ����� � ������������ ������� ���������� */
public interface IBoardWindow extends com.fairbg.core.commands.ICommander {
	/**
	 * ������������� ������� ������� �� �����
	 * @param position - ����� �������
	 */
	void setPosition(Position position);
	/**
	 * ������������� ��������� ����� �� �����	 
	 * @param params - ����� ��������� �����
	 */
	void setMatchParameters(MatchParameters params);
}
