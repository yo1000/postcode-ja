package com.yo1000.postcode.presentation;

import com.yo1000.postcode.application.TaskApplicationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PostUpdateTaskScheduler {
    private final TaskApplicationService taskApplicationService;

    public PostUpdateTaskScheduler(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        update();
    }

    @Scheduled(cron = "#{ @timeProperties.updateCron }")
    public void update() {
        taskApplicationService.updatePosts();
    }
}
