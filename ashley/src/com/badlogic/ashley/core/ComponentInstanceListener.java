package com.badlogic.ashley.core;

/**
 * This manager can be used to perform custom procedures when an component is added to an entity.
 * 
 * @author Jean-Hugo Oukem
 */
public class ComponentInstanceListener {

	/**
	 * Called when a component is removed from an entity. If the entity is inside the engine it will
	 * be triggered immediately otherwise it will be triggered when the entity will be added to the engine.
	 * 
	 * @param entity The entity this component has been removed from.
	 * @param object The component.
	 */
	public void removed(Entity entity, Component object) {
		// Custom logic.
	}
	
	/**
	 * Called when a component is added to an entity. If the entity is inside the engine it will
	 * be triggered immediately otherwise it will be triggered when the entity will be added to the engine.
	 * 
	 * @param entity The entity this component has been added on.
	 * @param object The component.
	 */
	public void added(Entity entity, Component object) {
		// Custom logic.
	}

	/**
	 * When an entity is added to the engine this method is called to inject all the entity components.
	 * 
	 * @param entity The entity that has been added to the engine.
	 */
	public void entityAdded(Entity entity) {
		for(Component component : entity.getComponents()){
			added(entity, component);
		}
	}

	/**
	 * When an entity is removed from the engine this method is called to clean ? All the entity components.
	 * 
	 * @param entity The entity that has been removed from the engine.
	 */
	public void entityRemoved(Entity entity) {
		for(Component component : entity.getComponents()){
			removed(entity, component);
		}
	}
	
}
