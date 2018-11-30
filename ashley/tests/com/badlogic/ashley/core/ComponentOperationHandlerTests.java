package com.badlogic.ashley.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.badlogic.ashley.core.ComponentOperationHandler.BooleanInformer;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

public class ComponentOperationHandlerTests {

	private static class ComponentMock implements Component{
		
	}
	
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
	
	private static class ComponentInstanceListenerSpy extends ComponentInstanceListener {
		public boolean called;
		
		@Override
		public void added(Entity entity, Component object) {
			called = true;
		}
		
		@Override
		public void removed(Entity entity, Component object) {
			called = true;
		}
	}
	
	@Test
	public void add() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceListenerSpy componentListenerSpy = new ComponentInstanceListenerSpy();

		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentInstanceListener = componentListenerSpy;
		entity.componentAdded.add(componentSpy);
		
		handler.add(entity, new ComponentMock());
		
		assertTrue(componentSpy.called);
		assertTrue(componentListenerSpy.called);
	}

	@Test
	public void addDelayed() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceListenerSpy componentListenerSpy = new ComponentInstanceListenerSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentInstanceListener = componentListenerSpy;
		entity.componentAdded.add(componentSpy);
		
		handler.add(entity, new ComponentMock());
		
		assertFalse(componentSpy.called);
		assertFalse(componentListenerSpy.called);
		handler.processOperations();
		assertTrue(componentSpy.called);
		assertTrue(componentListenerSpy.called);
	}
	
	@Test
	public void remove() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceListenerSpy componentListenerSpy = new ComponentInstanceListenerSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentInstanceListener = componentListenerSpy;
		entity.componentRemoved.add(componentSpy);
		
		handler.remove(entity, new ComponentMock());
		
		assertTrue(componentSpy.called);
		assertTrue(componentListenerSpy.called);
	}
	
	@Test
	public void removeDelayed() {
		ComponentSpy spy = new ComponentSpy();
		ComponentInstanceListenerSpy componentListenerSpy = new ComponentInstanceListenerSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentInstanceListener = componentListenerSpy;
		entity.componentRemoved.add(spy);
		
		handler.remove(entity, new ComponentMock());
		
		assertFalse(spy.called);
		handler.processOperations();
		assertTrue(spy.called);
		assertTrue(componentListenerSpy.called);
	}
}
