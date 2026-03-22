package com.turkcell.subscriptionservice.domain.config.quartz;

import com.turkcell.subscriptionservice.application.cron.SubscriptionCreationJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    private static final String JOB_NAME = "SubscriptionCreationJob";
    private static final String GROUP_NAME = "SubscriptionGroup";

    @Bean
    public JobDetail subscriptionJobDetail() {
        return JobBuilder.newJob(SubscriptionCreationJob.class)
                .withIdentity(JOB_NAME, GROUP_NAME)
                .withDescription("Monthly subscription cron job")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger subscriptionJobTrigger(JobDetail subscriptionJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(subscriptionJobDetail)
                .withIdentity(JOB_NAME + "Trigger", GROUP_NAME)
                .withDescription("Trigger subscription monthly task 01:00")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?"))
                //use for evey minute test
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
                .build();
    }
}