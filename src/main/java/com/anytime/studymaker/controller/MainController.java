package com.anytime.studymaker.controller;

import java.util.Date;
import java.util.Map;

import com.anytime.studymaker.config.jwt.Token;
import com.anytime.studymaker.config.jwt.JWTProvider;
import com.anytime.studymaker.domain.user.dto.LoginDto;
import com.anytime.studymaker.domain.user.dto.UserApiRequest;
import com.anytime.studymaker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MainController {

    private final UserService userService;
    private final JWTProvider JWTProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/main")
    public String hello() {
        return "안녕하세요. 현재 서버시간은 " + new Date() + "입니다. \n";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserApiRequest request) {
        //    TODO : 회원가입
        userService.create(request, passwordEncoder);
        ResponseEntity<String> responseEntity = ResponseEntity.ok("회원 가입이 완료되었습니다.");
        return responseEntity;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Token> signIn(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "Bearer " + JWTProvider.createToken(authentication);
        Token token = new Token(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        return new ResponseEntity<Token>(token, headers, HttpStatus.OK);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        boolean result = userService.existEmail(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody Map<String, Object> request) {
        String nickname = (String) request.get("nickname");
        boolean result = userService.existNickname(nickname);
        return ResponseEntity.ok(result);
    }
}
