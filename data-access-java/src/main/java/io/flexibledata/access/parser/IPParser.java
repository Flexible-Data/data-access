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
package io.flexibledata.access.parser;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ResourceUtils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;

import io.flexibledata.access.event.Area;
import lombok.extern.slf4j.Slf4j;

/**
 * Ip解析器，将IP地址解析成省市区
 * 
 * @author tan.jie
 *
 */
@Slf4j
public class IPParser {

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static Area parseIp(final String ip) throws IOException {
		// File database = new File("classpath:db/GeoIP2-City.mmdb");
		File database = ResourceUtils.getFile("classpath:db/GeoLite2-City.mmdb");
		DatabaseReader reader = new DatabaseReader.Builder(database).build();
		InetAddress ipAddress = InetAddress.getByName(ip);

		Area result = new Area();
		CityResponse response = null;
		try {
			response = reader.city(ipAddress);
		} catch (GeoIp2Exception e) {
			log.error("Can't parse ip {}", ip, e);
			return result;
		}

		Country country = response.getCountry();
		result.setCountry(country.getNames().get("zh-CN"));

		Subdivision subdivision = response.getMostSpecificSubdivision();
		result.setProvince(subdivision.getNames().get("zh-CN"));

		City city = response.getCity();
		result.setCity(city.getNames().get("zh-CN"));

		Location location = response.getLocation();
		result.setLatitude(location.getLatitude());
		result.setLongitude(location.getLongitude());

		return result;
	}

	public static void main(String[] args) {
		try {
			Area area = IPParser.parseIp("219.137.206.103");
			System.out.println(area);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
