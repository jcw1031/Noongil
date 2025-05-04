package com.woopaca.noongil.application.program;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

@ActiveProfiles("local-develop")
@SpringBootTest
class ProgramUpdateServiceTest {

    @Autowired
    private ProgramUpdateService programUpdateService;

    @Test
    void test() throws InterruptedException {
        programUpdateService.updateNewPrograms();

        programUpdateService.getProgramFetchExecutor()
                .shutdown();
        programUpdateService.getProgramFetchExecutor()
                .awaitTermination(1, TimeUnit.MINUTES);

        programUpdateService.getAddressCoordinateConvertExecutor()
                .shutdown();
        programUpdateService.getAddressCoordinateConvertExecutor()
                .awaitTermination(5, TimeUnit.MINUTES);
    }
}
