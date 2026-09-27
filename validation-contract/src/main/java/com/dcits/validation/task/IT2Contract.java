package com.dcits.validation.task;

import com.dcits.validation.task.dto.*;
import com.dcits.common.task.Request;
import com.dcits.common.task.Response;

public interface IT2Contract {
    Response<T2S1OutputDTO> executeT2S1(Request<T2S1InputDTO> input);
}