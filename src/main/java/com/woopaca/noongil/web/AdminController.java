package com.woopaca.noongil.web;

import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.web.dto.ApiResults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping("/test-users")
    public ApiResults.ApiResponse<Void> deleteTestUser() {
        List<User> testUsers = userRepository.findByName("지정훈")
                .stream()
                .flatMap(user -> userRepository.findByName("Jeonghun Ji").stream())
                .toList();
        userRepository.deleteAll(testUsers);
        return ApiResults.success("테스트 유저가 삭제되었습니다.", null);
    }
}
