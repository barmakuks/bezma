package com.fairbg.bezma.store;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.model.Match;
import com.fairbg.bezma.core.model.ModelState;

/**
 * Interface for model storage
 * contains all functions for store/restore model
 * @author Vitalik
 */
public interface IDatabase {
	/**
	 * reads Match Parameters from storage
	 * @param matchId Match identifier
	 * @return MatchParameters
	 */
	MatchParameters readMatchParameters(MatchIdentifier matchId);

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
	void writeCurrentState(MatchIdentifier matchId, ModelState m_ModelState);

	/**
	 * Writes match into storage
	 * @param matchId match identifier
	 * @param match match to write
	 */
	void writeMatch(MatchIdentifier matchId, Match match);

}