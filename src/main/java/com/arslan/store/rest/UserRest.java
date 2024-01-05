package com.arslan.store.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);

    @PutMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @PutMapping(path = "/logout")
    public ResponseEntity<String> logout(@RequestBody(required = true) Map<String, String> requestMap);

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

}
