package com.excercise;

import java.util.concurrent.atomic.AtomicBoolean;

class EvenThread implements Runnable {

	private Object lock;
	private AtomicBoolean flag;
	private int i = 2;
	/** Constructor to initilize EventThread*/
	public EvenThread(Object lock, AtomicBoolean flag) {
		super();
		this.lock = lock;
		this.flag = flag;
	}

	@Override
	public void run() {

		while (i<20) {
			synchronized (lock) {
				while (flag.get()) {
					System.out.println("in even while : "+flag);

					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(i);
				i = i + 2;
				flag.set(true);
				System.out.println("in even while set"+flag);

				lock.notify();
			}
		}

	}

}

class OddThread implements Runnable {

	private Object lock;
	private AtomicBoolean flag;
	private int i=1;
	public OddThread(Object lock, AtomicBoolean flag) {
		super();
		this.lock = lock;
		this.flag = flag;
	}

	@Override
	public void run() {
		while (i<20) {
			synchronized (lock) {
				while (!flag.get()) {
					System.out.println("in odd while "+flag);
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(i);
				i = i + 2;
				flag.set(false);
				System.out.println("in odd while set"+flag);

				lock.notify();
			}
		}
	}

}

public class EvenOddThread {

	public static void main(String[] args) {
		
		Object lock=new Object();
		AtomicBoolean flag=new AtomicBoolean(true);
		
		EvenThread evenThread=new EvenThread(lock,flag);
		OddThread oddThread=new OddThread(lock,flag);

		Thread evenTh = new Thread(evenThread,"Event Thread");
		evenTh.start();
		Thread oddTh = new Thread(oddThread,"Odd Thread");
		oddTh.start();


	}

}
