package com.example.demo.config;

import com.example.demo.dto.MemberLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody MemberLoginDto memDto) {
        log.info("signin:"+memDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
