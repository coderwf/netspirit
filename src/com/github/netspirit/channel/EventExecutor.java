package com.github.netspirit.channel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventExecutor extends Thread implements Executor{
    
	private BlockingQueue<Runnable> taskQs = new LinkedBlockingQueue<Runnable>();
	
	@Override
	public void execute(Runnable runner) {
		//通过这个接口执行的任务只会被该线程执行
		if(inEventLoop()) {
			runner.run();
			return ;
		}
		
		addTask(runner);
		
	}
    
	private void addTask(Runnable task) {
		try {
			taskQs.put(task);
		}catch(InterruptedException e) {
			//todo
		}
		
	}
	
	public final boolean inEventLoop() {
		return Thread.currentThread().getId() == getId();
	}
	
	@Override
	public void run() {
		
	}
	
}
