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
package io.flexibledata.access.mq;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息队列
 * 
 * @author tan.jie
 *
 */
@Slf4j
public class MessageQueue<T> {
	// 队列大小
	public static final int QUEUE_MAX_SIZE = 10000;
	private static MessageQueue queue = new MessageQueue();
	// 阻塞队列
	private BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<T>(QUEUE_MAX_SIZE);

	public static MessageQueue getInstance() {
		return queue;
	}

	public boolean push(T message) {
		return this.blockingQueue.offer(message);
	}

	public T pop() {
		T result = null;
		try {
			result = this.blockingQueue.take();
		} catch (InterruptedException e) {
			log.error("获取元素异常！", e);
		}
		return result;
	}

	public List<T> bulkTake(List<T> list, Integer size) {
		this.blockingQueue.drainTo(list, size);
		return list;
	}

	public int size() {
		return this.blockingQueue.size();
	}
}
