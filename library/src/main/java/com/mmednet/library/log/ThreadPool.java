package com.mmednet.library.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Title:ThreadPool
 * <p>
 * Description:线程池
 * </p>
 * Author Jming.L
 * Date 2017/8/29 11:56
 */
class ThreadPool {
	
    private static ExecutorService threadPool;

    public static void initThreadPool(int max){ // 可以在Application中进行配置
        if(max > 0){
            max = max < 3 ? 3 : max;
            threadPool = Executors.newFixedThreadPool(max);
        }else{
            threadPool = Executors.newCachedThreadPool();
        }
    }

    public static ExecutorService getInstances(){
        return threadPool;
    }

    public synchronized static<U, R> void execute(RunTask<U, R> runtask){
        if(null == threadPool){
            threadPool = Executors.newCachedThreadPool();
        }
        threadPool.execute(runtask);
    }

    public synchronized static void execute(Runnable runnable){
        if(null == threadPool){
            threadPool = Executors.newCachedThreadPool();
        }
        threadPool.execute(runnable);
    }
    

}
