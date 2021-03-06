/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.orm.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Base class for any class that needs to access an EntityManagerFactory,
 * usually in order to obtain an EntityManager. Defines common properties.
 *
 * <p>Not intended to be used directly. See {@link JpaAccessor}.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see JpaAccessor
 * @see EntityManagerFactoryUtils
 */
public abstract class EntityManagerFactoryAccessor {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private EntityManagerFactory entityManagerFactory;

	private final Map<String, Object> jpaPropertyMap = new HashMap<String, Object>();


	/**
	 * Set the JPA EntityManagerFactory that should be used to create
	 * EntityManagers.
	 * @see javax.persistence.EntityManagerFactory#createEntityManager()
	 * @see javax.persistence.EntityManagerFactory#createEntityManager(java.util.Map)
	 */
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.entityManagerFactory = emf;
	}

	/**
	 * Return the JPA EntityManagerFactory that should be used to create
	 * EntityManagers.
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFactory;
	}

	/**
	 * Specify JPA properties, to be passed into
	 * <code>EntityManagerFactory.createEntityManager(Map)</code> (if any).
	 * <p>Can be populated with a String "value" (parsed via PropertiesEditor)
	 * or a "props" element in XML bean definitions.
	 * @see javax.persistence.EntityManagerFactory#createEntityManager(java.util.Map)
	 */
	public void setJpaProperties(Properties jpaProperties) {
		CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.jpaPropertyMap);
	}

	/**
	 * Specify JPA properties as a Map, to be passed into
	 * <code>EntityManagerFactory.createEntityManager(Map)</code> (if any).
	 * <p>Can be populated with a "map" or "props" element in XML bean definitions.
	 * @see javax.persistence.EntityManagerFactory#createEntityManager(java.util.Map)
	 */
	public void setJpaPropertyMap(Map<String, Object> jpaProperties) {
		if (jpaProperties != null) {
			this.jpaPropertyMap.putAll(jpaProperties);
		}
	}

	/**
	 * Allow Map access to the JPA properties to be passed to the persistence
	 * provider, with the option to add or override specific entries.
	 * <p>Useful for specifying entries directly, for example via "jpaPropertyMap[myKey]".
	 */
	public Map<String, Object> getJpaPropertyMap() {
		return this.jpaPropertyMap;
	}


	/**
	 * Obtain a new EntityManager from this accessor's EntityManagerFactory.
	 * <p>Can be overridden in subclasses to create specific EntityManager variants.
	 * @return a new EntityManager
	 * @throws IllegalStateException if this accessor is not configured with an EntityManagerFactory
	 * @see javax.persistence.EntityManagerFactory#createEntityManager()
	 * @see javax.persistence.EntityManagerFactory#createEntityManager(java.util.Map)
	 */
	protected EntityManager createEntityManager() throws IllegalStateException {
		EntityManagerFactory emf = getEntityManagerFactory();
		Assert.state(emf != null, "No EntityManagerFactory specified");
		Map<String, Object> properties = getJpaPropertyMap();
		return (!CollectionUtils.isEmpty(properties) ? emf.createEntityManager(properties) : emf.createEntityManager());
	}

	/**
	 * Obtain the transactional EntityManager for this accessor's EntityManagerFactory, if any.
	 * @return the transactional EntityManager, or <code>null</code> if none
	 * @throws IllegalStateException if this accessor is not configured with an EntityManagerFactory
	 * @see EntityManagerFactoryUtils#getTransactionalEntityManager(javax.persistence.EntityManagerFactory)
	 * @see EntityManagerFactoryUtils#getTransactionalEntityManager(javax.persistence.EntityManagerFactory, java.util.Map)
	 */
	protected EntityManager getTransactionalEntityManager() throws IllegalStateException{
		EntityManagerFactory emf = getEntityManagerFactory();
		Assert.state(emf != null, "No EntityManagerFactory specified");
		return EntityManagerFactoryUtils.getTransactionalEntityManager(emf, getJpaPropertyMap());
	}

}
