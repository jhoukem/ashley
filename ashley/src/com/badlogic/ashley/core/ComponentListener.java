/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.ashley.core;

/**
 * Gets notified of {@link Component} related events.
 * @author Jean-Hugo Oukem
 */
public interface ComponentListener {
	
	/**
	 * Called whenever a {@link Component} is added to an {@link Entity} See
	 * {@link Engine#addComponentListener(ComponentListener)}
	 * @param component The component that has been added to the entity.
	 */
	public void componentAdded (Component component);

	/**
	 * Called whenever a {@link Component} is removed from an {@link Entity} See
	 * {@link Engine#removeComponentListener(ComponentListener)}
	 * @param component The component that has been removed from an entity.
	 */
	public void componentRemoved (Component component);
}
