/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Vitalik
 */
public final class Move {
    private Movement[] _movements = new Movement[4];
    //PUBLIC
    public int moveNo;
    public Position positionBeforeMove = null;
    public Position positionAfterMove = null;
    public boolean HasAlternativeDice = false;
    public int[] dice = new int[4];
    public int color;    
    
    //CONSTRUCTOR
    public Move(byte aDie1, byte aDie2, int aColor) {
        _die1 = aDie1;
        _die2 = aDie2;
        color = aColor;
        for (int i = 0; i < 4; i++) {
            _movements[i] = new Movement();
        }
        initDice();
    }
    
    private void orderMovements() {
        for (int i = 0; i <= 2; i++) {
            for (int j = i + 1; j <= 3; j++) {
                if ((!_movements[i].isEmpty()) && (!_movements[j].isEmpty())
                        && (_movements[i].moveFrom > _movements[j].moveFrom)) {
                    Movement temp_movement = _movements[i];
                    int temp_die = dice[i];
                    _movements[i] = _movements[j];
                    dice[i] = dice[j];
                    _movements[j] = temp_movement;
                    dice[j] = temp_die;
                }
            }
        }
    }
    //PROTECTED
    private byte _die1, _die2;
    
    private void initDice() {
        dice[0] = _die1;
        dice[1] = _die2;
        if (_die1 == _die2) {
            dice[2] = _die1;
            dice[3] = _die2;
        } else {
            dice[2] = 0;
            dice[3] = 0;
        }
    }

    
    public int getMovementCount() {
        int res = 0;
        for (int i = 0; i < 4; i++) {
            if (!_movements[i].isEmpty()) {
                res++;
            }
        }
        return res;
    }
    
    public Movement getMovement(int anIndex) {
        if (anIndex >= 0 && anIndex <= 3 && _movements[anIndex] != null) {
            return _movements[anIndex];
        } else {
            return null;
        }
    }
    
    public byte getDie(int aNo) {
        switch (aNo) {
            case 1:
                return _die1;
            case 2:
                return _die2;
            default:
                return 0;
        }
    }
    
    public boolean addMovement(int aFrom, int aTo, boolean aStrike) {
        for (int i = 0; i < 4; i++) {
            if (_movements[i].isEmpty() && (aTo - aFrom == dice[i])) {
                _movements[i].moveFrom = aFrom;
                _movements[i].moveTo = aTo;
                _movements[i].color = color;
                _movements[i].strike = aStrike;
                orderMovements();
                return true;
            }
        }
        return false;
        
    }
    
    public boolean canAddMovement(int aFrom, int aTo) {
        for (int i = 0; i <= 3; i++) {
            if (dice[i] == (aTo - aFrom) && (_movements[i].isEmpty())) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        for (int i = 0; i < 4; i++) {
            _movements[i].clear();
        }
        initDice();
    }
    
    public Move clone() {
        Move aCopy = new Move(_die1, _die2, color);
        for (int i = 0; i < 4; i++) {
            aCopy._movements[i] = new Movement(_movements[i]);
        }
        aCopy.moveNo = moveNo;
        return aCopy;
    }
    
    public boolean equalDice(byte aDie1, byte aDie2) {
        return (_die1 == aDie1 && _die2 == aDie2) || (_die1 == aDie2 && _die2 == aDie1);
    }
    
    public boolean equal(Move aCopy) {
        for (int i = 0; i <= 3; i++) {
            if (aCopy.dice[i] != dice[i]
                    || !Movement.equal(_movements[i], aCopy._movements[i])) {
                return false;
            }
        }
        return true;
    }
    
    public void InitDice(byte aDie1, byte aDie2, int aColor) {
        _die1 = aDie1;
        _die2 = aDie2;
        color = aColor;
        initDice();        
    }
    
    @Override
    public String toString() {
        return super.toString();
        /*
        char separator = ' ';
        string format_string = "{0}({1},{2})";
        string res = String.Format(format_string, TBzmPlayerColorUtils.GetPlayerShortColorString(color), _die1, _die2);
        for (int i = 0; i < 4; i++) {
        if (!_movements[i].IsEmpty) {
        if (_movements[i].MoveFrom == 0) {
        res = res + String.Format("{0}bar/{1}{2}", separator, 25 - _movements[i].MoveTo, _movements[i].Strike ? "*" : "");
        } else if (_movements[i].MoveTo > 24) {
        res = res + String.Format("{0}{1}/off", separator, 25 - _movements[i].MoveFrom);
        } else {
        res = res + String.Format("{0}{1}/{2}{3}", separator, 25 - _movements[i].MoveFrom, 25 - _movements[i].MoveTo, _movements[i].Strike ? "*" : "");
        }
        separator = ';';
        }
        }
        return res;
         */
    }
    
    public void applay(Position aBoardPosition) {
        throw new NotImplementedException();
        /*for (int i = 0; i < 4; i++) {
        if (!_movements[i].isEmpty()) {
        aBoardPosition.ApplayMovement(_movements[i].moveFrom, _movements[i].moveTo, _movements[i].color);
        }
        }*/
    }
}
