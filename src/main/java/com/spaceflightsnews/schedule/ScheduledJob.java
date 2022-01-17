package com.spaceflightsnews.schedule;

import com.spaceflightsnews.service.ScheduledPullArticlesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class ScheduledJob {
    @Autowired
    private ScheduledPullArticlesService service;

    @Scheduled(cron = "* * 9 * * *") // seg, min, hor, dia, mes, ano
    public void execute()  {
        log.info("EXECUTANDO ATUALIZAÇÃO DE DADOS..." + LocalTime.now());
        service.execute();
    }
}
