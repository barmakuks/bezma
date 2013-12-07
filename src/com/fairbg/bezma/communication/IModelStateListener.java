package com.fairbg.bezma.communication;

import com.fairbg.bezma.core.model.ModelSituation;

/** Интрефейс для устройств, которые могут отображать текущее состояние модели
 *	 
 */
public interface IModelStateListener {

	/**
	 * Отобразить на устройстве текущее состояние модели 
	 * @param aModelState текущее состояние модели
	 */
	public abstract void setModelState(ModelSituation aModelState);

}