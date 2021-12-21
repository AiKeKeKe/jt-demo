package com.aike.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AsynExecuteUtils {

    /**
     * 对集合中的每个对象做异步转换
     */
    public static <R, T> List<R> concurrentMap(Collection<T> list, Function<T, R> mapper, ExecutorService executorService) {
        List<CompletableFuture<R>> futures = list.stream()
                .map(entity -> CompletableFuture.supplyAsync(() -> mapper.apply(entity), executorService))
                .collect(Collectors.toList());
        return futures.stream().map(future -> future.join()).collect(Collectors.toList());
    }

    /**
     * 对集合里的每一个对象做异步执行
     */
    public static <T> void concurrentRun(List<T> list, Consumer<T> consumer, ExecutorService executorService){
        CompletableFuture[] futures = list.stream()
                .map(entity -> CompletableFuture.runAsync(() -> consumer.accept(entity), executorService))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }

    /**
     * 对单个对象做异步执行
     */
    public static <R> void runAsync(R r, Consumer<R> consumer, ExecutorService executorService){
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> consumer.accept(r), executorService);
        future.join();
    }

}
