package com.aike.concurrent;

import com.alibaba.druid.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 任务执行线程池
 */
@Slf4j
public class MyExecutors {
    private static final MyExecutors instance = new MyExecutors();

    /**
     * 缺省核心线程数
     */
    private static final int THREAD_CORE = 6;
    /**
     * 缺省最大线程数
     */
    private static final int THREAD_MAX = 12;
    /**
     * 缺省空闲间隔
     */
    private static final int THREAD_IDLE = 30;
    /**
     * 缺省队列大小
     */
    private static final int THREAD_CAPACITY = 1024;

    public static MyExecutors get() {
        return instance;
    }

    /**
     * 公共事件线程池
     */
    private static ExecutorService commonService = null;
    private static ExecutorService rabbitMQService = null;
    private static ExecutorService singleService = null;

    static {
        commonService = new ThreadPoolExecutor(THREAD_CORE, THREAD_MAX,
                THREAD_IDLE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(THREAD_CAPACITY * 2),
                new DaemonThreadFactory("commonService"),
                new RejectedExecutionHandlerImpl());

        rabbitMQService = new ThreadPoolExecutor(THREAD_CORE, THREAD_MAX,
                THREAD_IDLE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(THREAD_CAPACITY * 2),
                new DaemonThreadFactory("rabbitMQService"),
                new RejectedExecutionHandlerImpl());

        singleService = new ThreadPoolExecutor(1, 1,
                THREAD_IDLE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(15),
                new DaemonThreadFactory("singleService"),
                new RejectedExecutionHandlerImpl());
    }

    public ExecutorService getCommonService() {
        return commonService;
    }

    public ExecutorService getRabbitMQService() {
        return rabbitMQService;
    }

    public ExecutorService getSingleService() {
        return singleService;
    }

    public void destroy() {
        shutdownService(commonService);
        shutdownService(rabbitMQService);
        shutdownService(singleService);
    }

    private void shutdownService(ExecutorService service) {
        if (service == null) {
            return;
        }
        try {
            List<Runnable> awaiting = service.shutdownNow();
            if (CollectionUtils.isNotEmpty(awaiting)) {
                String threadNames = Arrays.toString(awaiting.toArray(new Runnable[awaiting.size()]));
                log.info("shutdown awaiting task:{}", threadNames);
            }
        } catch (Exception e) {
            log.info("shutdown ExecutorService", e);
        }
    }
}
