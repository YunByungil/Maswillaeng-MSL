package Maswillaeng.MSLback.service;

import Maswillaeng.MSLback.domain.entity.User;
import Maswillaeng.MSLback.domain.repository.UserRepository;
import Maswillaeng.MSLback.dto.user.request.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ValidateService {

    private final UserRepository userRepository;

    /* 존재하는 회원인지 체크하는 validate */
    public User validateExistUser(UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않는 회원")
                );
        return user;
    }
}
