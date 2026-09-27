package com.dcits.validation.task;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dcits.validation.task.scenario.T2S1;
import com.dcits.validation.task.dto.*;
import com.dcits.common.task.Request;
import com.dcits.common.task.RespHeader;
import com.dcits.common.task.Response;

@RestController
@RequestMapping("/tasks")
@Tag(name = "任务场景接口")
public class T2Pbs implements IT2Contract {
    @Autowired
    private T2S1 t2s1;

    @PostMapping("/T2S1")
    @Operation(summary="检查账户限制")
    public Response<T2S1OutputDTO> executeT2S1(@RequestBody @Valid Request<T2S1InputDTO> req) {
        RespHeader respHeader = new RespHeader(req.getHeader());
        return new Response<>(respHeader, t2s1.execute(respHeader, req.getBody()));
    }
}