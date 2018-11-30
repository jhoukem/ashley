package com.badlogic.ashley.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComponentInstanceListenerTests {

	private static class ComponentMockA implements Component{
		
	}
	
	private static class ComponentMockB implements Component{
		
	}
	
	private static class ComponentInstanceListenerAddedSpy extends ComponentInstanceListener {
		public boolean called;
		public int count;
		
		@Override
		public void added(Entity entity, Component object) {
			called = true;
			count++;
		}
	}
	
	private static class ComponentInstanceListenerRemovedSpy extends ComponentInstanceListener {
		public boolean called;
		public int count;
		
		@Override
		public void removed(Entity entity, Component object) {
			called = true;
			count++;
		}
	}
	
	@Test
	public void add() {
		ComponentInstanceListenerAddedSpy componentListenerAddedSpy = new ComponentInstanceListenerAddedSpy();
	
		Entity entity = new Entity();
		entity.componentInstanceListener = componentListenerAddedSpy;
		
		// When the entity component instance listener is set it should be called by the entity.
		assertFalse(componentListenerAddedSpy.called);
		entity.add(new ComponentMockA());
		entity.add(new ComponentMockB());
		assertTrue(componentListenerAddedSpy.called);
		assertEquals(componentListenerAddedSpy.count, entity.getComponents().size());
	}

	@Test
	public void addDelayed() {
		
		ComponentInstanceListenerAddedSpy componentListenerAddedSpy = new ComponentInstanceListenerAddedSpy();
		Engine engine = new Engine();
		engine.setComponentInstanceListener(componentListenerAddedSpy);

		// Add the component while the entity is not in the engine already.
		Entity entity = new Entity();
		entity.add(new ComponentMockA());
		entity.add(new ComponentMockB());
		// Ensure that is has not been called.
		assertFalse(componentListenerAddedSpy.called);
		// Add it to the engine and ensure that it has been called for every component.
		engine.addEntity(entity);
		assertTrue(componentListenerAddedSpy.called);
		assertEquals(componentListenerAddedSpy.count, entity.getComponents().size());
	}
	
	@Test
	public void remove() {
		ComponentInstanceListenerRemovedSpy  componentListenerRemovedSpy = new ComponentInstanceListenerRemovedSpy();
		
		Entity entity = new Entity();
		entity.add(new ComponentMockA());
		entity.add(new ComponentMockB());

		entity.componentInstanceListener = componentListenerRemovedSpy;
		
		// When the entity component instance listener is set it should be called by the entity.
		assertFalse(componentListenerRemovedSpy.called);
		entity.remove(ComponentMockA.class);
		entity.remove(ComponentMockB.class);
		assertTrue(componentListenerRemovedSpy.called);
		assertEquals(componentListenerRemovedSpy.count, 2);
	}
	
	@Test
	public void removeDelayed() {
		ComponentInstanceListenerRemovedSpy componentListenerRemovedSpy = new ComponentInstanceListenerRemovedSpy();
		Engine engine = new Engine();
		engine.setComponentInstanceListener(componentListenerRemovedSpy);

		// Add the component while the entity is not in the engine already.
		Entity entity = new Entity();
		entity.add(new ComponentMockA());
		entity.add(new ComponentMockB());
		engine.addEntity(entity);
		// Ensure that is has not been called.
		assertFalse(componentListenerRemovedSpy.called);
		engine.removeEntity(entity);
		// Add it to the engine and ensure that it has been called for every component.
		assertTrue(componentListenerRemovedSpy.called);
		assertEquals(componentListenerRemovedSpy.count, 2);
	}
}
