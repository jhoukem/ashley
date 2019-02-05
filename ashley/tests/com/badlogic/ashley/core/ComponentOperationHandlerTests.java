package com.badlogic.ashley.core;

import com.badlogic.ashley.core.ComponentOperationHandler.BooleanInformer;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import org.junit.Test;

import static org.junit.Assert.*;

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
		public int addedCalledCount;
		public int removedCalledCount;

		@Override
		public void added(Entity entity, Component object) {
			addedCalledCount++;
		}
		
		@Override
		public void removed(Entity entity, Component object) {
			removedCalledCount++;
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
		assertEquals(componentListenerSpy.addedCalledCount, 1);
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
		assertEquals(componentListenerSpy.addedCalledCount, 0);
		handler.processOperations();
		assertTrue(componentSpy.called);
		assertEquals(componentListenerSpy.addedCalledCount, 1);
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
		assertEquals(componentListenerSpy.removedCalledCount, 1);
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
		assertEquals(componentListenerSpy.removedCalledCount, 1);
	}
}
