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
package io.flexibledata.access.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.google.gson.Gson;

import io.flexibledata.access.constant.Channel;
import io.flexibledata.access.event.Area;
import io.flexibledata.access.event.Event;
import io.flexibledata.access.mapping.IDMappingService;
import io.flexibledata.access.mq.MessageQueue;
import io.flexibledata.access.parser.IPParser;
import lombok.extern.slf4j.Slf4j;

/**
 * 前端数据处理器
 * 
 * @author tan.jie
 *
 */
@Slf4j
@Component
public class HttpHandler {
	private static final String HTTP_PARAM_DATA = "data";
	private static final String ENCODE_UTF_8 = "UTF-8";

	@Autowired
	private IDMappingService idMappingService;

	@Autowired
	private MessageQueue<String> queue;

	public void handlerWebRequest(HttpServletRequest request) {
		Area area = extractArea(request);

		String dataWitEncode = request.getParameter(HTTP_PARAM_DATA);
		byte[] bytes = Base64Utils.decodeFromString(dataWitEncode);
		String originData = null;
		try {
			originData = new String(bytes, ENCODE_UTF_8);
		} catch (UnsupportedEncodingException e) {
			log.error("Can not encoding by utf-8 with {}.", originData);
		}
		final Gson gson = new Gson();
		Event event = gson.fromJson(originData, Event.class);
		log.debug("Decoded data is {}", originData);
		event.setArea(area);
		log.debug("Data after parsed Ip is {}", event);

		idMappingService.idMapping(event, Channel.Web);

		String jsonEvent = gson.toJson(event);
		
		boolean success = queue.push(jsonEvent);
		if (!success) {
			try {
				Thread.sleep(30 * 1000);
				queue.push(jsonEvent);
			} catch (InterruptedException e) {
				log.error("Program exception interrupt.", e);
			}
		}
	}

	public void handlerMobileRequest(HttpServletRequest request) {
		String dataWitEncode = request.getParameter(HTTP_PARAM_DATA);
		byte[] bytes = Base64Utils.decodeFromString(dataWitEncode);
		String data = null;
		try {
			data = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		System.out.println(data);
	}

	@Async
	public Area extractArea(HttpServletRequest request) {
		// String ip = IPParser.getIpAddress(request);
		String ip = "219.137.206.103";
		Area area = null;
		try {
			area = IPParser.parseIp(ip);
		} catch (IOException e) {
			log.error("Parsing IP error. IP is {}", ip, e);
		}
		return area;
	}

}
