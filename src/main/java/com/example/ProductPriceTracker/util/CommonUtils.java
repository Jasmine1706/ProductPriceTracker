package com.example.ProductPriceTracker.util;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class CommonUtils {

    public static String cronExpression(String userFrequency){
        switch (userFrequency){
            case "every 24 hours", "midnight":
                userFrequency = "0 0 0 * * ?";
                break;
            case "morning":
                userFrequency = "0 0 8 * * ?";
                break;
            case "afternoon":
                userFrequency = "0 0 14 * * ?";
                break;
            case "evening":
                userFrequency = "0 0 19 * * ?";
                break;
            case "now":
                userFrequency = "0 15 16 * * ?";
                break;
            default:
                userFrequency = "0 0 * * * *";
        }
        log.info("user frequency logged in database with value "+userFrequency);
        return userFrequency;
    }

    public static Boolean cronDateTimeConverter(String expression){

        try {
            log.info("cronDateTimeConverter() checking alert time and current time.");
            CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING));
            Cron cron = parser.parse(expression);
            cron.validate();

            ExecutionTime executionTime = ExecutionTime.forCron(cron);
            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

            return executionTime.nextExecution(now)
                    .map(next -> Math.abs(java.time.Duration.between(now, next).getSeconds()) <= 120L)
                    .orElse(false);
        }
        catch (Exception exception){
            exception.getMessage();
            log.error("Error in Parsing the Cron expression or Time Zone Error.");
        }
        return null;
    }
}

