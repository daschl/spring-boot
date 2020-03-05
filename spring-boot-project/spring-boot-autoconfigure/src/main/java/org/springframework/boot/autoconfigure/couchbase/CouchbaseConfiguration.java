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
import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.couchbase.client.java.ClusterOptions.clusterOptions;

/**
 * Support class to configure Couchbase based on {@link CouchbaseProperties}.
 *
 * @author Stephane Nicoll
 * @author Brian Clozel
 * @since 2.1.0
 */
@Configuration
public class CouchbaseConfiguration {

	private final CouchbaseProperties properties;

	public CouchbaseConfiguration(CouchbaseProperties properties) {
		this.properties = properties;
	}

	@Bean
	@Primary
	public ClusterEnvironment couchbaseEnvironment() {
		return initializeEnvironmentBuilder(this.properties).build();
	}

	@Bean
	@Primary
	public Cluster couchbaseCluster() {
		ClusterOptions options = clusterOptions(this.properties.getUsername(), this.properties.getPassword())
				.environment(couchbaseEnvironment());

		return Cluster.connect(this.properties.getConnectionString(), options);
	}

	@Bean
	@Primary
	public Bucket couchbaseBucket() {
		return couchbaseCluster().bucket(this.properties.getBucketname());
	}

	/**
	 * Initialize an environment builder based on the specified settings.
	 * @param properties the couchbase properties to use
	 * @return the {@link ClusterEnvironment} builder.
	 */
	protected ClusterEnvironment.Builder initializeEnvironmentBuilder(final CouchbaseProperties properties) {
		CouchbaseProperties.Timeouts timeouts = properties.getEnv().getTimeouts();
		CouchbaseProperties.Io io = properties.getEnv().getIo();

		ClusterEnvironment.Builder builder = ClusterEnvironment.builder();

		builder.timeoutConfig(TimeoutConfig
				.kvTimeout(timeouts.getKeyValue())
				.analyticsTimeout(timeouts.getAnalytics())
				.kvDurableTimeout(timeouts.getKeyValueDurable())
				.queryTimeout(timeouts.getQuery())
				.viewTimeout(timeouts.getView())
				.searchTimeout(timeouts.getSearch())
				.managementTimeout(timeouts.getManagement())
				.connectTimeout(timeouts.getConnect())
				.disconnectTimeout(timeouts.getDisconnect())
		);

		builder.ioConfig(IoConfig
				.maxHttpConnections(io.getMaxHttpConnections())
				.numKvConnections(io.getNumKvConnections())
				.idleHttpConnectionTimeout(io.getIdleHttpConnectionTimeout())
		);

		return builder;
	}

}
