package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response;

import lombok.Builder;

@Builder
public class FeedbackResponseDTO implements ResponseDTO {

    public String mainMessage;
    public String content;
    public String additionalInfo;
    public boolean isError;
    public boolean isAlert;
}
