package com.badlogic.ashley.core;

import com.badlogic.ashley.core.Engine.EntityComponentWrapper;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


class ComponentOperationHandler {
	private BooleanInformer delayed;
	private ComponentOperationPool operationPool = new ComponentOperationPool();
	private EntityComponentWrapperPool entityComponentWrapperPool = new EntityComponentWrapperPool();
 	private Array<ComponentOperation> operations = new Array<ComponentOperation>();

 	public ComponentOperationHandler(BooleanInformer delayed) {
 		this.delayed = delayed;
 	}
 	
	public void add(Entity entity, Component component) {
		if (delayed.value()) {
			ComponentOperation operation = operationPool.obtain();
			operation.makeAdd(entity, component);
			operations.add(operation);
		}
		else {
			entity.notifyComponentAdded(component);
		}
	}

	public void remove(Entity entity, Component component) {
		if (delayed.value()) {
			ComponentOperation operation = operationPool.obtain();
			operation.makeRemove(entity, component);
			operations.add(operation);
		}
		else {
			entity.notifyComponentRemoved(component);
		}
	}
	
	public boolean hasOperationsToProcess() {
		return operations.size > 0;
	}
	
	public void processOperations() {
		for (int i = 0; i < operations.size; ++i) {
			ComponentOperation operation = operations.get(i);

			switch(operation.type) {
				case Add:
					operation.entity.notifyComponentAdded(operation.component);
					break;
				case Remove:
					operation.entity.notifyComponentRemoved(operation.component);
					break;
				default: break;
			}

			operationPool.free(operation);
		}

		operations.clear();
	}
	
	public EntityComponentWrapper poolEntityWrapperComponent(){
		return entityComponentWrapperPool.obtain();
	}
	
	public void freeEntityComponentWrapper(EntityComponentWrapper entityComponentWrapper){
		entityComponentWrapperPool.free(entityComponentWrapper);
	}
	
	private static class ComponentOperation implements Pool.Poolable {
		public enum Type {
			Add,
			Remove,
		}

		public Type type;
		public Entity entity;
		public Component component;
		
		public void makeAdd(Entity entity, Component component) {
			this.type = Type.Add;
			this.entity = entity;
			this.component = component;
		}

		public void makeRemove(Entity entity, Component component) {
			this.type = Type.Remove;
			this.entity = entity;
			this.component = component;
		}

		@Override
		public void reset() {
			entity = null;
			component = null;
		}
	}
	
	private static class ComponentOperationPool extends Pool<ComponentOperation> {
		@Override
		protected ComponentOperation newObject() {
			return new ComponentOperation();
		}
	}
	
	private static class EntityComponentWrapperPool extends Pool<EntityComponentWrapper> {
		@Override
		protected EntityComponentWrapper newObject () {
			return new EntityComponentWrapper();
		}
	}
	
	interface BooleanInformer {
		public boolean value();
	}
}
