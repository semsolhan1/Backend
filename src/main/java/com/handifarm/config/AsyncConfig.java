//package com.handifarm.config;
//
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.AsyncConfigurer;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
//@Configuration
//@EnableAsync
//public class AsyncConfig implements AsyncConfigurer {
//
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5); // 원하는 코어 스레드 개수 지정
//        executor.setMaxPoolSize(10); // 최대 스레드 개수 지정
//        executor.setQueueCapacity(25); // 큐의 최대 용량 지정
//        executor.setThreadNamePrefix("AsyncExecutor-");
//        executor.initialize();
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new CustomAsyncExceptionHandler();
//    }
//
//    private static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
//        @Override
//        public void handleUncaughtException(Throwable ex, java.lang.reflect.Method method, Object... params) {
//            // 비동기 작업 중에 예외가 발생했을 때 처리하는 로직을 구현합니다.
//            // 여기에서 원하는 대로 예외를 처리하고 에러 메시지를 반환할 수 있습니다.
//            // 예를 들면, 로그에 예외를 기록하거나, 사용자에게 예외에 대한 적절한 메시지를 반환할 수 있습니다.
//            System.err.println("비동기 작업 중 에러 발생: " + ex.getMessage());
//        }
//    }
//
//}
