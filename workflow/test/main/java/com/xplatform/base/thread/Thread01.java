package com.xplatform.base.thread;

class Ticket extends Thread{
	private int num=100;
	public void run(){
		while(true){
			if(num>0){
				System.out.println(Thread.currentThread().getName()+"---"+num--);
			}
		}
	}
}
public class Thread01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ticket t1=new Ticket();
		/*Ticket t2=new Ticket();
		Ticket t3=new Ticket();
		Ticket t4=new Ticket();*/
		t1.start();
		t1.start();
		t1.start();
		t1.start();
	}

}
