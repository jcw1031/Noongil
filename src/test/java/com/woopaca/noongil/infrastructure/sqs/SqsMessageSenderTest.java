package com.woopaca.noongil.infrastructure.sqs;

import com.woopaca.noongil.infrastructure.sqs.TrainHealthModelMessage.ActivityMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Disabled
@SpringBootTest
class SqsMessageSenderTest {

    @Autowired
    private SqsMessageSender sqsMessageSender;

    @Test
    void test() {
        List<ActivityMessage> activities = List.of(
                ActivityMessage.builder()
                        .activityDate(LocalDate.of(2025, 5, 1))
                        .stepsCount(1000)
                        .dayOfWeek((DayOfWeek.SUNDAY.ordinal() + 1) % 7)
                        .build(),
                ActivityMessage.builder()
                        .activityDate(LocalDate.of(2025, 5, 2))
                        .stepsCount(2000)
                        .dayOfWeek((DayOfWeek.MONDAY.ordinal() + 1) % 7)
                        .build()
        );

        IntStream.rangeClosed(1, 1)
                .parallel()
                .forEach(value -> {
                    TrainHealthModelMessage message = new TrainHealthModelMessage((long) value, activities);
                    sqsMessageSender.send(message);
                });
    }
}