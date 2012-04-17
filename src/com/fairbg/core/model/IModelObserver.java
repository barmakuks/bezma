package com.fairbg.core.model;

/**
 * Observer listenes notifies from Model
 * @author Vitalik
 *
 */
public interface IModelObserver {
	void updateModel(Model model);
}