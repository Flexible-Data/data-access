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
package io.flexibledata.access.shipper;

import io.flexibledata.access.constant.ShipperTypeConstant;
import io.flexibledata.access.mq.MessageQueue;
import io.flexibledata.access.shipper.file.FileShipperService;
import io.flexibledata.access.shipper.kafka.KafkaShipperService;

/**
 * 投递工厂，创建投递者服务
 * 
 * @author tan.jie
 *
 */
public class ShipperServiceFactory {
	public void createShipperSerivce(final ShipperRegistryVo registryVo, final MessageQueue<String> queue) {
		AbstractShipperService shipperService = null;
		switch (registryVo.getType()) {
		case ShipperTypeConstant.SHIPPER_TYPE_FILE:
			shipperService = new FileShipperService(queue);
			break;
		case ShipperTypeConstant.SHIPPER_TYPE_KAFKA:
			shipperService = new KafkaShipperService(registryVo, queue);
			break;
		case ShipperTypeConstant.SHIPPER_TYPE_DB:
			break;
		default:
			break;
		}
		shipperService.ship();
	}
}
