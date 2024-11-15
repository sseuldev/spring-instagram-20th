package com.ceos20.instagram_clone.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponseDto (

    String accessToken,
    String refreshToken
){
}
