package com.example.janghj.service;

import com.example.janghj.config.security.UserDetailsImpl;
import com.example.janghj.config.security.kakao.KakaoOAuth2;
import com.example.janghj.config.security.kakao.KakaoUserInfo;
import com.example.janghj.domain.Address;
import com.example.janghj.domain.User.User;
import com.example.janghj.domain.User.UserCash;
import com.example.janghj.domain.User.UserRole;
import com.example.janghj.repository.UserCashRepository;
import com.example.janghj.repository.UserRepository;
import com.example.janghj.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final String ADMIN_TOKEN = "GMe3md5MK542K45M2ag32K252m22k2mGLWklrnYxKZ0aHgTBG30hfh90H";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserCashRepository userCashRepository;

    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackFor = Throwable.class)
    public User registerUser(UserDto userDto) {
        UserRole userRole = UserRole.USER;
        if (userDto.isAdmin()) {
            if (userDto.getAdminToken().equals(ADMIN_TOKEN)) {
                userRole = UserRole.ADMIN;
            }
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(userRole)
                .address(new Address(userDto.getAddressDto()))
                .userCash(new UserCash())
                .build();
        userRepository.save(user);

        return user;
    }

    public Boolean validationUserId(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean confirmPassword(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(
                () -> new NullPointerException("?????? ???????????? ????????????. userName = " + userDto.getUsername())
        );
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Throwable.class)
    public UserCash depositUserCash(User user, int readyCash) {
        UserCash userCash = userCashRepository.findByUserId(user.getId()).orElseThrow(
                () -> new NullPointerException("?????? ???????????? ????????? ?????????(???) ?????? ??? ????????????. userId = " + user.getId()));
        userCash.depositUserCash(readyCash);
        userCashRepository.save(userCash);
        return userCash;
    }

    @Transactional(rollbackFor = Throwable.class)
    public User setUserAddress(UserDetailsImpl nowUser, Address address) {
        User user = userRepository.findById(nowUser.getId()).orElseThrow(
                () -> new NullPointerException("?????? ???????????? ????????????. userId =" + nowUser.getId()));

        if (address != null) {
            user.setAddress(address);
        }

        userRepository.save(user);
        return user;
    }

    public void kakaoLogin(String authorizedCode) {
        // ????????? OAuth2 ??? ?????? ????????? ????????? ?????? ??????
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();
        // ?????? Id = ????????? nickname
        String username = nickname;
        // ???????????? = ????????? Id + ADMIN TOKEN
        String password = kakaoId + ADMIN_TOKEN;

        // DB ??? ????????? Kakao Id ??? ????????? ??????
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // ????????? ????????? ????????????
        if (kakaoUser == null) {
            // ???????????? ?????????
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(kakaoId, username, encodedPassword, email);
            userRepository.save(kakaoUser);
        }

        // ????????? ??????
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}