/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

/**
 *
 * @author Vitalik
 */
public class Constants {
    /** Состояние начала матча*/
    public static final int STATE_BEGIN = 0;
    /** Состояние ввода или вычисления значений кубиков*/
    public static final int STATE_DICE = 1;
    /** Состояние подтверждения хода соперника*/    
    public static final int STATE_ACCEPT_MOVE = 2;
    /** Состояние принятия решения по кубику стоимости после Double*/    
    public static final int STATE_ACCEPT_DOUBLE = 3;
 
    public static final int CMD_NONE = 0;
    public static final int CMD_POSITION = 1;
    public static final int CMD_EXIT = 2;
    public static final int CMD_DICE = 3;
    
    
    public static final int PLAYER_BLACK = -1;
    public static final int PLAYER_NONE = 0;
    public static final int PLAYER_WHITE = 1;
    public static final int PLAYER_SOME = 11;
    
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
    /**Количество индикаторов на поле*/
    public static final int INDICATOR_COUNT = 5;
    
    
}
