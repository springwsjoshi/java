package com.excercise;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class EvenReentrantThread implements Runnable {

	private Lock lock;
	private AtomicBoolean flag;
	private int i = 2;

	public EvenReentrantThread(Lock lock, AtomicBoolean flag) {
		super();
		this.lock = lock;
		this.flag = flag;
	}

	@Override
	public void run() {
		while (i < 20) {

			if (flag.get()) {
				lock.lock();
				try {
					System.out.println(i);
					i = i + 2;
					flag.set(false);
				} finally {
					lock.unlock();
				}
			}
		}
	}

}

class OddReentrantThread implements Runnable {

	private Lock lock;
	private AtomicBoolean flag;
	private int i = 1;

	public OddReentrantThread(Lock lock, AtomicBoolean flag) {
		super();
		this.lock = lock;
		this.flag = flag;
	}

	@Override
	public void run() {

		while (i < 20) {
			if(!flag.get()) {
				try {
					lock.lock();
					System.out.println(i);
					i=i+2;
					flag.set(true);
				}finally {
					lock.unlock();
				}
			}
		}
	}

}

public class ReentranEvenThread {

	public static void main(String[] args) {
		Lock lock=new ReentrantLock();
		AtomicBoolean flag=new AtomicBoolean(false);
		EvenReentrantThread evenReentrantThread = new EvenReentrantThread(lock,flag);
		OddReentrantThread oddReentrantThread = new OddReentrantThread(lock,flag);

		Thread evenThread1 = new Thread(evenReentrantThread);
		Thread oddThread1 = new Thread(oddReentrantThread);
		evenThread1.start();
		oddThread1.start();
		
	}

}
