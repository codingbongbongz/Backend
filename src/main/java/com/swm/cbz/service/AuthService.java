package com.swm.cbz.service;


import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.controller.exception.TokenForbiddenException;
import com.swm.cbz.controller.exception.UserConflictException;
import com.swm.cbz.domain.Users;
import com.swm.cbz.dto.authorization.request.SignupRequestDTO;
import com.swm.cbz.dto.authorization.response.SignupResponseDTO;
import com.swm.cbz.dto.authorization.response.TokenServiceVO;
import com.swm.cbz.repository.UserRepository;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SignupResponseDTO signupService(SignupRequestDTO requestDTO) {
        validateUserData(requestDTO);

        final Users newUser = userRepository.save(Users.of(requestDTO));
        TokenServiceVO tokenServiceVO = registerToken(newUser);

        return SignupResponseDTO.builder()
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
    }

    @Transactional
    public TokenServiceVO reIssueToken(@NotNull TokenServiceVO token){
        boolean isAccessTokenExpired = jwtService.isExpired(token.getAccessToken());
        boolean isRefreshTokenExpired = jwtService.isExpired(token.getRefreshToken());

        if(isAccessTokenExpired) {
            if(isRefreshTokenExpired){
                throw new TokenForbiddenException(ErrorMessage.EXPIRED_ALL_TOKEN_EXCEPTION);
            }
            final String refreshToken = token.getRefreshToken();
            return setNewAccessToken(refreshToken);
        }

        throw new TokenForbiddenException(ErrorMessage.VALID_ALL_TOKEN_EXCEPTION);
    }

    public void validateUserData(SignupRequestDTO requestDTO) {
        userRepository.findByPassword(requestDTO.getPassword())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_PASSWORD_EXCEPTION);
            });

        userRepository.findByNickname(requestDTO.getNickname())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_NICKNAME_EXCEPTION);
            });

    }

    public TokenServiceVO registerToken(Users user) {
        TokenServiceVO serviceToken = jwtService.createServiceToken(user.getUserId());

        return serviceToken;
    }

    public TokenServiceVO setNewAccessToken(String refreshToken) {
        final Long userId = jwtService.getUserId(refreshToken);

        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        final String newAccessToken = jwtService.createAccessToken(user.getUserId());
        final TokenServiceVO token = TokenServiceVO.of(newAccessToken, refreshToken);

        return token;
    }

}
