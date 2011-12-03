package com.fairbg.core;

/** Интерфейс для окна, отображающего состояние матча и формирующего команды управления */
public interface IBoardWindow extends com.fairbg.core.commands.ICommander {
	/**
	 * Устанавливает текущую позицию на доске
	 * @param position - новая позиция
	 */
	void setPosition(Position position);
	/**
	 * Устанавливает параметры матча на доске	 
	 * @param params - новые параметры матча
	 */
	void setMatchParameters(MatchParameters params);
}
