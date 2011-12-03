/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

/**
 * Описывает передвижение одной фишки 
 * @author Vitalik
 */
public final class Movement {
    /** Исходная позиция */    
    public int moveFrom;
    /** Конечная позиция */    
    public int moveTo;
    /** Пропуск хода */    
    public boolean strike;
    /** Цвет фишки */        
    public int color;

    /**
     * @return true, если пустой ход 
     */
    public boolean isEmpty() {
        return color == Constants.PLAYER_NONE;
    }

    @Override
    public String toString() {
        return super.toString();
        /*if (moveTo > 24)
        return String.format("  <{0:D2}> off", moveFrom);
        else
        if (moveFrom == 0)
        return String.Format("  <BAR> to <{0:D2}><1>", 25 - moveTo, strike ? "*" : "");
        else
        return String.Format("  <{0:D2}> to <{1:D1}><2>", 25 - moveFrom, 25 - moveTo, strike ? "*" : "");*/
    }

    public Movement() {
        clear();
    }

    public Movement(Movement aCopy) {
        moveFrom = aCopy.moveFrom;
        moveTo = aCopy.moveTo;
        color = aCopy.color;
        strike = aCopy.strike;
    }

    public Movement(int aMoveFrom, int aMoveTo, int aColor, boolean aStrike) {
        moveFrom = aMoveFrom;
        moveTo = aMoveTo;
        color = aColor;
        strike = aStrike;
    }

    public Movement clone() {
        return new Movement(moveFrom, moveTo, color, strike);
    }

    public boolean equal(Movement aCopy) {
        return (aCopy.moveTo == moveTo) && (aCopy.moveFrom == moveFrom) && (aCopy.color == color);
    }

    static public boolean equal(Movement aFirst, Movement aSecond) {
        return (aFirst == aSecond) || ((aFirst != null) && (aFirst.equal(aSecond)));
    }

    void clear() {
        moveFrom = 0;
        moveTo = 0;
        color = Constants.PLAYER_NONE;
        strike = false;
    }
}
