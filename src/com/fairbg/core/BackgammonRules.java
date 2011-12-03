/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

import java.util.ArrayList;
import java.util.Random;

/**
 * Вычисляет очередной ход
 * @author Vitalik
 */
public class BackgammonRules implements IGameRules {
    int [][] dice = {{1,2},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1},{1,1}};
    public Move[] findMove(Board board, Position next_position, int player) {
        for(int die1 = 1; die1 <= 6; die1++)
            for(int die2 = die1; die2 <= 6; die2++){
                
                
        }
        
        return null;//throw new UnsupportedOperationException("Not supported yet.");
    }
    private ArrayList<Move> _foundMoves = new ArrayList<Move>();

    /** Вычмсляет возможный ход
     * @param aMove - ход, здесь будет записан результат, при вызове рекурсии
     * @param aPositionFrom - исходная позиция
     * @param aPositionTo - конечная позиция
     * @param aPlayer - игрок, для которого вычисляется ход
     * @return true, если ход вычислен
     */
    private boolean findPosibleMoves(Move aMove, Position aPositionFrom, Position aPositionTo, int aPlayer) {
        Position aPositionFromCopy = new Position(); // TBzmBoardPositionFactory.Create(RulesType.Backgammon);
        Move move_copy = null;
        int from = -1;
        while (true) {
            from = findFirstMovedChecker(aPositionFrom, aPositionTo, aPlayer, from);
            if (from == -1) {
                return false;
            }
            for (int i = 0; i <= 3; i++) {
                move_copy = aMove.clone();
                aPositionFromCopy = (Position) aPositionFrom.clone();
                if (aMove.dice[i] != 0 && aMove.getMovement(i).isEmpty()) {
                    if (canApplayMovement(aPositionFrom, from, from + aMove.dice[i], aPlayer)) {
                        if (from + aMove.dice[i] < 25) {
                            int color = aPositionFromCopy.getColor(from, aPlayer);
                            if (color != aPlayer && color != Constants.PLAYER_NONE) {
                                move_copy.addMovement(from, from + aMove.dice[i], true);
                            } else {
                                move_copy.addMovement(from, from + aMove.dice[i], false);
                            }
                        } else {
                            move_copy.addMovement(from, from + aMove.dice[i], false);
                        }


                        aPositionFromCopy.moveChecker(from, from + aMove.dice[i], aPlayer);
                        if (stillCanDoMovement(move_copy, aPositionFromCopy)) {
                            findPosibleMoves(move_copy, aPositionFromCopy, aPositionTo, aPlayer);
                        } else {
                            if (aPositionFromCopy.equal_checkers(aPositionTo)) {
                                boolean notFoundEqual = true;
                                for (int j = 0; j < _foundMoves.size(); j++) {
                                    notFoundEqual = notFoundEqual && !(_foundMoves.get(j).equal(move_copy));
                                }
                                if (notFoundEqual) {
                                    _foundMoves.add(move_copy.clone());
                                }                                
                            }
                        }
                    }
                }
            }
        }

    }

    private int findFirstMovedChecker(Position aPositionFrom, Position aPositionTo, int aColor, int beginFrom) {
        for (int i = beginFrom + 1; i <= 25; i++) {
            if ((aPositionFrom.getColor(i, aColor) == aColor)
                    && (aPositionFrom.getCheckerCount(i, aColor) > aPositionTo.getCheckerCount(i, aColor)
                    || aPositionFrom.getCheckerCount(i, aColor) >= Constants.INDICATOR_COUNT)) {
                return i;
            }
        }
        return -1;
    }

    private Move getBestMove(Position aPositionFrom, int aColor) {

        int aAltColor = (aColor == Constants.PLAYER_WHITE) ? Constants.PLAYER_BLACK : Constants.PLAYER_WHITE;
        Move best_move = null, temp_move = null;
        int aMinMovesLeft = -1000, aMovesLeft = 0;
        Position aPositionCopy;
        for (int i = 0; i < _foundMoves.size(); i++) {
            aPositionCopy = (Position) aPositionFrom.clone();
            temp_move = _foundMoves.get(i);
            temp_move.applay(aPositionCopy);
            aMovesLeft = aPositionCopy.getPips(aAltColor) - aPositionCopy.getPips(aColor);
            if (aMinMovesLeft < aMovesLeft) {
                best_move = temp_move;
                aMinMovesLeft = aMovesLeft;
            }
        }
        return best_move;
    }

    private boolean hasLefterCheckerInHome(Position aPosition, int aMoveFrom, int aPlayerId) {
        for (int i = aMoveFrom - 1; i >= 19; i--) {
            if (aPosition.getColor(aMoveFrom, aPlayerId) == aPosition.getColor(i, aPlayerId)) {
                return true;
            }
        }
        return false;
    }

    private boolean canApplayMovement(Position aBoardPosition, int aMoveFrom, int aMoveTo, int aPlayerId) {
        if (aBoardPosition.getCheckerCount(aMoveFrom, aPlayerId) == 0) // Если нет шашки для хода
        {
            return false;
        }
        if (aBoardPosition.getCheckerCount(0, aPlayerId) != 0 && aMoveFrom != 0) // Если есть шашка на баре и ход не из бара
        {
            return false;
        }
        if (aMoveTo >= 25) {
            if (!canOffChecker(aBoardPosition, aBoardPosition.getColor(aMoveFrom, aPlayerId))) {
                return false;
            }
            if (aMoveTo != 25 && hasLefterCheckerInHome(aBoardPosition, aMoveFrom, aPlayerId)) {
                return false;
            }
        } else {
            if ((aBoardPosition.getColor(aMoveFrom, aPlayerId) != aBoardPosition.getColor(aMoveTo, aPlayerId)
                    && (aBoardPosition.getColor(aMoveTo, aPlayerId) != Constants.PLAYER_NONE)
                    && (aBoardPosition.getCheckerCount(aMoveTo, aPlayerId) > 1))) {
                return false;
            }
        }
        return true;
    }

    public Move findMove(byte aDie1, byte aDie2, int anActivePlayerId, Position aPositionFrom, Position aPositionTo) {
        _foundMoves.clear();
        Move move = new Move(aDie1, aDie2, anActivePlayerId);

        // Если ни одна фишка не передвинута, то смотрим возможность пропуска хода
        if (aPositionFrom.absolute_equal(aPositionTo) && !stillCanDoMovement(move, aPositionFrom)) {
            return move;
        }

        findPosibleMoves(move, aPositionFrom, aPositionTo, anActivePlayerId);
        if (_foundMoves.size() > 0) {
            return getBestMove(aPositionFrom, anActivePlayerId);
        } else {
            return null;
        }
    }

    public boolean stillCanDoMovement(Move move, Position aBoardPosition) {
        if (aBoardPosition.getCheckerCount(0, move.color) > 0) {
            for (int i = 0; i < 4; i++) {
                if (move.dice[i] != 0 && move.getMovement(i).isEmpty()
                        && canApplayMovement(aBoardPosition, 0, move.dice[i], move.color)) {
                    return true;
                }
            }
            return false;
        }
        for (int j = 1; j <= 24; j++) {
            if (aBoardPosition.getColor(j, move.color) == move.color) {
                for (int i = 0; i < 4; i++) {
                    if (move.dice[i] != 0 && move.getMovement(i).isEmpty()
                            && canApplayMovement(aBoardPosition, j, j + move.dice[i], move.color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canOffChecker(Position aPosition, int aPlayerId) {
        for (int i = 0; i <= 18; i++) {
            if (aPosition.getColor(i, aPlayerId) == aPlayerId && aPosition.getCheckerCount(i, aPlayerId) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает случайно сгенерированную позицию
     * */
    public Position getRandomPosition() {
        Position pos = new Position();
        Random rnd = new Random();
        int[] checkers = pos.getCheckers();
        int checkers_left = 15;
        while (checkers_left > 0) { // расставляем белые фишки
            int p = rnd.nextInt(28);
            if (p != 27 && p != 25 && pos.getColor(p, Constants.PLAYER_BLACK) >= 0) {
                checkers[p]++;
                checkers_left--;
            }
        }
        checkers_left = 15;
        while (checkers_left > 0) { // расставляем черные фишки
            int p = rnd.nextInt(28);
            if (p != 26 && p != 24 && pos.getColor(p, Constants.PLAYER_BLACK) <= 0) {
                checkers[p]++;
                checkers_left--;
            }
        }
        pos.setCubeValue((new int[]{2, 4, 8, 16, 32, 64})[rnd.nextInt(6)]);
        pos.setCubePosition(rnd.nextInt(6) - 1);
        return pos;
    }

    /** Возвращает начальную позицию */
    public Position getStartPosition() {
        Position pos = new Position();
        pos.setChecker(0, 2, Constants.PLAYER_BLACK);
        pos.setChecker(11, 5, Constants.PLAYER_BLACK);
        pos.setChecker(16, 3, Constants.PLAYER_BLACK);
        pos.setChecker(18, 5, Constants.PLAYER_BLACK);

        pos.setChecker(5, 5, Constants.PLAYER_WHITE);
        pos.setChecker(7, 3, Constants.PLAYER_WHITE);
        pos.setChecker(12, 5, Constants.PLAYER_WHITE);
        pos.setChecker(23, 2, Constants.PLAYER_WHITE);
        return pos;
    }
}
