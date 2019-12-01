package com.excercise;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReaderThread implements Runnable {

	Lock readerLock, writerLock;
	AtomicInteger readerCount, writerCount;
	SharedData sharedData;

/** Constuctor to initilize RederThread*/
	public ReaderThread(Lock readerLock, Lock writerLock, AtomicInteger readerCount, AtomicInteger writerCount,
			SharedData sharedData) {
		super();
		this.readerLock = readerLock;
		this.writerLock = writerLock;
		this.readerCount = readerCount;
		this.writerCount = writerCount;
		this.sharedData = sharedData;
	}

	@Override
	public void run() {
		while (true) {
			if (writerCount.get() == 0) {
				try {
					readerLock.tryLock();
					readerCount.incrementAndGet();
					sharedData.criticalSection();
					break;
				} finally {
					int decrementAndGet = readerCount.decrementAndGet();
					if (decrementAndGet == 0) {
						readerLock.unlock();
					}
				}
			}
		}
	}
}

class WriterThread implements Runnable {

	Lock readerLock, writerLock;
	AtomicInteger readerCount, writerCount;
	SharedData sharedData;

	public WriterThread(Lock readerLock, Lock writerLock, AtomicInteger readerCount, AtomicInteger writerCount,
			SharedData sharedData) {
		super();
		this.readerLock = readerLock;
		this.writerLock = writerLock;
		this.readerCount = readerCount;
		this.writerCount = writerCount;
		this.sharedData = sharedData;

	}

	@Override
	public void run() {
		while (true) {
			if (writerCount.get() == 0 && readerCount.get() == 0) {

				if (writerLock.tryLock()) {
					try {
						writerCount.incrementAndGet();
						sharedData.criticalSection();
						break;
					} finally {
						writerCount.decrementAndGet();
						writerLock.unlock();
					}

				} else {
					System.out.println("Write lock is in use");
				}
			}
		}
	}

}

class SharedData {

	void criticalSection() {
		System.out.println("Starting "+Thread.currentThread().getName());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Ending "+Thread.currentThread().getName());

	}
}

public class ReaderWriterLock {

	public static void main(String[] args) {
		Lock readerLock = new ReentrantLock(), writerLock = new ReentrantLock();
		AtomicInteger readerCount = new AtomicInteger(0), writerCount = new AtomicInteger(0);
		SharedData sharedData = new SharedData();

		ReaderThread readerThread1 = new ReaderThread(readerLock, writerLock, readerCount, writerCount, sharedData);
		ReaderThread readerThread2 = new ReaderThread(readerLock, writerLock, readerCount, writerCount, sharedData);
		ReaderThread readerThread3 = new ReaderThread(readerLock, writerLock, readerCount, writerCount, sharedData);

		WriterThread writerThread1 = new WriterThread(readerLock, writerLock, readerCount, writerCount, sharedData);
		WriterThread writerThread2 = new WriterThread(readerLock, writerLock, readerCount, writerCount, sharedData);
		WriterThread writerThread3 = new WriterThread(readerLock, writerLock, readerCount, writerCount, sharedData);

		Thread rt1 = new Thread(readerThread1, "Reader 1");
		Thread rt2 = new Thread(readerThread2, "Reader 2");
		Thread rt3 = new Thread(readerThread3, "Reader 3");

		Thread wt1 = new Thread(writerThread1, "Writer 1");
		Thread wt2 = new Thread(writerThread2, "Writer 2");
		Thread wt3 = new Thread(writerThread3, "Writer 3");

		rt1.start();
		wt1.start();
		wt2.start();
		rt2.start();
		wt3.start();
		rt3.start();
	}

}
