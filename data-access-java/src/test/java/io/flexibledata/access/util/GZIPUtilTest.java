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
package io.flexibledata.access.util;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 * @author tan.jie
 *
 */
public final class GZIPUtilTest {
	private static final String plainText = "hello,world";
	private static final String cipherText = "[B@69d0a921";
	private static final String charset = "UTF-8";

	@Test
	public void assertCompress() throws UnsupportedEncodingException {
		/*byte[] compress = GZIPUtil.compress(plainText);
		String result = compress.toString();
		System.out.println(result);
		byte[] result01 = GZIPUtil.uncompress(compress);
		System.out.println(result01.toString());
		assertEquals(result, cipherText);*/
	}

	@Test
	public void assertUncompress() throws UnsupportedEncodingException {
		/*
		 * byte[] uncompress = GZIPUtil.uncompress(cipherText.getBytes()); String result
		 * = new String(uncompress, charset); System.out.println();
		 */
		// assertEquals(result, plainText);
	}
}
