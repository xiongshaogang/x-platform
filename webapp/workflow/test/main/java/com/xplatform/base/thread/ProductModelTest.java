package com.xplatform.base.thread;

class Resource{
	public String name;
	public String sex;
	public boolean flag=false;
}
class Input implements Runnable {
	private Resource r;
	public Input(Resource r){
		this.r=r;
	}
	public void run(){
		int x=0;
		while(true){
			synchronized (r) {
				if(r.flag){
					try{r.wait();}catch(InterruptedException e){}
				}
				if(x==0){
					r.name="张三";
					r.sex="男男男男男男男";
				}else if(x==1){
					r.name="小兰";
					r.sex="女";
				}
				r.flag=true;
				r.notify();
			}
			x = (x+1)%2;
		}
	}
}

class Output implements Runnable {
	private Resource r;
	public Output(Resource r){
		this.r=r;
	}
	public void run(){
		while(true){
			synchronized (r) {
				if(!r.flag){
					try{r.wait();}catch(InterruptedException e){}
				}
				System.out.println("姓名:"+r.name+";性别："+r.sex);
				r.flag=false;
				r.notify();
			}
			
		}
	}
}
public class ProductModelTest {

	public static void main(String[] args) {
		Resource r=new Resource();
		
		Input i=new Input(r);
		Output o=new Output(r);
		
		Thread t1=new Thread(i);
		Thread t2=new Thread(o);
		
		t1.start();
		t2.start();
	}

}
