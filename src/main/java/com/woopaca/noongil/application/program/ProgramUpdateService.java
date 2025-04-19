package com.woopaca.noongil.application.program;

import com.woopaca.noongil.adapter.program.ProgramCache;
import com.woopaca.noongil.domain.address.AddressCoordinateConverter;
import com.woopaca.noongil.domain.address.Coordinate;
import com.woopaca.noongil.domain.program.FeeType;
import com.woopaca.noongil.domain.program.Program;
import com.woopaca.noongil.domain.program.ProgramRepository;
import com.woopaca.noongil.infrastructure.publicdata.ProgramDto;
import com.woopaca.noongil.infrastructure.publicdata.ProgramResponse;
import com.woopaca.noongil.infrastructure.publicdata.PublicDataClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ProgramUpdateService {

    private final ProgramRepository programRepository;
    private final PublicDataClient publicDataClient;
    private final AddressCoordinateConverter addressCoordinateConverter;
    private final ProgramCache programCache;

    @Getter
    private final ExecutorService programFetchExecutor;
    @Getter
    private final ExecutorService addressCoordinateConvertExecutor;
    private final GeometryFactory geometryFactory;

    public ProgramUpdateService(ProgramRepository programRepository, PublicDataClient publicDataClient, AddressCoordinateConverter addressCoordinateConverter, ProgramCache programCache) {
        this.programRepository = programRepository;
        this.publicDataClient = publicDataClient;
        this.addressCoordinateConverter = addressCoordinateConverter;
        this.programCache = programCache;
        this.programFetchExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("program-fetcher-" + thread.getId());
            return thread;
        });
        this.addressCoordinateConvertExecutor = Executors.newFixedThreadPool(20, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("update-processor-" + thread.getId());
            return thread;
        });
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public void updateNewPrograms() {
        int totalPage = publicDataClient.getTotalPage();
        IntStream.rangeClosed(1, totalPage)
                .forEach(page -> programFetchExecutor.execute(() -> fetchProgramData(page)));
    }

    private void fetchProgramData(int page) {
        ProgramResponse liveAlonePrograms = publicDataClient.getLiveAlonePrograms(page);
        List<ProgramDto> programs = liveAlonePrograms.getPrograms();
        programs.forEach(program -> addressCoordinateConvertExecutor.execute(() -> updateProgram(program)));
    }

    private void updateProgram(ProgramDto program) {
        String uniqueId = program.getUniqueId();
        boolean isAbsent = programCache.setIfAbsent(uniqueId);
        if (!isAbsent) {
            return;
        }

        try {
            String address = program.getSimpleAddress();
            String cleanAddress = address.replaceAll("\\s*\\(.*?\\)", "")
                    .trim();

            Thread.sleep(500L);
            addressCoordinateConverter.convertToCoordinate(cleanAddress)
                    .ifPresentOrElse(coordinate -> {
                        Program programEntity = convertToEntity(program, coordinate, uniqueId);
                        programRepository.save(programEntity);
                    }, () -> {
                        log.warn("주소 변환 실패.(변환된 값 없음) uniqueId: {})", uniqueId);
                        programCache.delete(uniqueId);
                    });
        } catch (Exception e) {
            log.error("1인가구 프로그램 저장 중 예외 발생. uniqueId: {})", uniqueId, e);
            programCache.delete(uniqueId);
        }
    }

    private Program convertToEntity(ProgramDto programDto, Coordinate coordinate, String uniqueId) {
        return Program.builder()
                .name(programDto.getName())
                .receptionStartDate(programDto.getReceptionStartDate())
                .receptionEndDate(programDto.getReceptionEndDate())
                .programStartDate(programDto.getProgramStartDate())
                .programEndDate(programDto.getProgramEndDate())
                .address(programDto.getFullAddress())
                .borough(programDto.getBorough())
                .location(geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(coordinate.longitude(), coordinate.latitude())))
                .institution(programDto.getInstitution())
                .contact(programDto.getContact())
                .ageRange(programDto.getAgeRange())
                .gender(programDto.getGender())
                .feeType(FeeType.find(programDto.getFeeType()))
                .feeAmount(programDto.getFeeAmount())
                .receptionMethod(programDto.getReceptionMethod())
                .receptionUrl(programDto.getReceptionUrl())
                .uniqueId(uniqueId)
                .build();
    }
}
