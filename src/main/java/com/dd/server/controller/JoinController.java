package com.dd.server.controller;

import com.dd.server.dto.JoinDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }
    @PostMapping("/join")
    public SuccessResponse<JoinDto> joinProcess(@RequestBody JoinDto joinDto){
        boolean result = joinService.joinProcess(joinDto);
        return new SuccessResponse<>(result, joinDto);
    }
}
