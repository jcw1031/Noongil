package com.woopaca.noongil.domain.safety;

import lombok.Getter;

@Getter
public enum SafetyStatus {

    SAFE("안전"), // 사용자의 활동 분석 결과가 정상 범위인 경우
    CAUTION("주의"), // 사용자의 활동 분석 결과가 정상 범위가 아닌 경우
    WARNING("경고"), // 사용자가 "도움이 필요" 응답 또는 6시간 이상 응답이 없는 경우
    DANGER("위험"), // 지인들도 6시간 이상 응답이 없는 경우
    COMPLETE("완료"); // 사용자가 "괜찮아요" 응답 또는 지인들이 응답한 경우

    private final String description;

    SafetyStatus(String description) {
        this.description = description;
    }
}
