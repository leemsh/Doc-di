package com.dd.server.controller;

import com.dd.server.domain.User;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.dto.UserDto;
import com.dd.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;


    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<User>> getUser(
            @RequestParam(required = true) String email){
        User user = userService.getUser(email);
        SuccessResponse<User> response;
        if(user != null)
            response = new SuccessResponse<>(user, 200);
        else
            response = new SuccessResponse<>(null, 500);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, response.getStatus());
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<User>> editUser(UserDto userDto){

        SuccessResponse<User> response;

        User user = userService.editUser(userDto);
        if(user == null)
            response = new SuccessResponse<>(null, 500);
        else
            response = new SuccessResponse<>(user, 200);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }


    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<String>> deleteUser(
            @RequestParam(required = true) String email){

        SuccessResponse<String> response;

        boolean isDelete = userService.deleteUser(email);
        if(isDelete)
            response = new SuccessResponse<>("delete Complete", 200);
        else
            response = new SuccessResponse<>("delete failed", 500);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, response.getStatus());
    }
}
