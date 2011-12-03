package com.fairbg.core;
/**
 * Параметры матча, счет и запись ходов в партиях
 * @author Vitalik
 */
public class Match {
    private MatchParameters _match_parameters = new MatchParameters();    
    /**
     * Возвращает параметры матча
     * @return 
     */
    public MatchParameters getMatchParams(){
        return _match_parameters;
    }

    /** добавляет хода в текущую партию */
    void addMoves(Move[] moves) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
