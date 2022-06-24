package com.bb8qq.tgbotproject.lib;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean("taskExecutorA")
    public Executor asyncExecutorA() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Количество основных потоков 5: количество потоков, инициализированных при создании пула потоков
        executor.setCorePoolSize(5);
        // Максимальное количество потоков 5: максимальное количество потоков в пуле потоков, только после того,
        // как буферная очередь будет заполнена, будет применяться к потокам, которые превышают количество основных потоков
        executor.setMaxPoolSize(5);
        // Буферная очередь 500: очередь, используемая для буферизации задач выполнения
        executor.setQueueCapacity(500);
        // Разрешить потоку время простоя в течение 60 секунд: когда поток превышает основной поток, поток будет
        // уничтожен после наступления времени простоя
        executor.setKeepAliveSeconds(60);
        // Префикс имени пула потоков: после его установки нам удобно найти пул потоков, в котором находится задача обработки
        executor.setThreadNamePrefix("DailyAsync-");
        executor.initialize();
        return executor;
    }

    @Bean("taskExecutorB")
    public Executor asyncExecutorB() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("DailyAsync-");
        executor.initialize();
        return executor;
    }

}
