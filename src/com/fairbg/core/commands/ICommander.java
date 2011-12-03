package com.fairbg.core.commands;
/** ����� �������� ������� ���������� */
public interface ICommander {
	/**
	 * ������������ ��������� ������
	 * @param listener ��������� ������
	 */
	void addListener(ICommandListener listener);
	/** ��������� ���������� �������� 
	 * @param command
	 */
	void sendCommand(UserCommand command);
	/** �������� ������������� ������� ��������� � ������� ��������� ��������� */
	void start();
	/** ����������� ������������� ������� ��������� � ������� ��������� ��������� */	
	void stop();
}
