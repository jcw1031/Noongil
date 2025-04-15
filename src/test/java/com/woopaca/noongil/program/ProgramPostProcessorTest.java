package com.woopaca.noongil.program;

import com.woopaca.noongil.infrastructure.publicdata.ProgramDto;
import com.woopaca.noongil.infrastructure.publicdata.ProgramResponse;
import com.woopaca.noongil.infrastructure.publicdata.PublicDataClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class ProgramPostProcessorTest {

    @Autowired
    private PublicDataClient publicDataClient;

    @Test
    void parallel() {
        ExecutorService pageExecutor = Executors.newFixedThreadPool(5, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("page-thread-" + thread.getId());
            return thread;
        });

        ExecutorService elementExecutor = Executors.newFixedThreadPool(20, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("element-thread-" + thread.getId());
            return thread;
        });

        List<? extends Future<?>> pageTasks = IntStream.rangeClosed(1, 10)
                .mapToObj(page -> pageExecutor.submit(() -> {
                    log.info("시작. page: {}", page);
                    task(page, elementExecutor);
                }))
                .toList();

        for (Future<?> pageTask : pageTasks) {
            try {
                pageTask.get();
            } catch (Exception e) {
                log.error("Error while processing page task", e);
            }
        }

        pageExecutor.shutdown();
        elementExecutor.shutdown();
    }

    private void task(int page, ExecutorService elementExecutor) {
        ProgramResponse liveAlonePrograms = publicDataClient.getLiveAlonePrograms(page);
        List<ProgramDto> programs = liveAlonePrograms.getPrograms();

        List<? extends Future<?>> elements = programs.stream()
                .map(program -> elementExecutor.submit(() -> task2(program)))
                .toList();

        List<?> results = elements.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        log.error("Error while processing element", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("끝. page: {}, elements: {}", page, results.size());
    }

    private static ProgramDto task2(ProgramDto program) {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("name: {}", program.getName());
        return program;
    }
}
