package com.ceos20.instagram_clone.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(

        @NotNull(message = "사용자 이름은 필수 입력 사항입니다.")
        String nickname,

        @NotNull(message = "비밀번호는 필수 입력 사항입니다.")
        @Size(min = 6, message = "비밀번호는 6자 이상입니다.")
        String password,

        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        String email,

        @Pattern(regexp = "^\\d{10,11}$", message = "휴대전화 번호는 10자리 또는 11자리 숫자입니다.")
        String phoneNumber
) {

}
