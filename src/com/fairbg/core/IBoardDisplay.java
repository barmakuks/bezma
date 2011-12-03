package com.fairbg.core;

/**
 * Интерфейс отображения текущего состояния игры на каком-либо устройстве
 * @author Vitalik
 */
public interface IBoardDisplay extends com.fairbg.core.commands.ICommander{

    /**
     * Устанавливает связь с объектом Board
     * @param board объект, хранящий в себе текущее состояние доски
     */
    void setBoardObject(Board board);

    /**
     * Вызывается, когда необходимо обновить отображение объекта board на устройстве
     */
    void updateDisplay();
}
