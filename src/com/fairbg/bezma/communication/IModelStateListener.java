package com.fairbg.bezma.communication;

import com.fairbg.bezma.core.model.ModelSituation;
import com.fairbg.bezma.core.errors.Error;

/** Интрефейс для устройств, которые могут отображать текущее состояние модели
 *	 
 */
public interface IModelStateListener {

	/**
	 * Отобразить на устройстве текущее состояние модели 
	 * @param aModelState текущее состояние модели
	 */
	void setModelState(ModelSituation aModelState);
	
	void displayError(Error error);
}