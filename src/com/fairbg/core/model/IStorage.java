package com.fairbg.core.model;
/**
 * Intarface for model storage
 * contains all functions for store/restore model
 * @author Vitalik
 */
public interface IStorage {

	/**
	 * reads Match Parameters from storage
	 * @param matchId Match identifier
	 * @return MatchParameters
	 */
	MatchParameters readMatchParameters(MatchId matchId);

	/**
	 * Writes Match Parameters into storage
	 * @param m_MatchParameters match Parameters
	 */
	void writeMatchParameters(MatchParameters m_MatchParameters);

	/**
	 * Writes model state into storage
	 * @param matchId match identifier
	 * @param m_ModelState model state to write
	 */
	void writeCurrentState(MatchId matchId, ModelState m_ModelState);

	/**
	 * Writes match into storage
	 * @param matchId match identifier
	 * @param match match to write
	 */
	void writeMatch(MatchId matchId, Match match);
	

}
