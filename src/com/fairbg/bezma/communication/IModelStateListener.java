package com.fairbg.bezma.communication;

import com.fairbg.core.model.ModelState;

/** Интрефейс для устройств, которые могут отображать текущее состояние модели
 *	 
 */
public interface IModelStateListener {

	/**
	 * Отобразить на устройстве текущее состояние модели 
	 * @param aModelState текущее состояние модели
	 */
	public abstract void setModelState(ModelState aModelState);

}