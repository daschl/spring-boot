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

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

/**
 * Configuration for Couchbase-related beans that depend on a
 * {@link CouchbaseClientFactory}.
 *
 * @author Stephane Nicoll
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(CouchbaseClientFactory.class)
class CouchbaseClientFactoryDependentConfiguration {

	@Bean
	@ConditionalOnMissingBean
	CouchbaseTemplate couchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
			MappingCouchbaseConverter mappingCouchbaseConverter) {
		return new CouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
	}

	@Bean
	@ConditionalOnMissingBean
	ReactiveCouchbaseTemplate reactiveCouchbaseTemplate(CouchbaseClientFactory couchbaseClientFactory,
			MappingCouchbaseConverter mappingCouchbaseConverter) {
		return new ReactiveCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
	}

	@Bean
	@ConditionalOnMissingBean
	RepositoryOperationsMapping couchbaseRepositoryOperationsMapping(CouchbaseTemplate couchbaseTemplate) {
		return new RepositoryOperationsMapping(couchbaseTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	ReactiveRepositoryOperationsMapping reactiveCouchbaseRepositoryOperationsMapping(
			ReactiveCouchbaseTemplate reactiveCouchbaseTemplate) {
		return new ReactiveRepositoryOperationsMapping(reactiveCouchbaseTemplate);
	}

}
