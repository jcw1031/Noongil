package com.woopaca.noongil.program;

import com.woopaca.noongil.client.ProgramResponse;
import com.woopaca.noongil.client.ProgramResponse.Program;
import com.woopaca.noongil.client.PublicDataClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class ProgramPostProcessorTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private PublicDataClient publicDataClient;

    private ExecutorService executorService;

    @Value("${public-data.authentication-key}")
    private String publicDataAuthenticationKey;

    @BeforeEach
    void setUp() {
        this.executorService = Executors.newFixedThreadPool(20);
    }

    @AfterEach
    void tearDown() {
        this.executorService.shutdown();
    }

    @Test
    void parallel() throws InterruptedException {
        IntStream.rangeClosed(1, 30)
                .parallel()
                .forEach(this::collectAndProcessProgramsData);
    }

    void collectAndProcessProgramsData(int page) {
        log.info("current thread: {}", Thread.currentThread().getName());

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        ProgramResponse liveAlonePrograms = publicDataClient.getLiveAlonePrograms(page);
        List<Program> programs = liveAlonePrograms.getPrograms();
        CountDownLatch countDownLatch = new CountDownLatch(programs.size());

        int chunkSize = 5;
        int totalChunks = (int) Math.ceil((double) programs.size() / chunkSize);

        IntStream.range(0, totalChunks)
                .mapToObj(i -> programs.subList(i * chunkSize, Math.min((i + 1) * chunkSize, programs.size())))
                .forEach(chunk -> executorService.execute(() ->
                        chunk.forEach(program -> {
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            log.info("program: {}, 접수 종료: {}", program.getName(), program.getRegistrationEndDate());
                            countDownLatch.countDown();
                        })));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
