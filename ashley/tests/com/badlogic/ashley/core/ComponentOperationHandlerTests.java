package com.badlogic.ashley.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.ashley.core.ComponentOperationHandler.BooleanInformer;
import com.badlogic.ashley.core.Engine.EntityComponentWrapper;
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
	
	private static class ComponentInstanceSpy implements Listener<EntityComponentWrapper> {
		public boolean called;
		
		@Override
		public void receive(Signal<EntityComponentWrapper> signal, EntityComponentWrapper object) {
			called = true;
		}
	}
	
	@Test
	public void add() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceSpy componentInstanceSpy = new ComponentInstanceSpy();

		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentAdded.add(componentSpy);
		entity.componentInstanceAdded.add(componentInstanceSpy);
		
		handler.add(entity, new ComponentMock());
		
		assertTrue(componentSpy.called);
		assertTrue(componentInstanceSpy.called);
	}

	@Test
	public void addDelayed() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceSpy componentInstanceSpy = new ComponentInstanceSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentAdded.add(componentSpy);
		entity.componentInstanceAdded.add(componentInstanceSpy);
		
		handler.add(entity, new ComponentMock());
		
		assertFalse(componentSpy.called);
		assertFalse(componentInstanceSpy.called);
		handler.processOperations();
		assertTrue(componentSpy.called);
		assertTrue(componentInstanceSpy.called);
	}
	
	@Test
	public void remove() {
		ComponentSpy componentSpy = new ComponentSpy();
		ComponentInstanceSpy componentInstanceSpy = new ComponentInstanceSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentRemoved.add(componentSpy);
		entity.componentInstanceRemoved.add(componentInstanceSpy);
		
		handler.remove(entity, new ComponentMock());
		
		assertTrue(componentSpy.called);
		assertTrue(componentInstanceSpy.called);
	}
	
	@Test
	public void removeDelayed() {
		ComponentSpy spy = new ComponentSpy();
		ComponentInstanceSpy componentInstanceSpy = new ComponentInstanceSpy();
		BooleanInformerMock informer = new BooleanInformerMock();
		ComponentOperationHandler handler = new ComponentOperationHandler(informer);
		
		informer.delayed = true;
		
		Entity entity = new Entity();
		entity.componentOperationHandler = handler;
		entity.componentRemoved.add(spy);
		entity.componentInstanceRemoved.add(componentInstanceSpy);
		
		handler.remove(entity, new ComponentMock());
		
		assertFalse(spy.called);
		handler.processOperations();
		assertTrue(spy.called);
		assertTrue(componentInstanceSpy.called);
	}
}
