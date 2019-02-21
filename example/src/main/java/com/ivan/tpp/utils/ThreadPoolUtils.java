package com.ivan.tpp.utils;


import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

public class ThreadPoolUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtils.class);
	
	private ThreadPoolUtils(){}
	
	public static ExecutorService newFixedThreadArrayPool(int corePoolSize, int maximumPoolSize,long keepAliveTime,int queueSize){
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(queueSize), new DelayTryRunPolicy(1000));
	}
	
	public static class DelayTryRunPolicy implements RejectedExecutionHandler{
		
		private long delayTime;
		
		public DelayTryRunPolicy(){
		}
		
		public DelayTryRunPolicy(long delayTime){
			this.delayTime = delayTime;
		}
		
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			if (!e.isShutdown()) {
                try {
					if(e.getQueue().poll(delayTime,TimeUnit.SECONDS) != null){
						e.execute(r);
					}
				} catch (InterruptedException e1) {
					log.error("",e1);
				}
            }
		}

		public long getDelayTime() {
			return delayTime;
		}

		public void setDelayTime(long delayTime) {
			this.delayTime = delayTime;
		}

	}
	
	public static void main(String[] args) {
		
//		Map<String,String> mm = Maps.newHashMap();
//		mm.put("dataType", "carrier");
//		mm.put("reportIsSuccess", "true");
//		mm.put("productType", "XJD");
//		mm.put("customerId", "xxxxxxxxxxxx");
//		
//		System.out.println(JSON.toJSON(mm));
		
//		final int COUNT_BITS = Integer.SIZE - 3;
//		int pwo = (int) (Math.pow(2, 29) - 1);
//		System.out.println((1 << COUNT_BITS) - 1);
//		System.out.println(pwo);
//		System.out.println(Integer.toBinaryString(-1));
//		final int RUNNING    = -1 << COUNT_BITS;
//		final int SHUTDOWN   =  0 << COUNT_BITS;
//		final int STOP       =  1 << COUNT_BITS;
//		final int TIDYING    =  2 << COUNT_BITS;
//		final int TERMINATED =  3 << COUNT_BITS;
//		System.out.println("RUNNING:\t"+RUNNING);
//		System.out.println("SHUTDOWN:\t"+SHUTDOWN);
//		System.out.println("STOP:\t"+STOP);
//		System.out.println("TIDYING:\t"+TIDYING);
//		System.out.println("TERMINATED:\t"+TERMINATED);
		
		ExecutorService executorService = newFixedThreadArrayPool(0, 1, 10000, 20);
		executorService.execute(new ThreadRun());
//		executorService.execute(new ThreadRun());
//		executorService.shutdown();
		
//		AtomicInteger exp = new AtomicInteger(-536870912);
//		System.out.println(exp.compareAndSet(-536870912, -536870912 + 1));
		int line = (1 << 16) - 1;
		System.out.println(fill(line));
		
	}
	
	public static String fill(int num){
		String jinzhi = Integer.toBinaryString(num);
		StringBuffer sf = new StringBuffer();
		for(int i = 0; i < 32-jinzhi.length(); i++){
			sf = sf.append("0");
		}
		return sf.append(jinzhi).toString();
	}
	
	public static void showThreadState(){
		System.out.println(Integer.toBinaryString(-536870911));
		int COUNT_BITS = Integer.SIZE - 3;
		int temp = (1 << COUNT_BITS) - 1;
		System.out.println(Integer.toBinaryString(536870911));
		System.out.println(Integer.toBinaryString(~temp));
		System.out.println(Integer.toBinaryString(temp));
		System.out.println(Integer.toBinaryString(-1 << COUNT_BITS));
		System.out.println(-1 << COUNT_BITS);
		System.out.println(fill(temp));
		System.out.println(fill(0));
		System.out.println("========================");
		System.out.println(fill(-1));
		System.out.println(Integer.toBinaryString(-1));
	}
}

class ThreadRun implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
	}
	
}