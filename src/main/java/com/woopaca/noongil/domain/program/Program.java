package com.woopaca.noongil.domain.program;

import com.woopaca.noongil.domain.BaseEntity;
import com.woopaca.noongil.domain.program.converter.IntegerListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name = "program", indexes = {
        @Index(name = "uidx_unique_id", columnList = "uniqueId", unique = true),
        @Index(name = "sidx_location", columnList = "location")
})
public class Program extends BaseEntity {

    @Column(nullable = false, length = 1024)
    private String name;

    private LocalDate receptionStartDate;

    private LocalDate receptionEndDate;

    private LocalDate programStartDate;

    private LocalDate programEndDate;

    @Column(nullable = false, length = 1024)
    private String address;

    @Column(nullable = false, length = 8)
    private String borough;

    @Column(nullable = false, columnDefinition = "POINT")
    private Point location;

    @Column(nullable = false, length = 1024)
    private String institution;

    @Column(nullable = false, length = 13)
    private String contact;

    @Convert(converter = IntegerListConverter.class)
    @Column(length = 32)
    private List<Integer> ageRange;

    @Column(nullable = false, length = 6)
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeType feeType;

    @Column(length = 1024)
    private String feeAmount;

    @Column(nullable = false, length = 1024)
    private String receptionMethod;

    @Column(length = 1024)
    private String receptionUrl;

    @Column(nullable = false, length = 32, unique = true, columnDefinition = "CHAR(32)")
    private String uniqueId;

    public Program() {
    }

    @Builder
    public Program(String name, LocalDate receptionStartDate, LocalDate receptionEndDate, LocalDate programStartDate, LocalDate programEndDate, String address, String borough, Point location, String institution, String contact, List<Integer> ageRange, String gender, FeeType feeType, String feeAmount, String receptionMethod, String receptionUrl, String uniqueId) {
        this.name = name;
        this.receptionStartDate = receptionStartDate;
        this.receptionEndDate = receptionEndDate;
        this.programStartDate = programStartDate;
        this.programEndDate = programEndDate;
        this.address = address;
        this.borough = borough;
        this.location = location;
        this.institution = institution;
        this.contact = contact;
        this.ageRange = ageRange;
        this.gender = gender;
        this.feeType = feeType;
        this.feeAmount = feeAmount;
        this.receptionMethod = receptionMethod;
        this.receptionUrl = receptionUrl;
        this.uniqueId = uniqueId;
    }
}
