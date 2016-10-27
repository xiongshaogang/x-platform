package com.xplatform.base.system.log.aop;

import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


class WorkQueue {
	private final int nThreads;
	private final PoolWorker[] threads;
	LinkedList<Runnable> queue;

	public WorkQueue(int nThreads) {
		this.nThreads = nThreads;
		this.queue = new LinkedList<Runnable>();
		this.threads = new PoolWorker[nThreads];
		for (int i = 0; i < this.nThreads; i++) {
			this.threads[i] = new PoolWorker();
			this.threads[i].start();
		}
	}

	public void execute(Runnable r) {
		synchronized (this.queue) {
			this.queue.addLast(r);
			this.queue.notify();
		}
	}

	private class PoolWorker extends Thread {
		private Log logger = LogFactory.getLog(PoolWorker.class);

		private PoolWorker() {
			
		}

		public void run() {
			while (true) {
				Runnable r;
				synchronized (WorkQueue.this.queue) {
					try {
						WorkQueue.this.queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						this.logger.error(e.getMessage());
					}
					if (WorkQueue.this.queue.isEmpty()) {
						continue;
					}

					r = (Runnable) WorkQueue.this.queue.removeFirst();
				}
				try {
					r.run();
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error(e.getMessage());
				}
			}
		}
	}
}

