/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.core;

/**
 *
 * @author Vitalik
 */
interface IGameRules {
    /**
     * Вычисляет последовательность ходов на основе позиций фишек до и после хода
     * @param board текущая ситуация на доске 
     * @param next_position позиция после хода
     * @return Move[1], если это просто ход или Double/pass
     *         Move[2], если double/take и ход
     */
    Move[] findMove(Board board, Position next_position, int player);
    
    Position getStartPosition();
    Position getRandomPosition();
}
