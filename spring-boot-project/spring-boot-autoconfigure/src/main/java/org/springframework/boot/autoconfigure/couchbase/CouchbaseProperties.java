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

import java.time.Duration;

import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.core.env.TimeoutConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Configuration properties for Couchbase.
 *
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Yulin Qin
 * @author Brian Clozel
 * @since 1.4.0
 */
@ConfigurationProperties(prefix = "spring.couchbase")
public class CouchbaseProperties {

	/**
	 * Couchbase nodes (host or IP address) to bootstrap from.
	 */
	private String connectionString;

	/**
	 * Cluster username when using role based access.
	 */
	private String username;

	/**
	 * Cluster password when using role based access.
	 */
	private String password;

	private String bucketname;

	private final Env env = new Env();

	public Env getEnv() {
		return this.env;
	}

	public String getConnectionString() {
		return this.connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBucketname() {
		return this.bucketname;
	}

	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	public static class Env {
		private final Timeouts timeouts = new Timeouts();
		private final Io io = new Io();

		public Timeouts getTimeouts() {
			return this.timeouts;
		}

		public Io getIo() {
			return io;
		}
	}

	public static class Io {

		private int numKvConnections = IoConfig.DEFAULT_NUM_KV_CONNECTIONS;
		private int maxHttpConnections = IoConfig.DEFAULT_MAX_HTTP_CONNECTIONS;
		private Duration idleHttpConnectionTimeout = IoConfig.DEFAULT_IDLE_HTTP_CONNECTION_TIMEOUT;

		public int getNumKvConnections() {
			return numKvConnections;
		}

		public void setNumKvConnections(int numKvConnections) {
			this.numKvConnections = numKvConnections;
		}

		public int getMaxHttpConnections() {
			return maxHttpConnections;
		}

		public void setMaxHttpConnections(int maxHttpConnections) {
			this.maxHttpConnections = maxHttpConnections;
		}

		public Duration getIdleHttpConnectionTimeout() {
			return idleHttpConnectionTimeout;
		}

		public void setIdleHttpConnectionTimeout(Duration idleHttpConnectionTimeout) {
			this.idleHttpConnectionTimeout = idleHttpConnectionTimeout;
		}
	}

	public static class Timeouts {

		/**
		 * Bucket connect timeouts.
		 */
		private Duration connect = TimeoutConfig.DEFAULT_CONNECT_TIMEOUT;

		/**
		 * Bucket disconnect timeouts.
		 */
		private Duration disconnect = TimeoutConfig.DEFAULT_DISCONNECT_TIMEOUT;

		/**
		 * operations performed on a specific key timeout.
		 */
		private Duration keyValue = TimeoutConfig.DEFAULT_KV_TIMEOUT;

		/**
		 * operations performed on a specific key timeout.
		 */
		private Duration keyValueDurable = TimeoutConfig.DEFAULT_KV_DURABLE_TIMEOUT;

		/**
		 * N1QL query operations timeout.
		 */
		private Duration query = TimeoutConfig.DEFAULT_QUERY_TIMEOUT;

		/**
		 * Regular and geospatial view operations timeout.
		 */
		private Duration view = TimeoutConfig.DEFAULT_VIEW_TIMEOUT;

		/**
		 * Timeout for the search service.
		 */
		private Duration search = TimeoutConfig.DEFAULT_SEARCH_TIMEOUT;

		/**
		 * Timeout for the analytics service.
		 */
		private Duration analytics = TimeoutConfig.DEFAULT_ANALYTICS_TIMEOUT;

		/**
		 * Timeout for the management operations.
		 */
		private Duration management = TimeoutConfig.DEFAULT_MANAGEMENT_TIMEOUT;

		public Duration getConnect() {
			return this.connect;
		}

		public void setConnect(Duration connect) {
			this.connect = connect;
		}

		public Duration getKeyValue() {
			return this.keyValue;
		}

		public void setKeyValue(Duration keyValue) {
			this.keyValue = keyValue;
		}

		public Duration getQuery() {
			return this.query;
		}

		public void setQuery(Duration query) {
			this.query = query;
		}

		public Duration getView() {
			return this.view;
		}

		public void setView(Duration view) {
			this.view = view;
		}

		public Duration getDisconnect() {
			return disconnect;
		}

		public void setDisconnect(Duration disconnect) {
			this.disconnect = disconnect;
		}

		public Duration getKeyValueDurable() {
			return keyValueDurable;
		}

		public void setKeyValueDurable(Duration keyValueDurable) {
			this.keyValueDurable = keyValueDurable;
		}

		public Duration getSearch() {
			return search;
		}

		public void setSearch(Duration search) {
			this.search = search;
		}

		public Duration getAnalytics() {
			return analytics;
		}

		public void setAnalytics(Duration analytics) {
			this.analytics = analytics;
		}

		public Duration getManagement() {
			return management;
		}

		public void setManagement(Duration management) {
			this.management = management;
		}
	}

}
