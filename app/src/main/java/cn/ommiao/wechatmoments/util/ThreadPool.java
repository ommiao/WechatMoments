package cn.ommiao.wechatmoments.util;


import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private static class LazyHolder {
        static final MyThreadPoolExecutor HTTP_THREAD_POOL = new MyThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {

            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "HttpThread #" + mCount.getAndIncrement());
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            }
        });
    }

    public static MyThreadPoolExecutor getHttpExecutor() {
        return LazyHolder.HTTP_THREAD_POOL;
    }

    public interface PoolExecutorListener {

        void afterExecute(Runnable r, Throwable t);

        void beforeExecute(Thread t, Runnable r);
    }

    public static class MyThreadPoolExecutor extends ThreadPoolExecutor {
        private final Vector<PoolExecutorListener> listeners = new Vector<PoolExecutorListener>();

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        public BlockingQueue<Runnable> getQueue() {
            return super.getQueue();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            for (PoolExecutorListener l : listeners) {
                l.afterExecute(r, t);
            }
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            for (PoolExecutorListener l : listeners) {
                l.beforeExecute(t, r);
            }
        }

        public void registerPoolExecutorListener(PoolExecutorListener listener) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public void unRegisterPoolExecutorListener(PoolExecutorListener listener) {
            listeners.remove(listener);
        }
    }

    public static void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}