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

package org.springframework.boot.autoconfigure.couchbase;

import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.env.ClusterEnvironment.Builder;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Timeouts;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Couchbase.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Yulin Qin
 * @since 1.4.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Cluster.class)
@ConditionalOnProperty("spring.couchbase.connection-string")
@EnableConfigurationProperties(CouchbaseProperties.class)
public class CouchbaseAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ClusterEnvironment couchbaseClusterEnvironment(CouchbaseProperties properties,
			ObjectProvider<ClusterEnvironmentBuilderCustomizer> customizers) {
		Builder builder = initializeEnvironmentBuilder(properties);
		customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
		return builder.build();
	}

	@Bean(destroyMethod = "disconnect")
	@ConditionalOnMissingBean
	public Cluster couchbaseCluster(CouchbaseProperties properties, ClusterEnvironment couchbaseClusterEnvironment) {
		ClusterOptions options = ClusterOptions.clusterOptions(properties.getUsername(), properties.getPassword())
				.environment(couchbaseClusterEnvironment);
		return Cluster.connect(properties.getConnectionString(), options);
	}

	private ClusterEnvironment.Builder initializeEnvironmentBuilder(CouchbaseProperties properties) {
		Timeouts timeouts = properties.getEnv().getTimeouts();
		CouchbaseProperties.Io io = properties.getEnv().getIo();
		CouchbaseProperties.Ssl ssl = properties.getEnv().getSsl();

		ClusterEnvironment.Builder builder = ClusterEnvironment.builder();

		builder.timeoutConfig(TimeoutConfig.kvTimeout(timeouts.getKeyValue()).analyticsTimeout(timeouts.getAnalytics())
				.kvDurableTimeout(timeouts.getKeyValueDurable()).queryTimeout(timeouts.getQuery())
				.viewTimeout(timeouts.getView()).searchTimeout(timeouts.getSearch())
				.managementTimeout(timeouts.getManagement()).connectTimeout(timeouts.getConnect())
				.disconnectTimeout(timeouts.getDisconnect()));

		builder.ioConfig(IoConfig.maxHttpConnections(io.getMaxEndpoints()).numKvConnections(io.getMinEndpoints())
				.idleHttpConnectionTimeout(io.getIdleHttpConnectionTimeout()));

		if (ssl.getEnabled()) {
			try {
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
				store.load(new FileInputStream(ssl.getKeyStore()), ssl.getKeyStorePassword().toCharArray());
				tmf.init(store);
				builder.securityConfig(SecurityConfig.enableTls(true).trustManagerFactory(tmf));
			} catch (Exception ex) {
				throw new CouchbaseException("Could not enable SSL", ex);
			}
		}

		return builder;
	}

}
