/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

import com.fairbg.core.commands.Command;

/**
 *
 * @author Vitalik
 */
public class Automat {

    /** текущее состояние автомата*/
    public int currentState = Constants.STATE_BEGIN;
    /** текущий игрок, от кого ожидается ввод команды*/
    public int currentPlayer = Constants.PLAYER_NONE;
    
    /** 
     * Проверяет, разрешена ли обработка команды от игрока
     * @param player игрок (PLAYER_WHITE или PLAYER_BLACK)
     * @return TRUE, если обработка команды от данного игрока разрешена
     */
    public boolean allowPlayerCommand(int player){
        return currentPlayer == Constants.PLAYER_NONE || currentPlayer == player;
    }
    
}
