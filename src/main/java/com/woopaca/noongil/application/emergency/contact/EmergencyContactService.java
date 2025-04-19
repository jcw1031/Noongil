package com.woopaca.noongil.application.emergency.contact;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmergencyContactService {

    private final UserRepository userRepository;

    public EmergencyContactService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerEmergencyContact(String name, String contact) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userRepository.acquireExclusiveLock(authenticatedUser.getId());
        // 검증
            // 등록된 연락처가 2개 미만인지 확인
            // 이미 등록된 연락처가 아닌지 확인
            // 자신의 연락처가 아닌지 확인
        // 만약 상대방이 가입되어 있다면
            // 비상연락망 생성(바로 ACCEPTED 상태)
            // 상대방도 자동으로 비상연락망 생성(바로 ACCEPTED 상태)
        // 만약 상대방이 가입되어 있지 않다면
            // 비상연락망 생성(PENDING 상태)
            // 상대방에게 비상연락망 등록 요청(SMS 발송)
    }
}
