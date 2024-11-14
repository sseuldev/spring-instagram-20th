package com.ceos20.instagram_clone.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, false, "올바르지 않은 요청입니다."),

    // 멤버 에러
    NOT_FOUND_MEMBER_ID(1001, false,"요청한 ID에 해당하는 멤버가 존재하지 않습니다."),
    FAIL_TO_CREATE_NEW_MEMBER(1002, false,"새로운 멤버를 생성하는데 실패하였습니다."),

    // 채팅 에러
    NOT_FOUND_CHATROOM_ID(2001, false,"요청한 ID에 해당하는 채팅방이 존재하지 않습니다."),
    INVALID_CHATROOM(2002, false,"존재하지 않는 채팅방입니다."),
    VALID_CHATROOM(2003, false,"이미 존재하는 채팅방입니다."),
    INVALID_CHATROOM_AUTHORITY(2004, false,"해당 채팅방에 대한 접근 권한이 없습니다."),


    // 게시글 에러
    NOT_FOUND_POST_ID(3001, false,"요청한 ID에 해당하는 게시글이 존재하지 않습니다."),
    NOT_FOUND_POST_LIKE(3002, false,"요청한 ID에 해당하는 게시글 좋아요가 존재하지 않습니다."),

    // 해시태그 에러
    VALID_HASHTAG(4001, false,"이미 존재하는 해시태그입니다."),
    INVALID_HASHTAG(4002, false,"존재하지 않는 해시태그입니다."),

    // 이미지 에러
    EXCEED_IMAGE_CAPACITY(5001, false,"업로드 가능한 이미지 용량을 초과했습니다."),
    NULL_IMAGE(5002,false, "업로드한 이미지 파일이 NULL입니다."),
    EMPTY_IMAGE(5003, false,"최소 한 장 이상의 이미지를 업로드해야합니다."),
    EXCEED_IMAGE_LIST_SIZE(5004, false,"업로드 가능한 이미지 개수를 초과했습니다."),
    INVALID_IMAGE_URL(5005, false,"요청한 이미지 URL의 형식이 잘못되었습니다."),
    INVALID_IMAGE_PATH(5101, false,"이미지를 저장할 경로가 올바르지 않습니다."),
    FAIL_IMAGE_NAME_HASH(5102, false,"이미지 이름을 해싱하는 데 실패했습니다."),
    INVALID_IMAGE(5103, false,"올바르지 않은 이미지 파일입니다."),

    // 댓글 에러
    NOT_FOUND_COMMENT_ID(7001, false,"요청한 ID에 해당하는 댓글이 존재하지 않습니다."),
    NOT_FOUND_PARENT_COMMENT_ID(7002, false,"요청한 ID에 해당하는 부모 댓글이 존재하지 않습니다."),

    // 인증 에러
    INVALID_USER_NAME(8001, false,"존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(8002, false,"비밀번호가 일치하지 않습니다."),
    DUPLICATED_ADMIN_EMAIL(8101, false,"중복된 사용자 이메일입니다."),
    DUPLICATED_ADMIN_USERNAME(8102, false,"중복된 사용자 이름입니다."),
    NOT_FOUND_ADMIN_ID(8103, false,"요청한 ID에 해당하는 관리자를 찾을 수 없습니다."),
    INVALID_CURRENT_PASSWORD(8104, false,"현재 사용중인 비밀번호가 일치하지 않습니다."),
    INVALID_ADMIN_AUTHORITY(8201, false,"해당 관리자 기능에 대한 접근 권한이 없습니다."),
    NOT_FOUND_REFRESH_TOKEN_IN_DB(9001, false,"DB에 존재하지 않는 RefreshToken 입니다."),
    NOT_SUPPORTED_OAUTH_SERVICE(9002, false,"해당 OAuth 서비스는 제공하지 않습니다."),
    FAIL_TO_CONVERT_URL_PARAMETER(9003, false,"Url Parameter 변환 중 오류가 발생했습니다."),
    INVALID_REFRESH_TOKEN(9101, false,"올바르지 않은 형식의 RefreshToken 입니다."),
    INVALID_ACCESS_TOKEN(9102, false,"올바르지 않은 형식의 AccessToken 입니다."),
    EXPIRED_PERIOD_REFRESH_TOKEN(9103, false,"기한이 만료된 RefreshToken 입니다."),
    EXPIRED_PERIOD_ACCESS_TOKEN(9104, false,"기한이 만료된 AccessToken 입니다."),
    FAIL_TO_VALIDATE_TOKEN(9105, false,"토큰 유효성 검사 중 오류가 발생했습니다."),
    NOT_FOUND_REFRESH_TOKEN(9106, false,"RefreshToken 이 존재하지 않습니다."),
    INVALID_AUTHORITY(9201, false,"해당 요청에 대한 접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(9999, false,"서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;
    private final boolean inSuccess;
    private final String message;
}
