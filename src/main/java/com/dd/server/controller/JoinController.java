package com.dd.server.controller;

import com.dd.server.dto.JoinDto;
import com.dd.server.dto.SuccessResponse;
import com.dd.server.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinservice;

    public JoinController(JoinService joinService){
        this.joinservice = joinService;
    }
    @PostMapping("/join")
    public SuccessResponse<JoinDto> joinProcess(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color1,
            @RequestParam(required = false) String color2,
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String txt1,
            @RequestParam(required = false) String txt2){


        JoinDto joinDto = new JoinDto();

        SuccessResponse response = new SuccessResponse(joinservice.joinProcess(joinDto), joinDto);
        return response;
    }
}
