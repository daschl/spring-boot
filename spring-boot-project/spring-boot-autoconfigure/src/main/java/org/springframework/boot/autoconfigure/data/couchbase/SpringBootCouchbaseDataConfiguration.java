/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.data.couchbase;

import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.config.CouchbaseConfigurer;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.core.mapping.Document;

/**
 * Configure Spring Data's couchbase support.
 *
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnMissingBean(AbstractCouchbaseConfiguration.class)
class SpringBootCouchbaseDataConfiguration extends AbstractCouchbaseConfiguration {

	private final ApplicationContext applicationContext;

	private final CouchbaseProperties properties;

	private final CouchbaseDataProperties dataProperties;

	private final CouchbaseConfigurer couchbaseConfigurer;

	SpringBootCouchbaseDataConfiguration(ApplicationContext applicationContext, CouchbaseProperties properties,
			CouchbaseDataProperties dataProperties, CouchbaseConfigurer couchbaseConfigurer) {
		this.applicationContext = applicationContext;
		this.properties = properties;
		this.dataProperties = dataProperties;
		this.couchbaseConfigurer = couchbaseConfigurer;
	}

	@Override
	protected CouchbaseConfigurer couchbaseConfigurer() {
		return this.couchbaseConfigurer;
	}

	@Override
	public String getConnectionString() {
		return this.properties.getConnectionString();
	}

	@Override
	public String getUserName() {
		return this.properties.getUsername();
	}

	@Override
	public String getPassword() {
		return this.properties.getPassword();
	}

	@Override
	public String getBucketName() {
		return this.dataProperties.getBucketName();
	}

	@Override
	protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
		return new EntityScanner(this.applicationContext).scan(Document.class, Persistent.class);
	}

	@Override
	public String typeKey() {
		return this.dataProperties.getTypeKey();
	}

	@Override
	@ConditionalOnMissingBean(name = BeanNames.COUCHBASE_TEMPLATE)
	@Bean(name = BeanNames.COUCHBASE_TEMPLATE)
	public CouchbaseTemplate couchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
			MappingCouchbaseConverter mappingCouchbaseConverter) {
		return super.couchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
	}

	@Override
	@ConditionalOnMissingBean(name = BeanNames.REACTIVE_COUCHBASE_TEMPLATE)
	@Bean(name = BeanNames.REACTIVE_COUCHBASE_TEMPLATE)
	public ReactiveCouchbaseTemplate reactiveCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
			MappingCouchbaseConverter mappingCouchbaseConverter) {
		return super.reactiveCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
	}

	@Override
	@ConditionalOnMissingBean(name = BeanNames.COUCHBASE_CUSTOM_CONVERSIONS)
	@Bean(name = BeanNames.COUCHBASE_CUSTOM_CONVERSIONS)
	public CustomConversions customConversions() {
		return super.customConversions();
	}

}
