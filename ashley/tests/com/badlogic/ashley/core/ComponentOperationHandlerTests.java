package com.badlogic.ashley.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.badlogic.ashley.core.ComponentOperationHandler.BooleanInformer;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

public class ComponentOperationHandlerTests {

	private static class BooleanInformerMock implements BooleanInformer {
		public boolean delayed = false;
		
		@Override
		public boolean value () {
			return delayed;
		}
	}
	
	private static class ComponentSpy implements Listener<Entity> {
		public boolean called;
		
		@Override
		public void receive(Signal<Entity> signal, Entity object) {
			called = true;
		}
	}
	
	@Test
	public void add() {
		ComponentSpy componentSpy = new ComponentSpy();

		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentAdded.add(componentSpy);
		
		handler.add(entity);
		
		assertTrue(componentSpy.called);
	}

	@Test
	public void addDelayed() {
		ComponentSpy componentSpy = new ComponentSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentAdded.add(componentSpy);
		
		handler.add(entity);
		
		assertFalse(componentSpy.called);
		handler.processOperations();
		assertTrue(componentSpy.called);
	}
	
	@Test
	public void remove() {
		ComponentSpy componentSpy = new ComponentSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentRemoved.add(componentSpy);
		
		handler.remove(entity);
		
		assertTrue(componentSpy.called);
	}
	
	@Test
	public void removeDelayed() {
		ComponentSpy spy = new ComponentSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentRemoved.add(spy);
		
		handler.remove(entity);
		
		assertFalse(spy.called);
		handler.processOperations();
		assertTrue(spy.called);
	}
}
