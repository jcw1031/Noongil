package com.woopaca.noongil.application.user;

import com.woopaca.noongil.domain.user.AccountStatus;
import com.woopaca.noongil.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {

    public void validateActiveUser(User user) {
        if (user.getStatus() == AccountStatus.PENDING) {
            throw new IllegalStateException("아직 연락처 정보가 등록되지 않은 사용자입니다.");
        }
    }
}
