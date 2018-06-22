/*
 * Copyright 2016-2018 flexibledata.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package io.flexibledata.access.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.base.Preconditions;

import io.flexibledata.access.mq.MessageQueue;
import io.flexibledata.access.shipper.ShipperRegistryVo;

/**
 * @author tan.jie
 *
 */
@Configuration
public class RootContextConfig {

	@Autowired
	private Environment env;

	@Bean
	public MessageQueue<String> messageQueue() {
		return new MessageQueue<String>();
	}

	@Bean
	public ShipperRegistryVo shipperRegistryVo(Environment env) {
		String type = env.getProperty("spring.shipper.type");
		Preconditions.checkNotNull(type, "Shipper type can't be Null!");
		String servers = env.getProperty("spring.shipper.kafka.servers");
		String topic = env.getProperty("spring.shipper.kafka.topic");
		String url = env.getProperty("spring.shipper.db.url");
		String username = env.getProperty("spring.shipper.db.username");
		String password = env.getProperty("spring.shipper.db.password");
		ShipperRegistryVo result = new ShipperRegistryVo();
		result.setType(type);
		result.setServers(servers);
		result.setTopic(topic);
		result.setUrl(url);
		result.setUsername(username);
		result.setPassword(password);
		return result;
	}
}
