package org.example.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
@SuppressWarnings({"unchecked", "rawTypes"})
public class ScheduledTaskManager
{
    private final SessionPropertyManager sessionPropertyManager;

    public ScheduledTaskManager(SessionPropertyManager sessionPropertyManager)
    {
        this.sessionPropertyManager = sessionPropertyManager;
    }

    @Scheduled(cron = "${schedule.tenSec}")
    public void refreshSessionProperties()
    {
        sessionPropertyManager.refreshSessionProperties();
    }
}
