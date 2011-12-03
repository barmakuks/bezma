package com.fairbg.core.commands;
/**
 * ����� ������� ��������
 * */
public interface ICommandListener {
	/** ���������� ��� ��������� ������� 
	 * @param commander ������ ��������� �������
	 * @param command �������
	 */
	void onCommand(ICommander commander, UserCommand command);
}
