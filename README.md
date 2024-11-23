# spring-instagram-20th
CEOS 20th BE study - instagram clone coding

---

# 1-1. 도메인 및 ERD 분석
![img.png](readme_img/erd.png)

- 게시글 조회
- 게시글에 사진과 함께 글 작성하기
- 게시글에 댓글 및 대댓글 기능
- 게시글에 좋아요 기능
- 게시글, 댓글, 좋아요 삭제 기능
- 유저 간 1:1 DM 기능

총 6가지 기능에 맞추어 인스타그램 데이터 모델링을 진행해보았다. 

도메인은 총 5개 (`/member`, `/post`, `/comment`, `/hashtag`, `/chat`) 로 구성하였다. 

### 1. member
<img src="readme_img/member.png" width="400"/>

#### 회원가입
- 가입을 위해서는 휴대폰 또는 이메일 인증을 거쳐야한다.
- 인증 후에는 비밀번호, 이름, 사용자이름(닉네임)을 기입한다.

#### 마이페이지
- 마이페이지에는 프로필이미지, 이름, 사용자이름, 소개, 링크, 작성한 게시글 등이 포함된다.

#### 상태 
- 모든 회원의 상태는 `active`, `inactive` 중 하나이다. 
- 회원 탈퇴 시, `inactive` 상태로 두고 일정 기간동안 비활성인 경우 자동 탈퇴 처리가 이루어진다. 

<br />

### 2. post
<img src="readme_img/post.png" width="400"/>

#### 게시글 작성
- 게시글에는 내용, 위치, 이미지(최대 10장), 음악 등을 넣을 수 있다.

#### 게시글 조회
- 게시글은 프로필 사진, 사용자 이름, 이미지, 댓글 개수, 위치, 음악, 해시태그 등을 포함한다. 

<br />

### 3. comment
<img src="readme_img/comment.png" width="400"/>

#### 댓글
- 댓글에는 내용, 좋아요 개수 등이 포함된다. 
- 댓글에는 부모 댓글을 기준으로 대댓글이 달릴 수 있다.
- 대댓글은 자신이 달린 부모 댓글의 참조 정보를 가지고 있다. 
- 모든 댓글에는 좋아요를 누를 수 있다.

<br />

### 4. hashtag
<img src="readme_img/hashtag.png" width="400"/>

<br />

### 5. chat
<img src="readme_img/chat.png" width="400"/>

#### 채팅방(디엠 리스트)
- 사용자와 또 다른 사용자 간의 디엠을 주고받는 공간이다. 

#### 메세지(디엠)
- 사용자 간 일대일로 진행되며, 실시간으로 이루어진다. 

<br />

## ✔︎ 연관관계 분석

1. 회원 - 게시글, 회원 - 메세지, 회원 - 채팅방, 회원 - 댓글좋아요, 회원 - 게시글좋아요, 회원 - 댓글  (`1:N` 일대다관계)

	: 한 명의 회원은 여러 개의 게시글 / 메세지 / 채팅방 / 댓글좋아요 / 게시글좋아요 / 댓글을 가지지만, 이들은 한 명의 회원에만 속한다.

2. 채팅방 - 메세지 (`1:N` 일대다관계)

   	: 하나의 채팅방은 여러 개의 메세지를 포함할 수 있지만, 하나의 메세지는 하나의 채팅방에만 속한다.  

4. 게시글 - 댓글, 게시글 - 게시글좋아요, 게시글 - 이미지 (`1:N` 일대다관계)

   	: 하나의 게시글은 여러 개의 댓글 / 게시글좋아요 / 이미지를 가지지만, 이들은 하나의 게시글에만 속한다. 

6. 게시글 - 게시글해시태그, 해시태그 - 게시글해시태그 (`1:N` 일대다관계)

   	- `게시글 - 해시태그` 간 연결을 위해 중간 엔티티인 게시글해시태그를 활용하였다. 

   	- 하나의 게시글은 여러 게시글해시태그를 가질 수 있고, 마찬가지로 해시태그도 여러 게시글해시태그를 가질 수 있다. 이때 게시글해시태그는 하나의 게시글과 해시태그에만 종속된다.

8. 댓글 - 댓글좋아요 (`1:N` 일대다관계)

   	: 하나의 댓글은 여러 개의 댓글좋아요를 가지지만, 하나의 댓글좋아요는 하나의 댓글에만 속한다. 

<br />

## 🤔 BaseEntity
공통 필드를 정의하여 다른 엔티티들이 공통적으로 사용할 수 있게 분리한 추상 클래스이다.
다른 엔티티들은 해당 추상 클래스를 상속받아 공통 필드를 편리하게 가져다 사용할 수 있다. 

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime modifiedAt;
}
```
### ✅ @MappedSuperclass
이 클래스를 상속받는 다른 엔티티들이 공통된 필드를 가지도록 함

### ✅ @EntityListeners(AuditingEntityListener.class)
- `JPA Auditing` 기능 활성화 
- `AuditingEntityListener`는 엔티티 생성 및 수정 시점을 자동으로 기록함
- `@CreatedDate`와 `@LastModifiedDate`가 이 리스너를 통해 자동으로 관리

### ✅ @CreatedDate
- `JPA Auditing` 의 어노테이션
- 엔티티가 생성될 때 자동으로 현재 날짜와 시간으로 필드 설정

### ✅ @LastModifiedDate
- `JPA Auditing` 의 어노테이션
- 엔티티가 수정될 때 자동으로 현재 날짜와 시간으로 필드를 업데이트함

### ✅ @EnableJpaAuditing
`Springboot Application` 자체도 `JpaAuditing` 사용이 가능하도록 변경해주어야 함
```java
@SpringBootApplication
@EnableJpaAuditing
public class InstagramCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramCloneApplication.class, args);
	}

}
```

<br />

## 🤔 Builder 패턴
`Builder`를 적용하는 방식이 크게 2가지가 있음을 알게 되었다.

### 1. `@Builder`를 클래스 전체에 선언하는 방식
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
```
- 클래스의 모든 생성자에 대해 빌더 패턴 적용 가능 
- 클래스 전체에서 동일한 방식으로 객체를 생성할 수 있어 코드의 일관성이 유지됨
- 생성자에 따라 빌더를 적용하는 데에 있어 혼동이 발생할 가능성 존재

<br />

### 2. `@Builder`를 클래스 내부 메소드로 사용하는 방식
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    
    (생략)
   
   @Builder
   public Member(String name, String email, String password) {
      this.name = name;
      this.email = email;
      this.password = password;
   }
}
```
- 클래스 내에서 특정 생성자에 대해서만 빌더 패턴 적용 가능
- 빌더 메서드에서 필드를 선택적으로 설정할 수 있어 더 디테일한 코드 작성 가능
- 추가적인 메서드 관리와 코드 중복을 감수해야 함

<br />

## 🤔 @Column 속성
`@Column` 어노테이션은 엔티티 클래스의 필드와 데이터베이스 테이블의 열을 매핑하는 데 사용된다. 

```java
@Column(length = 50, nullable = false)
private String nickname;

@Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
private LocalDateTime createdAt;
```
### name
- 데이터베이스 열 이름 직접 지정
- 기본값은 필드명

### length
- 열의 최대 길이 설정
- `length = 50` 은 `varchar(50)`이라는 의미

### nullable
- 열이 `NULL` 값을 허용하는지에 대한 여부를 지정
- `@Column(nullable = false)`란, 해당 필드는 `NULL`값을 허용하지 않는다는 의미

### columnDefinition
- 데이터베이스 열의 SQL 데이터 유형과 속성을 직접 정의할 때 쓰임
- `text`와 `timestamp` 유형의 경우, 따로 명시해주어야 데이터베이스에 반영됨
- 예) `@Column(columnDefinition = "text")`

<br />

# 1-2. Repository 단위 테스트
```java
@SpringBootTest
@Transactional
class PostRepositoryTest {
```
### ✅ 클래스 어노테이션 정리
- `@SpringBootTest` : 애플리케이션 컨텍스트를 로드하여 통합 테스트 수행
- `@Transactional`
  - 각 테스트 메서드가 실행된 후 데이터베이스의 상태를 롤백
  - 테스트 간 데이터 간섭 방지 가능

<br />

```java
@BeforeEach
    void 기본세팅() {

        // given
        Member member = Member.builder()
                .name("이한슬")
                .email("ceos@naver.com")
                .password("1234")
                .nickname("sseuldev")
                .build();
        newMember = memberRepository.save(member);

        post1 = Post.builder()
                .content("테스트1")
                .member(newMember)
                .build();

                    (생략)

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }
```
### ✅ @BeforeEach (기본세팅)
- 각 테스트 메서드가 실행되기 전에 반드시 실행되어야 하는 코드를 지정할 때 사용
- 모든 테스트 메서드마다 반복적이고 독립적으로 실행
- 테스트 환경을 초기화하거나 공통으로 필요한 설정을 할 때 매우 유용함
- 여러 테스트에서 공통적으로 필요한 설정을 한 곳에 모아 코드의 중복을 줄여줌
- `@BeforeEach`로 선언된 메서드는 반드시 `void` 타입이어야 함!

<br />

```java
    @Test
    public void 게시물_조회_테스트() throws Exception {

        // given & when
        List<Post> posts = postRepository.findAllByMember(newMember);

        // then
        assertEquals(3, posts.size(), "게시물 개수는 총 3개입니다.");

        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트1")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트2")));
        assertTrue(posts.stream().anyMatch(post -> post.getContent().equals("테스트3")));

        posts.forEach(post -> assertEquals(newMember.getId(), post.getMember().getId()));
    }

    @Test
    public void 게시물_삭제_테스트() throws Exception {

        // given & when
        postRepository.deleteById(post1.getId());

        // then
        List<Post> posts = postRepository.findAllByMember(newMember);

        assertEquals(2, posts.size(), "게시물 개수는 총 2개입니다.");

        Optional<Post> deletedPost = postRepository.findById(post1.getId());
        assertFalse(deletedPost.isPresent(), "삭제된 게시물이므로 존재하면 안됩니다!");
    }
```
### ✅ Assertions 정리

- assertEquals (`예상값`, `실제값`, `실패 시 출력되는 메시지`)
   : 예상 값과 실제 값을 비교하여 일치하는지 확인

- assertTrue (`조건`, `실패 시 출력되는 메시지`)
   : 주어진 조건이 참인지 확인

- assertFalse(`조건`, `실패 시 출력되는 메시지`)
  : 주어진 조건이 거짓인지 확인


<br />

<br />

# 2-1. 🌿지난주 코드 리팩토링🌿
## ✏️ 회원 삭제 `Soft Delete` 로 구현하기!
![image](https://github.com/user-attachments/assets/f905e593-3767-4c45-bca5-29f666b3efcd)

### 1. `Hard Delete`란?
- 물리적 삭제
- 데이터를 실제로 삭제하는 방법
- 삭제된 데이터는 시스템에서 완전히 제거되어 복구 불가

### 2. `Soft Delete`란?
- 논리적 삭제
- 데이터를 실제로 삭제하지 않고, 삭제된 것처럼 보이게 함
- 데이터는 시스템에서 더 이상 사용되지 않지만, 필요한 경우 되돌리기 가능
- 데이터 보존을 위해 유용하며, 실수로 삭제된 데이터 복구 가능
- 그러나! 삭제된 데이터 유지하려면 추가적인 저장 공간이 필요하기 때문에 신중하게 사용할 것,,

#### 👉 회원의 경우
- 탈퇴를 했다가 일주일 내로 다시 돌아올 가능성 존재
- 회원에 대한 분석, 통계의 필요성
- 탈퇴가 이루어진 경우에도, 다른 서비스나 비즈니스 로직에서 이 회원과 관련된 데이터에 접근 가능해야 함

<br />

이러한 이유들로 인해 `Member`에 대한 데이터 삭제의 경우는 `Soft Delete` 를 써야 한다!!

### 3. `Soft Delete` 구현하기
- 엔티티 삭제는 실제 `DELETE` 쿼리가 아니라, `UPDATE` 로 `deleted_at`에 현재 시간(`NOW()`)을 기록하는 것
    > `@SqlDelete` 어노테이션으로 구현

- 모든 SQL 쿼리에 `WHERE deleted_at IS NULL`을 붙여야만 삭제되지 않은 데이터에 대한 조회, 변경 작업 수행 가능
    > `@Where` 어노테이션으로 구현

<br />

```java
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at IS NULL")
public class Member extends BaseEntity {
```

#### 1. `@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")`
- 삭제 요청이 들어왔을 때 실행되는 SQL 구문 정의 => `UPDATE` 실행
- 데이터는 삭제되지 않고, **삭제된 상태** 로 표시 됨

#### 2. `@Where(clause = "deleted_at IS NULL")`
- `deleted_at`이 `NULL`인, 즉 삭제되지 않은 레코드만 조회되게 함
- 삭제된 레코드는 조회되지 않음

<br />

## ✏️ `@Builder.Default` 추가하기!
#### * 원래 하던 방식
```java
@Column(name = "comment_count")
private int commentCount = 0;
```

#### * `@Builder.Default` 이용한 방식

```java
@Column(name = "comment_count")
@Builder.Default
private int commentCount = 0;
```

<br />

내가 원래 하던 방식으로 할 경우,

`Builder` 패턴에서 기본값을 직접 설정하지 않는다면 0이 아닌 값으로 설정될 여지가 있다고 한다

프로그램 전체에 `Builder` 패턴을 사용했기 때문에 내가 초기화한 기본값이 확실히 보장되는 `@Builder.Default` 을 이용하는 것이 더 나은 방식!!

추가적으로, 엔티티의 `new ArrayList<>()`에도 마찬가지로 `@Builder.Default`를 적용해주었다.

`@Builder.Default`를 사용하지 않으면 null 상태의 리스트에 접근하게 되어 에러가 발생하기 때문이다!!!

#### ❗ `@Builder.Default` 없이 Builder를 사용해 객체를 생성한다면,,
필드는 초기화되지 않고 null 상태로 남게된다. 이 상태에서 접근을 시도한다면, `NullPointerException` 이 터지게 된다..

따라서, `@OneToMany` 관계에서 `List<Post> posts = new ArrayList<>()` 와 같은 컬렉션 타입 필드를 사용하는 경우에는 기본적으로 해당 필드에 빈 컬렉션을 할당해줘야한다.

# 2-2. Service 코드 구현
## 🤔 예외 처리란?
코드 내 (주로 `Service` 코드) 에서 발생하는 오류를 체계적이고 일관되게 관리하기 위해서 **예외처리구조** 를 도입한다고 한다.

예외처리구조를 위해서는 총 4가지 파일이 요구된다. 

### 1. `BadRequestException` 클래스
```java
@Getter
public class BadRequestException extends RuntimeException {

    private final int code;
    private final String message;

    public BadRequestException(final ExceptionCode exceptionCode){
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
```

- 잘못된 요청이 발생했을 때 사용하는 **사용자 정의 예외 클래스**
- 예외 코드와 메시지를 포함하며 `ExceptionCode`와 연동됨

### 2. `ExceptionCode` 열거형
```java
@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    // 멤버 에러
    NOT_FOUND_MEMBER_ID(1001, "요청한 ID에 해당하는 멤버가 존재하지 않습니다."),
    FAIL_TO_CREATE_NEW_MEMBER(1002, "새로운 멤버를 생성하는데 실패하였습니다."),

    // 채팅 에러
    NOT_FOUND_CHATROOM_ID(2001, "요청한 ID에 해당하는 채팅방이 존재하지 않습니다."),
    INVALID_CHATROOM(2002, "존재하지 않는 채팅방입니다."),
    VALID_CHATROOM(2003, "이미 존재하는 채팅방입니다."),

    // 게시글 에러
    NOT_FOUND_POST_ID(3001, "요청한 ID에 해당하는 게시글이 존재하지 않습니다."),
    NOT_FOUND_POST_LIKE(3002, "요청한 ID에 해당하는 게시글 좋아요가 존재하지 않습니다."),

    INTERNAL_SERVER_ERROR(9999, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;

    private final String message;
}
```

- 다양한 예외 상황에 대한 **코드와 메시지**를 관리
- 코드와 메시지를 중앙에서 관리하기 떄문에 유지보수가 용이
- 사전에 정의된 예외 코드와 메시지를 제공하여 예외 처리를 일관성 있게 유지하게 함

### 3. `ExceptionResponse` 클래스
```java
@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    private final int code;
    private final String message;
}
```
- 클라이언트에게 반환할 **예외 응답 객체**
- 예외 코드와 메시지를 클라이언트에게 전달

### 4. `GlobalExceptionHandler` 클래스
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ){
        log.warn(e.getMessage(), e);

        final String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(INVALID_REQUEST.getCode(), errorMessage));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException e){
        log.warn(e.getMessage(), e);

        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e){
        log.error(e.getMessage(), e);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage()));
    }

}
```
- **전역 예외 처리 클래스**
- 다양한 예외를 처리하고 적절한 응답을 클라이언트에게 반환
- 예외 처리 로직과 응답 포맷팅을 중앙에서 관리하기 때문에 유지보수가 용이
- 예외가 발생하면 `handleException` 메서드에서 정의한 응답 형식대로 클라이언트에게 반환됨

### ✅ Service 코드에서는?
```java
// NOT_FOUND_MEMBER_ID(1001, "요청한 ID에 해당하는 멤버가 존재하지 않습니다."),
// NOT_FOUND_POST_ID(3001, "요청한 ID에 해당하는 게시글이 존재하지 않습니다."),

public Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
}

public Post findPostById(Long postId) {
    return postRepository.findById(postId).orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
}
```
이와 같이, 예외 발생 시 구체적인 예외 메시지와 코드가 제공되기 때문에 문제를 정확히 파악하고 처리할 수 있다!

**[ 클라이언트가 받게 되는 오류정보 예시 ]**
```java
{
  "code": 1001,
  "message": "요청한 ID에 해당하는 멤버가 존재하지 않습니다."
}
```


<br />

## 🤔 DTO에서 `record` 사용해보기

`Service` 코드를 작성하며 항상 드는 생각이..

- `Service` 코드에는 비즈니스 로직만 담고 깔끔하게 하고 싶다!
- `DTO` 를 더 효율적으로 잘 써보고 싶다!!

였다. 기존에 작성했던 나의 `Service` 코드를 보면..

```java
public MemberEditInfoResponseDto getMemberEditInfo(Long memberId) {

    Member member = findMemberById(memberId);

    return MemberEditInfoResponseDto.builder()
            .imageUrl(member.getImageUrl())
            .name(member.getName())
            .email(member.getEmail())
            .phoneNumber(member.getPhoneNumber())
            .insuranceId(member.getInsurance().getInsuranceId())
            .build();
}
```

![image](https://github.com/user-attachments/assets/eb6f2777-c49c-412c-b13b-33821a8a4395)

일단, `return` 값에 빌더가 들어가있기 때문에 비즈니스 로직의 가독성이 떨어지고 복잡해보인다.

`DTO` 의 경우, 사용하는 인스턴스들이 겹침에도 불구하고 모든 `DTO` 파일을 기능에 따라 하나하나씩 다 만들어놓은 것을 볼 수 있다. 

#### 💡 `Service` 코드의 builder를 없애고 `Validation - Business Logic - Response` 에 집중해서 코드의 가독성을 높이자!

#### 💡 쓰이는 인스턴스가 비슷한 `DTO`들은 하나의 파일 안에 넣고, `DTO` 안에 builder를 넣어보자!

이러한 이유들로 더 나은 `DTO` 작성방식에 대해 알아보다가 `record` 를 활용한 `DTO`에 대해 알게 되었다!

### DTO에서 `record` 사용하면 좋은 이유

- `record`는 본래 데이터 전달을 위한 단순한 구조체 역할을 하기 위해 설계된 것
- **불변 객체**를 다루기 위해 만들어졌기 때문에 데이터를 단순히 전달하는 **DTO**에 적합
- 자동으로 **생성자**, **getter**, `equals`, `hashCode`, `toString` 메서드를 제공
- `DTO`는 데이터를 캡슐화하여 전송하는 객체이므로, `record`의 간결함과 불변성이 큰 장점

### `record`의 특징

- `DTO`의 필드만 정의하면 해당 필드를 포함하는 **생성자**, **getter** 등이 자동으로 제공
- 필드 이름 자체가 getter 역할을 하므로 `getName()` 대신 **`name()`** 메서드를 사용
- `record`의 필드는 기본적으로 `final`처럼 동작하기 때문에 객체 생성 후 값 변경 불가
- `record` 를 선언할 때는 필요한 필드를 생성자 파라미터로 선언

> - 단순한 데이터 전송의 경우 `record` 가 적합!
> - 복잡한 로직이나 상속이 필요한 경우 `class` 가 적합!

<br />


### ✅`record`를 활용한 DTO
```java
public record PostReq(

        @NotNull
        String content,
        int commentCount,
        String location,
        String music,
        @NotNull
        List<String> imageUrls
) {
        public Post toEntity(Member member) {

                List<Image> images = this.imageUrls.stream()
                        .map(imageUrl -> Image.builder()
                                .imageUrl(imageUrl)
                                .post(Post.builder().build())
                                .build())
                        .collect(Collectors.toList());

                return Post.builder()
                        .content(this.content)
                        .commentCount(this.commentCount)
                        .location(this.location)
                        .music(this.music)
                        .member(member)
                        .images(images)
                        .build();
        }
}
```
```java
@Builder
public record ChatroomRes (
        Long chatroomId,
        Long senderId,
        Long receiverId
) {
    public static ChatroomRes of(Chatroom chatroom) {
        return ChatroomRes.builder()
                .chatroomId(chatroom.getId())
                .senderId(chatroom.getSender().getId())
                .receiverId(chatroom.getReceiver().getId())
                .build();
    }
}
```
- `of`
  - 주로 정적 팩토리 메서드로 사용되어 객체 생성을 나타냄
  - 다른 객체로부터 새로운 객체를 생성할 때 사용됨


- `toEntity`
  - DTO를 엔티티로 변환할 때 사용함
  - 변환 의도를 명확히 하고 싶을 때 사용됨

> 원래 `record`를 활용한 방식에는 불변성을 살리기 위해 `builer` 보다는 `new` 를 사용한다고 한다. 추후 코드 리팩토링을 하면서 수정해봐야겠다!

<br />

**[ 회원 정보 수정에 대한 서비스 코드 ]**
```java
@Transactional
public MemberRes updateMemberInfo(MemberReq request, Long memberId) {

    // Validation
    Member member = findMemberById(memberId);

    // Business Logic
    member.update(request);
    Member saveMember = memberRepository.save(member);

    // Response
    return MemberRes.MemberEditRes(saveMember);
}
```

그 이전보다 비즈니스 로직이 더 잘 보이고 가독성있게 작성됐다는 것을 확인할 수 있다!

<br />

<br />

# 3-1. 🌿지난주 코드 리팩토링🌿

## ✏️ `Where` 어노테이션 삭제하기

기존의 `@Where` 어노테이션이 _deprecated_ (더 이상 사용되지 않음) 되고, 그 대안책으로 사용되는 것이 `@SQLRestriction` 이다!

<br />

### `@SQLRestriction` 이란?

- 특정 엔티티 필드에 대해 SQL 조건을 설정하는데 사용
- 해당 필드에 대한 조회나 수정이 이루어질 때마다 이 제약 조건이 자동으로 반영
- 특히 필터링이 필요하거나, 논리적으로 삭제된 데이터를 제외하고 싶은 경우 등에 사용됨

---

❌ 그러나 `@SQLRestriction` 에 대한 문제점이 많은 것 같아, 사용하지 않기로 결정하였음

1. 제약 조건이 고정적
    - `@SQLRestriction` 에 지정된 조건은 동적으로 변경할 수 없으므로, 조건이 고정된 상황에서만 사용이 가능
2. 복잡한 필터링에서는 어려움 존재
3. 백오피스를 사용하여 데이터 통계를 내는 경우, 삭제된 데이터 조회 불가
    - **Soft Delete** 방식을 선택한 의미가 사라짐

### 🤔 이걸 안쓰면 어떻게 할거야?
```java
Optional<Post> findByIdAndDeletedAtIsNull(Long postId);
```
조건부 쿼리 메소드를 작성하는 방식을 선택하였다.

`deletedAt`이 `NULL`인 경우만 조회하기 때문에 명시적이고 유연한 조건 설정이 가능해진다!

<br />

## ✏️ List 타입 총 정리

### 1. `Stream` 이란?

컬렉션(배열 포함)의 저장 요소를 하나씩 참조해서 람다식으로 처리할 수 있도록 해주는 반복자

- 데이터 소스를 스트림으로 변환 (`stream()` 메서드)
- **중간 연산**을 사용하여 데이터를 변환 또는 필터링
- **종료 연산**을 사용하여 결과를 모으거나 처리

---

#### ✔️ 중간 연산

- `map(Function)` → 각 요소를 다른 값으로 변환
- `filter(Predicate)` → 조건에 맞는 요소만 선택
- `sorted()` → 요소들을 정렬

#### ✔️ 종료 연산

- `collect(Collector)` → 스트림의 요소를 수집하여 리스트나 세트로 반환
- `forEach(Consumer)` → 각 요소에 대해 특정 작업을 수행
- `reduce(BinaryOperator)` → 요소를 반복적으로 결합하여 단일 결과를 생성

```java
List<Integer> studentIds = students.stream()
        .filter(student -> student.getGrade() >= 90) // 점수가 90 이상인 학생만 필터링
        .sorted(Comparator.comparing(Student::getAge)) // 나이 순서로 정렬
        .map(Student::getId) // 학생의 ID만 추출
        .collect(Collectors.toList()); // ID를 List로 수집
```

<br />

### 2. 메서드 레퍼런스 (map 함수)

```java
// 메서드 레퍼런스
.map(this::findOrCreateHashtag)
.map(HashtagResponseDto::from)

// 기존
.map(hashtag -> this.findOrCreateHashtag(hashtag))
.map(hashtag -> HashtagResponseDto.from(hashtag))
```

<br />

### 3. `Collectors.toList()` vs `Stream.toList()`

### ☑️ `Collectors.toList()`

- **Java 8**에서 도입된 메서드로, 스트림의 요소들을 **리스트로 수집**하는 가장 일반적인 방법
- 스트림에서 수집된 데이터를 `List`로 변환하는 **Collector**

```java
stream.collect(Collectors.toList());
```
<br />

### ☑️ `Stream.toList()`

- **Java 16**에서 새롭게 도입된 메서드로, **간단하게 리스트로 변환**할 때 사용
- `Collectors.toList()`와 동일한 기능을 하며 더 간결한 문법을 제공
- `Stream.toList()`는 불변 리스트를 반환하는 반면, `Collectors.toList()`는 **가변 리스트**를 반환

  ⇒ `Stream.toList()`로 반환된 리스트는 수정할 수 없음

---

### ☑️ 비교
- `Collectors.toList()`는 **가변 리스트**를 반환

  ✔️ 반환된 리스트에서 요소를 추가하거나 제거할 수 있음

- `Stream.toList()`는 **불변 리스트**를 반환할 수 있음


불변 리스트 반환이 가능하고 형태가 더 간결한 `Stream.toList()` 를 사용하기로 결정!
```java
.imageUrls(post.getImages().stream()
.map(Image::getImageUrl)  // 각 Image 객체에서 imageUrl 값을 추출하여 새로운 스트림 생성
.toList())  // 리스트로 변환
```

<br />

## ✏️ dto로 알아보는 `public`과 `public static`의 차이

### 1. `public` (인스턴스 메서드)
```java
public Chatroom toEntity(Member sender, Member receiver) {
    return Chatroom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
}
```

- 클래스의 인스턴스를 먼저 생성 후 호출 가능
- 특정 객체의 상태를 변환하거나 그 객체와 밀접한 관련이 있을 때 주로 사용
- 객체의 상태에 따라 동작하는 메서드를 정의할 때 사용
- 예) `toEntity`는 `ChatroomRequestDto` 객체가 만들어진 후에 그 객체에 접근해 호출됨

```java
MyClass obj = new MyClass();    // 객체가 만들어져야
obj.instanceMethod();           // 호출 가능
```
<br />

### 2. `public static` (정적 메서드)

```java
public static ChatroomResponseDto from(Chatroom chatroom) {
        return ChatroomResponseDto.builder()
                .chatroomId(chatroom.getId())
                .senderId(chatroom.getSender().getId())
                .receiverId(chatroom.getReceiver().getId())
                .createdAt(chatroom.getCreatedAt())
                .build();
    }
```
- 클래스의 인스턴스를 생성하지 않고도, 클래스 자체에서 호출 가능
- 특정 인스턴스에 의존하지 않음
- 객체와 관계없이 클래스 레벨에서 공통적으로 사용할 수 있는 메서드를 정의할 때 사용
- **정적 팩토리 메서드 패턴**에서 사용되는 방식

  👉 왜? 인스턴스가 없더라도 사용할 수 있으며, 인스턴스 자체의 상태와 관련이 없기 때문

```java
MyClass.printMessage();
```

<br />

## ✏️ cascade 속성과 orphan 속성 이해하기

```java
@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Image> images = new ArrayList<>();
```

**`Post` 엔티티를 저장할 때 연관된 `Image` 엔티티들도 자동으로 저장**됨. 

✅ 따라서 `imageRepository.saveAll(images)`를 따로 호출할 필요가 없다!!

- **`cascade = CascadeType.ALL`**
  - `Post` 엔티티가 **저장**되거나 **수정**, **삭제**될 때 연관된 `Image` 엔티티들도 함께 처리된다는 의미
  - 즉, `Post`를 저장하면 그에 속한 `Image` 리스트도 함께 저장
  

- **`orphanRemoval = true`**
  - `Post`에 속한 `Image`가 **Post 엔티티에서 제거되면** 자동으로 해당 `Image`도 삭제된다는 의미
  - 즉, 고아 상태가 된 `Image` 엔티티를 자동으로 제거해줌


💡 **`Post`와 연관된 `Image` 엔티티들은 **자동으로 영속성 컨텍스트에 저장**되거나 **삭제**되기 때문에 따로 신경써주지 않아도 됨!**

<br />

# 3-2. Controller 코드 구현

## 🤔 성공 응답 코드 통일하기
```java
@Data
@AllArgsConstructor
public class CommonResponse<T> {

    private int code;
    private boolean inSuccess;
    private String message;
    private T result;

    public CommonResponse(ResponseCode status, T result) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();
        this.result = result;
    }

    public CommonResponse(ResponseCode status) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = status.getMessage();
    }
}
```

```java
@Getter
public enum ResponseCode {

    SUCCESS(2000, true, "요청에 성공하였습니다.");

    private int code;
    private boolean inSuccess;
    private String message;

    ResponseCode(int code, boolean inSuccess, String message) {
        this.inSuccess = inSuccess;
        this.code = code;
        this.message = message;
    }
}
```
### Controller 코드
```java
@Operation(summary = "게시글 조회", description = "하나의 게시글을 조회하는 API")
@GetMapping("/{postId}")
public CommonResponse<PostResponseDto> getPost(@PathVariable Long postId) {

    return new CommonResponse<>(ResponseCode.SUCCESS, postService.getPost(postId));
}
```

### 프론트엔드에게 전해지는 실제 응답
![스크린샷 2024-10-28 025755](https://github.com/user-attachments/assets/ee18575a-c8e7-4ae3-91db-ca322282fd9e)

<br />

## 🤔 트러블 슈팅 < 일대일 채팅 조회 >

### 문제점
사용자가 참여한 일대일 채팅방을 조회하는 API를 작성하던 과정에서 발생한 문제이다. 

```java
@Operation(summary = "1:1 채팅방 조회", description = "1:1 채팅방을 조회하는 API")
@GetMapping("/{senderId}/{receiverId}")
public CommonResponse<ChatroomResponseDto> getChatroom(@PathVariable Long senderId, @PathVariable Long receiverId) {

    return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getChatroom(senderId, receiverId));
}
```

sender 와 receiver 를 구분해서 인자를 받고 서비스 코드에서 조회하는 로직으로 구성하였다.
이때, 실제로는 sender, receiver 구분 없이 내가 대화를 참여하고 있다면 채팅방 조회가 되어야한다! 

따라서 서비스 로직에서는, `findBySenderAndReceiverOrReceiverAndSender(sender, receiver, receiver, sender)` 를 이용하여 sender와 receiver의 위치에 관계없이 채팅방을 조회할 수 있도록 구성하였다. 

하지만 swagger 테스트를 해보니 내가 sender 인 경우만 채팅방 조회가 되고, receiver 의 경우에는 채팅방 조회가 되지 않는 문제가 발생하였다. 

<br />

### 문제의 원인
문제는 내가 작성한 **JPA 메서드 쿼리**에 있었다. 

보다시피 해당 메서드 쿼리는 `AND` 과 `OR` 이 복잡하게 혼합되어있음을 알 수 있다. JPA에서는 `OR` 조건을 포함한 쿼리를 작성할 때, `AND` 와 `OR` 의 조합을 정확히 해석하지 못할 수 있다고 한다. 이로 인해 내 의도와는 다르게 코드가 동작한 것이다. 

<br />

### 해결책
~~JPQL도 짜버릇 하자~~
#### 💡`@Query` 어노테이션을 사용해서 JPQL로 작성하자
```java
@Query("SELECT c FROM Chatroom c WHERE " +
       "((c.sender = :sender AND c.receiver = :receiver) OR " +
       "(c.sender = :receiver AND c.receiver = :sender)) AND c.deletedAt IS NULL")
Optional<Chatroom> findChatroomByMembers(@Param("sender") Member sender, @Param("receiver") Member receiver)
```
- `@Query`
  - JPA Repository 메서드에서 직접 쿼리를 작성할 수 있게 해주는 어노테이션
- `sender` 와 `receiver` 의 위치가 바뀌어도 동일한 채팅방이 조회되도록 해라.
- `@Param("sender")`와 `@Param("receiver")`
  - `@Param` 어노테이션을 사용해 sender와 receiver 파라미터를 쿼리에서 사용할 수 있게 함

<br />

# 3-3. Controller 통합 테스트 구현

## 1. 테스트 클래스 설정
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
```

- `@SpringBootTest` 와 `@AutoConfigureMockMvc`
  - SpringBoot 테스트 환경 구성
  - `MockMvc` 를 자동 설정하여 테스트 중 애플리케이션 컨텍스트를 로드하고, 컨트롤러의 실제 엔드포인트를 호출할 수 있도록 함


- `@Transactional`
  - 테스트가 끝나면 DB 변경 사항을 자동으로 롤백하여 테스트 환경을 깨끗하게 유지하는 역할


- `ObjectMapper`
  - Java 객체와 JSON 간의 변환을 담당
  - 요청 본문으로 객체를 JSON으로 변환하여 전달하거나, JSON 응답을 객체로 변환할 때 사용

<br />

## 2. 테스트 코드
```java
@Test
public void 게시물_생성_성공() throws Exception {

    // given
    List<String> images = List.of("aaa", "bbb");
    PostRequestDto request = new PostRequestDto("새로운 게시글입니다",0, "서울시", "music", images);

    // when & then
    mockMvc.perform(post("/api/post/{memberId}", memberId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))

            .andDo(print()) // 요청과 응답 정보를 콘솔에 출력
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.content").value("새로운 게시글입니다"))
            .andExpect(jsonPath("$.result.location").value("서울시"))
            .andExpect(jsonPath("$.result.music").value("music"))
            .andExpect(jsonPath("$.result.imageUrls[0]").value("aaa"))
            .andExpect(jsonPath("$.result.imageUrls[1]").value("bbb"));
}
```

- `mockMvc.perform`
  - `MockMvc` 객체를 통해 HTTP 요청을 실행하는 메서드


- `contentType(MediaType.APPLICATION_JSON)`
  - 요청의 Content-Type이 JSON 형식임을 명시


- `content(objectMapper.writeValueAsString(request))`
  - `PostRequestDto` 객체를 JSON 문자열로 변환하여 요청 본문에 포함시킴


- `andDo(print())`
  - 요청과 응답 정보를 콘솔에 출력


- `andExpect` 메서드
  - 서버의 응답이 예상한 결과와 일치하는지 검증
  - `status().isOk()` 는 응답 HTTP 상태 코드가 **200 OK** 인지 확인
  - `jsonPath("$.result.content").value("새로운 게시글입니다")`
    - 응답 JSON의 `result.content` 필드 값이 일치하는지 확인

<br />

<br />

# 4. Spring Security

## 4-1. JWT 인증(Authentication)
![jwt](https://github.com/user-attachments/assets/1d644977-126a-44d5-9283-f505964d256a)

> JWT를 이용한 인증 방식에서는 **accessToken** 과 **refreshToken** 을 활용한다.

### ⭐ JWT 인증 흐름 요약

1. **로그인 시, accessToken과 refreshToken 발급**
   - 이때, refreshToken은 accessToken보다 토큰 만료 기간이 더 길다!
2. **accessToken으로 인증**
   - 클라이언트는 API 요청 시 accessToken을 사용하여 서버에 사용자를 인증
3. **accessToken 만료 시 refreshToken 사용**
   - accessToken이 만료되면, 클라이언트는 refreshToken을 사용해 새로운 accessToken을 요청
   - 서버는 DB에 저장된 refreshToken과 비교하여 유효한 경우, 새로운 accessToken 발급

     ✅ 검증을 위해서 서버에 refreshToken을 별도로 저장시키는 로직 필요!
4. **refreshToken 만료 시 재로그인 필요**
   - refreshToken 또한 만료되거나 유효하지 않으면, 사용자는 다시 로그인해야 함

<br />

### ⭐ JWT 생성 원리 및 암호화 방식

> Header + Payload + Signature 구조

내부 정보를 단순 `BASE64` 방식으로 인코딩하기 때문에 외부에서 쉽게 디코딩이 가능하다. 

따라서, 외부에서 열람해도 되는 정보만을 담아야 한다!

❌ 토큰 내부에 비밀번호와 같은 값 입력 금지

- 토큰 자체의 발급처를 확인하기 위해서 사용
- (내가 선택한) 암호화 방식 : 양방향 대칭키 방식인 `HS256` 사용하기로 결정

  ✅ 암호화 키를 따로 `application.yml` 파일에 저장해두어야 함 (유출 방지 위함)

<br />

## 4-2. 세션, 토큰, 쿠키, OAuth 방식 비교
**'누가' 로그인 중인지**에 대한 상태를 기억하기 위해 **세션, 토큰, 쿠키**를 사용한다.

### 1. 세션
- 서버 중심의 인증 방식 ⇒ 서버에 사용자 상태를 저장
- 비밀번호와 같은 인증 정보를 쿠키에 저장하지 않고, 대신에 사용자의 식별자인 `session Id` 를 저장

  ⇒ `session id`를 통해 클라이언트와 소통하는 형식

- 보안성이 높은 반면,  저장소가 과부하될 가능성 존재

### **2. 토큰**

- 클라이언트 측에 토큰 정보 저장
- 요청 헤더에 직접 포함하여 전송
- 서버는 무상태 (Stateless) 로 동작

  ❓ `Stateless` : 서버가 각 요청에 대한 상태를 저장하지 않는 방식

- 확장성과 성능이 뛰어나지만,  보안 측면에서 관리가 필요
- 일정한 토큰 유효기간 동안의 토큰 보안 관리 필요

### **3. 쿠키**

- 클라이언트와 서버 간의 상태 정보를 유지하기 위해 사용
- 쿠키는 공개 가능한 정보를 사용자의 브라우저에 저장시킴
- 사용자를 식별할 수 있는 토큰 (`refreshToken`) 이나 `session ID` 같은 식별 정보를 저장
- 서버에 부담이 없고 클라이언트 측에서 상태를 유지할 수 있지만, 보안에 취약하고 클라이언트측으로부터 조작될 가능성이 존재한다는 단점 존재

#### ✔️ 과정
1. 서버는 클라이언트의 로그인 요청에 대한 응답을 작성할 때, 클라이언트 측에 저장하고 싶은 정보를 응답 헤더의 `set-cookie` 에 담아 응답
2. 클라이언트가 쿠키를 저장하고 이후 모든 요청마다 쿠키를 서버로 다시 전송하는 방식으로 동작
3. 서버는 쿠키에 담긴 정보를 바탕으로 해당 요청의 클라이언트가 누군지 식별

### 4. OAuth

- 인증의 과정을 **'타 서비스에게 위임'** 하는 인증 방식 (예: 소셜로그인)

#### ✔️ 과정
1. **사용자 요청**
   - 클라이언트는 사용자가 리소스 서버 (예: Google, Facebook) 에 접근하고자 하는 경우, 사용자를 OAuth 인증 서버로 리디렉션하여 접근 권한을 요청

2. **사용자 승인**
   - 사용자는 OAuth 서버에서 로그인하고 애플리케이션이 자신의 정보에 접근하는 것을 승인

3. **Authorization Code 발급**
   - 인증 서버는 사용자를 승인한 후, 클라이언트에게 `Authorization Code` 를 발급하여 전달
   - 이 코드는 일회용이며 짧은 시간 동안만 유효

4. **accessToken 요청**
   - 클라이언트는 `Authorization Code` 와 함께 인증 서버에 `accessToken` 을 요청

5. **accessToken 발급**
   - 인증 서버는 요청을 검증한 후, 클라이언트에게 `accessToken` 을 발급

6. **리소스 서버 요청**
   - 클라이언트는 `accessToken` 을 포함해 리소스 서버에 요청을 보냄
   - 서버는 토큰을 검증하여 요청된 리소스 제공

#### ✔️ 예시
구글은 웹 사이트 사용자가 '구글 로그인' 기능을 통해 구글에게 전송한 구글 계정 정보가 유효한지 (구글 아이디 및 비밀번호가 일치하는지) 를 확인한다.

유효하다면 해당하는 구글 유저 정보 중 일부 (유저 이름, 프로필 이미지 등) 를 내 웹 사이트에 제공해주는 **'인증' 과정만을 처리**해주는 방식이다!

<br />

### ❔ 인증과 인가의 차이

#### 인증 (**Authentication**)

사용자가 누구인지 확인하고 증명해주는 로그인/로그아웃 같은 것

#### 인가 (**Authorization**)

인증된 사용자가 페이지에 접근할 수 있는 권한

✅ 인증이 먼저 이루어지고 그 다음 인가가 뒤따르게 됨

<br />

## 4-3. Spring Security 구현하기
<img src="https://github.com/user-attachments/assets/bd1d373f-6e54-4ca7-ac19-274732fe49a2" width="300px" />

사용자 이름 (닉네임) 과 비밀번호만을 이용해 로그인하는 방식으로 로직을 구현해보았다. 

## 1. SecurityConfig 설정

- 스프링 시큐리티의 인가 및 설정을 담당하는 클래스
- 인증을 관리하는 `AuthenticationManager`를 설정
- `LoginFilter`, `JWTFilter`, `CustomLogoutFilter`를 시큐리티 필터 체인에 추가해서 각 필터가 적절한 시점에 동작하도록 구성

✔️ `http.authorizeHttpRequests().requestMatchers(...)`

- 경로별 인가 작업 담당
- 특정 url에 대한 접근 권한 설정
- `permitAll` : 모든 권한 허용
- `.anyRequest().authenticated());` : 로그인한 사용자만 접근 가능 (인증 필요)


✔️ **Stateless 상태 지정**
```java
http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
```

JWT를 통한 인증/인가를 위해서 세션을 `STATELESS` 상태로 설정하는 것이 중요!!

✔️ **csrf 비활성화해도 되는 이유?**

> `CSRF` 는 주로 **세션 쿠키를 사용하는 환경에서 발생하는 공격**

- JWT 인증 방식에서는 서버가 클라이언트의 세션 상태를 유지하지 않고, 매 요청마다 클라이언트가 토큰을 포함해 인증을 수행
- JWT는 각 요청에 인증 정보를 포함하기 때문에 **세션을 사용하지 않는 `Stateless` 방식** ⇒ `CSRF` 보호가 필요하지 않음

### CORS

- 웹 브라우저는 보안상의 이유로 다른 도메인 간의 리소스 요청을 제한함
- CORS 설정을 통해 특정 도메인에서의 요청 허용 가능

✔️ **CORS 에러**

서로 다른 도메인의 애플리케이션 간 API 호출 제한되어 발생하는 에러

예) 백엔드의 8080 포트와 프론트엔드의 3000 포트
    
⇒ **포트가 다르기 때문에 서로 다른 출처**로 인식되어 CORS 에러가 발생

```java
configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
```

✅ 프론트엔드에서 데이터 보낼 3000번대 포트 허용

<br />

## 2. 로그인 로직 구현

<img src="https://github.com/user-attachments/assets/e9716b08-6b13-4005-81c7-5f6c261f5949" />

### 1. LoginFilter

- 커스텀 `UsernamePasswordAuthentication` 필터 작성 (상속받아 사용)
- 로그인 요청 처리하는 필터 ⇒ **아이디, 비밀번호 검증**을 위한 커스텀 필터
- `AuthenticationManager` 를 이용하여 DB에 저장되어 있는 회원 정보를 기반으로 검증할 로직 작성
- 로그인 성공 시 **JWT를 반환**할 success 핸들러 생성
- 커스텀한 필터 `SecurityConfig`에 등록해야 함
- refreshToken은 DB에 저장해서 토큰 재발급이 가능하도록 함

### 2. CustomUserDetailsService
- `UserDetailsService` 커스텀해서 구현
- DB에서 **사용자 정보를 조회**하는 기능

### 3. CustomUserDetails

- `UserDetails` 커스텀해서 구현
- 인증된 사용자 정보를 관리
- **`UserDetailsService` 에 데이터를 넘겨주는 DTO 역할**
- 여기서 실행되는 `getMemberId` 메서드를 이용하여 추후 컨트롤러에 사용될 사용자 토큰 정보 가져옴

### ⭐ 회원 검증

- `UsernamePasswordAuthenticationFilter`가 호출한 `AuthenticationManager` 를 통해 진행

- `AuthenticationManager` 는 DB에서 조회한 데이터를 `UserDetailsService`를 통해 받아서 회원 검증

### 4. JWTUtil

- JWT에 관한 발급과 검증을 담당하는 클래스
- JWT를 생성하고 검증하는 핵심 로직!

### 5. JWTFilter

- 들어오는 HTTP 요청의 헤더에서 JWT 추출 및 사용자 인증에 대한 처리 진행
- 요청 헤더 **Authorization 키에 JWT가 존재**하는 경우, JWT를 검증하고 강제로 `SecurityContextHolder` 에 세션을 생성

    (이 세션은 `STATELESS` 상태로 관리되기 때문에 해당 요청이 끝나면 소멸)

### ⭐ 액세스 & 리프레쉬 토큰 발급

로그인 성공 시 `Access/Refresh`에 해당하는 다중 토큰 발급 ⇒ 총 2개의 토큰을 발급

- **accessToken**: 헤더에 발급 후 프론트에서 로컬 스토리지 저장
- **refreshToken**: 쿠키에 발급
<br />

### ⏩ Postman 테스트 결과
<img src="https://github.com/user-attachments/assets/427aae58-7173-4fd5-b004-0e9d6043a40f" width="400px" />
<img src="https://github.com/user-attachments/assets/6e65efa8-6e2c-4a80-acb1-1bda9f457fbe" width="400px" />

> `/login` : Spring Security에서 기본으로 사용하는 로그인 엔드포인트

`/login` POST 요청을 통해 닉네임과 비밀번호를 입력하면, 응답 헤더에 accessToken 값이 올바르게 뜨는 것을 확인할 수 있다!

마찬가지로, refreshToken 또한 쿠키에 올바르게 발급되는 것을 확인할 수 있다!

<br />

## 3. 토큰 재발급 로직 구현

1. 서버 측 `JWTFilter`에서 accessToken의 만료로 인한 특정한 상태 코드가 프론트엔드에게 응답
2. 프론트 측의 예외 핸들러에서 accessToken 재발급을 위한 refreshToken을 서버 측으로 전송
3. 서버에서는 refreshToken을 받아 새로운 accessToken을 응답
4. 이때 accessToken 갱신 시 refreshToken도 함께 갱신 (⭐ **Refresh Rotate**)

### ⭐ Refresh Rotate란?

refreshToken을 받아 accessToken 갱신 시 refreshToken도 함께 갱신하는 방법

Rotate 되기 이전의 토큰을 가지고 서버측으로 가도 인증이 되는 문제 발생!

✅ 서버측에서 발급했던 refreshToken들을 기억한 뒤 **블랙리스트 처리**를 진행하는 로직을 작성해야 한다! 

(Rotate 이전의 refreshToken은 사용하지 못하도록,,)


- refreshToken 교체로 보안성 강화
- 로그인 지속시간 길어짐

생명주기가 긴 refreshToken은 발급 시 서버측 저장소에 저장한 후 서버에 **기억되어 있는 refreshToken만** 사용할 수 있도록 하는 것이 좋다 (**서버측 주도권**)

1. **발급시** : refreshToken을 서버측 저장소에 저장

2. **갱신시 (Refresh Rotate)** : 기존 refreshToken을 삭제하고 새로 발급한 refreshToken을 새로 저장

### ✔️ 토큰 저장소

RDB 또는 Redis와 같은 데이터베이스를 통해 refreshToken을 저장한다. 

이때 Redis의 경우, `TTL` 설정을 통해 생명주기가 끝난 토큰을 자동으로 삭제할 수 있다는 장점이 있다.

<br />

## 4. 로그아웃 로직 구현
`CustomLogoutFilter` 를 통해 로그아웃 로직을 구현한다.

### 프론트엔드측

로컬 스토리지에 존재하는 accessToken 삭제 및 서버측 로그아웃 경로로 refreshToken 전송

### 백엔드측 
- refreshToken을 받아 Cookie 초기화 (`NULL`) 후, Refresh DB에서 해당 refreshToken 삭제
- 세션을 무효화하고, 인증 정보를 제거

(nickname 기반으로 모든 refreshToken 삭제하는 로직 구현)

### ⏩ Postman 테스트 결과
<img src= "https://github.com/user-attachments/assets/cf1ff858-b887-494d-856e-640e15a2e6f9" width="80%" />

> `/logout` : Spring Security에서 기본으로 사용하는 로그아웃 엔드포인트

`/logout` POST 요청을 보낼 시, 쿠키에 있던 refreshToken 값이 사라지는 것을 확인할 수 있다!

<br />

## 5. 토큰이 필요한 API 구현

### 기존 코드
```java
@Operation(summary = "게시글 전체 조회", description = "내가 작성한 전체 게시글을 조회하는 API")
@GetMapping("/my/{memberId}")
public CommonResponse<List<PostResponseDto>> getAllPosts(@PathVariable Long memberId) {

    return new CommonResponse<>(ResponseCode.SUCCESS, postService.getAllPosts(memberId));
}
```

### ♻️ 토큰값 적용한 코드
```java
@Operation(summary = "게시글 전체 조회", description = "내가 작성한 전체 게시글을 조회하는 API")
@GetMapping("/my")
public CommonResponse<List<PostResponseDto>> getAllPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {

    return new CommonResponse<>(ResponseCode.SUCCESS, postService.getAllPosts(userDetails.getMemberId()));
}
```

### `@AuthenticationPrincipal`
- Spring Security에서 현재 인증된 사용자 정보를 컨트롤러 메서드에 직접 주입할 때 사용하는 어노테이션
- `CustomUserDetails` 를 주입함으로써, `memberId` 를 가져와 줌

<br />

<img src="https://github.com/user-attachments/assets/5c798a4e-244f-4d26-93df-c8242975639e" width="80%"/>

✔️ 로그인 시 응답 헤더에 기록되는 accessToken을 swagger의 Authorize 값에 넣어서 사용자 인증을 해야 한다!

<br />

<img src="https://github.com/user-attachments/assets/19b61b67-8c35-4416-958d-00be6f839f43" width="80%" />

✔️ 인증이 올바르게 되었다면, `memberId` 값을 입력하지 않아도 인증된 사용자 정보를 바탕으로 **사용자 토큰이 필요한 API 요청**이 성공적으로 실행되는 것을 볼 수 있다.

<br />

## 📢 트러블 슈팅 < Swagger 403 오류 문제 >
### 1. 문제점
<img src="https://github.com/user-attachments/assets/40926b05-f578-4103-9b51-50b2f795b0c6" />

토큰이 필요한 모든 API 요청 테스트 시, `403 Forbidden` 오류가 발생했다.

> `403 Forbidden` : 접근 권한이 없는 경우

**Swagger에서 `403 Forbidden` 오류가 발생하는 주요 원인에는 무엇이 있을까?**

주로 **인증 또는 인가**에 문제가 있는 경우라고 한다.

<br />

#### 1. JWT 토큰이 누락되거나 잘못된 경우

- 요청을 보낼 때, `Authorization` 헤더에 JWT 토큰을 포함하지 않았거나 잘못된 형식으로 포함한 경우

🤔 Swagger의 Authorize 버튼을 통해, `/login` 시 응답 헤더로부터 전해진 accessToken 값을 올바르게 넘겼다고 생각한다!

<br />

#### 2. 토큰이 만료된 경우

- JWT 토큰이 만료되면 인증이 실패함

🤔 방금 로그인해서 받은 accessToken 값이기 때문에 만료되었을 리가 없다!

<br />

#### 3. Spring Security 설정에서 경로 접근 권한이 필요한 경우

- 특정 경로에 대해 권한이 필요하지만, 요청을 보낸 사용자의 권한이 부족한 경우

```java
http
        .authorizeHttpRequests((auth) -> auth
        .requestMatchers("/login", "/", "/api/auth/signup", "/api/auth/reissue", "/swagger-ui.html", "/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/admin").hasRole("ADMIN")    // ADMIN 권한 설정
                        .anyRequest().authenticated()   // 따로 권한 설정 없이 인증만 이루어지면 접근 가능
                );
```

🤔 admin 경로를 제외한 나머지 API 경로의 경우 따로 권한 설정을 하지 않았다!

<br />

#### 4. CORS 설정 문제

- 클라이언트 (Swagger UI) 와 서버 도메인이 다를 때, `CORS` 설정이 올바르지 않으면 서버가 요청을 차단

🤔 로컬 환경에서 `localhost:8080`을 통해 Swagger로 테스트한 경우이므로, 도메인이 달라 발생하는 오류인 `CORS` 에러가 원인일 가능성은 적다!

<br />

**해당 경우들이 모두 성립하지 않는데, 도대체 오류의 원인은 무엇일까??**

<br />

### 2. 문제의 원인
```java
/** [ JWTFilter 코드 ] **/

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            
    // 헤더에서 access키에 담긴 토큰을 꺼냄
    String accessToken = request.getHeader("access");

    
/** [ LoginFilter 코드 ] **/

@Override 
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        ( 생략 )   
        
    // 로그인 성공 시 발급되는 토큰에 대한 응답 설정
    response.setHeader("access", access);
```

처음에는 로그인을 성공할 경우 발급되는 accessToken에 대한 헤더 이름을 (확인하기 쉬우라고) `access` 라는 이름으로 설정을 진행하였다.

내가 이렇게 작성했기 때문에 당연하게도 헤더에서 accessToken을 가져오는 경우, access 키에 담긴 토큰을 꺼내는 로직으로 `getHeader` 를 구현한 것이었다.

**❗ 이게 바로 문제의 원인이었다 ❗**

다시 swagger의 응답을 살펴보자.

![스크린샷 2024-11-08 152843](https://github.com/user-attachments/assets/0dd73c1b-5cc7-401c-82cb-eb0a1303933f)

`Authorization : Bearer <token>` 형태로 accessToken 값이 들어오고 있음을 확인할 수 있다!

이런 상황에서 나의 코드는 헤더의 access 키의 토큰을 꺼내줘! 라고 요청하고 있으니 swagger에서는 토큰에 대한 인식 자체를 하지 못한 것이었다. 

### ⭐ `Authorization : Bearer <token>`

- HTTP 표준과 Spring Security에서 토큰 기반 인증 정보를 전달하는 표준 방식
- 대부분의 클라이언트 라이브러리 (예: Axios, Postman, Swagger 등) 와 브라우저의 인증 토큰 관리 방식이 `Authorization` 헤더에 의존한다고 함

`Authorization` 헤더 대신 다른 이름을 사용하면, 자동으로 `Bearer` 토큰을 인식하지 못하고 인증 처리가 누락될 가능성 존재!!

<br />

### 3. 해결책
```java
/** [ JWTFilter 코드 ] **/

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    // Authorization 헤더값 추출
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    // "Bearer " 접두사 제거 후 accessToken만 추출
    String token = header.substring(7);

    
/** [ LoginFilter 코드 ] **/

@Override 
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        ( 생략 )   
        
    // 로그인 성공 시 발급되는 토큰에 대한 응답 설정 -> 표준 방식으로 수정
    response.setHeader("Authorization", "Bearer " + access);
```

<br />

#### < 최종 결과 >

<img src="https://github.com/user-attachments/assets/a8870ebc-0a19-4f98-a1e3-5f40bda4f8a9" width="400px"/>

<img src="https://github.com/user-attachments/assets/1ba4fa6f-da71-4add-b886-1ff49cb7ca6c" width="400px"/>

<br />

<br />

# 5. Docker

## 5-1. Docker 이해하기

## 🐋 Docker 란?

<img width="80%" alt="스크린샷 2021-08-19 오후 6 12 36" src="https://github.com/user-attachments/assets/81b5a8b4-1ade-417d-9f77-9567884d4373">

- 애플리케이션을 신속하게 구축, 테스트 및 배포할 수 있는 소프트웨어 플랫폼

- 애플리케이션과 그에 필요한 모든 라이브러리, 종속성, 설정 파일 등을 **컨테이너**라는 독립된 환경에 패키징하여, 어디서든 일관되게 실행할 수 있도록 해줌 (운영환경 의존X)

- **docker hub**에서 **image**를 `pull` 하고, **image** 를 `run` 하면 **container**가 실행

- 활용 예시 ) 애플리케이션 배포, 개발 환경 구축, `CI/CD` 파이프라인 

<br />

## 🐋 Docker에서의 이미지란?

- 컨테이너를 만들기 위한 템플릿

- 애플리케이션을 실행할 수 있는 환경(운영 체제, 라이브러리, 애플리케이션 파일 등)을 포함하는 **읽기 전용 파일 시스템**
  - 도커 **이미지**에는 애플리케이션 실행에 필요한 소프트웨어와 환경이 모두 들어 있으며, 이를 통해 어디서든 동일한 환경에서 일관되게 애플리케이션을 실행할 수 있음
      

- 예시) **MySQL 이미지**
    - `mysql:5.7`과 같은 Docker 이미지는 MySQL 서버를 실행할 수 있는 환경을 제공하는 템플릿
  
    - `docker pull mysql:5.7` : 'MySQL 이미지를 Docker Hub 에서 다운받는다' 는 의미

<br />

## 🐋 Docker에서의 컨테이너란?

- **이미지**를 실행하여 만든 **실제 실행 중인 환경**

- **컨테이너**는 독립적이고 격리된 환경에서 애플리케이션을 실행

- 예시) **MySQL 컨테이너**
  - `mysql:5.7` **이미지**를 실행하여 MySQL 서버를 작동시키는 **컨테이너**를 생성
  
  - `docker run -d --name mysql-container mysql:5.7` : `mysql:5.7` **이미지**를 실행하여 `mysql-container`이라는 이름의 **MySQL 컨테이너** 만들기

<br />

## 🐋 Docker에서의 네크워크란?

- 여러 **컨테이너**가 서로 **통신**할 수 있도록 해주는 **가상 네트워크**

- 기본적으로 **Docker 컨테이너**는 **브리지 네트워크**에 연결
  ⇒ 동일한 Docker 호스트 내에서 컨테이너들이 서로 통신 가능하게 해줌

```c
docker network create my-network
docker run -d --name container1 --network my-network my-image
docker run -d --name container2 --network my-network my-image
```

✔️ `my-network`라는 네트워크를 만들고, `container1`과 `container2`라는 컨테이너를 그 네트워크에 연결

✔️ 두 컨테이너는 서로 통신 가능!

<br />

## 최종 정리

- **도커 파일 (Dockerfile)** : 이미지를 만드는 설정 파일 (레시피)

- **이미지 (Image)**: 애플리케이션을 실행할 수 있는 환경을 제공하는 템플릿 (레시피의 준비물)

- **컨테이너 (Container)**: 이미지를 실행한 실제 인스턴스 (요리)

- **네트워크 (Network)**: 여러 컨테이너가 서로 통신할 수 있도록 해주는 가상 네트워크 (테이블 연결)

👉 도커 이미지는 **준비된 상태**이고, 도커 컨테이너는 이 준비된 이미지를 **실제로 실행**한 결과

<br />

## 5-2. Docker 기반 스프링부트 빌드하기

## 1. Dockerfile

> Gradle 탭에서 Tasks-build-bootJar 실행 → build/libs 경로에 jar 파일 생성 → Dockerfile 설정

- **Docker 이미지**를 생성하기 위한 설정 파일

- 1개의 컨테이너 생성하는 파일

- 애플리케이션 코드, 라이브러리, 환경 변수, 설정 파일 등을 포함하는 **애플리케이션의 실행 환경**을 나타냄

<br />

## 2. docker-compose.yml

- **MySQL**과 **Spring Boot 애플리케이션**을 컨테이너화하여 실행하는 설정

- 여러 개의 **서비스 (컨테이너)** 를 생성할 수 있는 파일

- 두 서비스를 동일한 **네트워크**에 연결하여 서로 통신할 수 있게끔 해줌

- `depends_on`과 `healthcheck`를 사용하여 **`application` 서비스**가 **`database` 서비스**가 준비된 후에 시작되도록 보장


```yaml
version: "3"

services:
  database:
    container_name: instagram
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: testdb
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      TZ: 'Asia/Seoul'
    ports:
      - "3306:3306"
    command:
      - "--character-set-server=utf8mb4"
      - "--collation-server=utf8mb4_unicode_ci"
    networks:
      - network
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h 127.0.0.1 -p${DB_PASSWORD} --silent"]
      interval: 30s
      retries: 5

  application:
    container_name: main-server
    build:
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: ${DB_URL}
      SPRING_DATASOURCE_USERNAME: ${DB_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
    depends_on:
      database:
        condition: service_healthy
    networks:
      - network
    env_file:
      - .env

networks:
  network:
    driver: bridge
```

## ⭐ 코드 해석

### `services :`

- Compose 파일 내에서 **각각의 컨테이너**를 정의하는 부분
- 각 서비스는 개별 컨테이너로 실행되며, `database`, `application`과 같은 이름을 가진 서비스들을 정의

<br />

### 1. `database` 서비스

```yaml
  database:
    container_name: instagram
```

- `database` 서비스에 대한 컨테이너 이름을 `instagram`으로 설정

✅MySQL 데이터베이스를 실행하는 **Docker 컨테이너** `instagram` 생성!

<br />

`image: mysql:8.0`

- 사용할 **Docker 이미지**를 지정
- 해당 이미지는 MySQL 데이터베이스 서버를 실행하는 데 필요한 모든 구성 요소를 포함

<br />

```yaml
environment:
  MYSQL_DATABASE: testdb
  MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
  TZ: 'Asia/Seoul'
```

- `MYSQL_DATABASE`
  - MySQL이 처음 실행될 때 자동으로 생성할 데이터베이스 이름을 지정 
  - 여기서는 `testdb`라는 데이터베이스가 생성 (내 MySQL 데이터베이스명과 일치)

❗**애플리케이션과 데이터베이스 컨테이너는 동일한 데이터베이스 이름과 비밀번호를 사용**해야함

<br />

```yaml
ports:
  - "3306:3306"
```

- 포트 매핑 ⇒ 로컬 시스템에서 **컨테이너 내 MySQL**에 접근할 수 있다!
- Docker 컨테이너 내의 MySQL 서비스는 기본적으로 3306 포트를 사용하는데 이 포트를 호스트 시스템의 3306 포트와 연결
- `3306:3306` 형식은 호스트(로컬) 포트와 컨테이너 포트를 매핑하는 방식

<br />

```yaml
command:
  - "--character-set-server=utf8mb4"
  - "--collation-server=utf8mb4_unicode_ci"

```

- MySQL 커맨드 옵션
- **이모지**나 **다국어 문자**를 처리하는 데 문제가 없으며, MySQL에서 더 널리 사용되는 **UTF-8 문자 집합**을 사용할 수 있게 해줌

<br />

```yaml
networks:
  - network
```

- 이 서비스가 연결될 네트워크를 지정
- 여기서는 `network`라는 이름의 사용자 정의 네트워크에 이 서비스가 연결됨

<br />

```yaml
healthcheck:
  test: ["CMD-SHELL", "mysqladmin ping -h 127.0.0.1 -p${DB_PASSWORD} --silent"]
  interval: 30s
  retries: 5
```

`healthcheck`는 컨테이너가 정상적으로 작동하는지 확인하는 방법을 설정하는 역할

- `application` 서비스가 **`database` 서비스가 준비되었을 때만** 실행되도록 할 수 있음

- `test`: `mysqladmin ping` 명령어를 사용하여 MySQL 서버가 정상적으로 동작하는지 확인
  
    ✔️ `-h 127.0.0.1`은 MySQL 서버의 호스트를 지정 
    
    ✔️ `-p${DB_PASSWORD}`는 MySQL의 root 비밀번호를 전달

- `interval`: `healthcheck`를 실행하는 간격

- `retries`: `healthcheck` 실패 시 재시도 횟수를 설정

<br />

### 2. `application` 서비스

```yaml
application:
  container_name: main-server
```

- 이 서비스의 컨테이너 이름을 `main-server`로 설정

<br />

```yaml
build:
  dockerfile: Dockerfile
```

- 애플리케이션 이미지를 빌드할 때 사용할 `Dockerfile`을 지정
  
    ⇒ 현재 디렉토리에서 `Dockerfile`을 사용하여 애플리케이션 이미지를 빌드함
- `Dockerfile` : 내 현재 Spring Boot 애플리케이션을 Docker 이미지로 만든 것

<br />

```yaml
ports:
  - "8080:8080"
```

- 애플리케이션이 사용하는 포트를 호스트와 연결
- `8080` 포트를 매핑하여, 호스트의 `8080` 포트에서 애플리케이션에 접근할 수 있도록 함

<br />

```yaml
environment:
  SPRING_DATASOURCE_URL: ${DB_URL}
  SPRING_DATASOURCE_USERNAME: ${DB_USERNAME}
  SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
```

- Spring Boot 애플리케이션이 데이터베이스에 연결하기 위한 설정
- `DB_URL` = `jdbc:mysql://instagram:3306/testdb?useSSL=false&serverTimezone=Asia/Seoul`
  
⭐ 여기서 **database 컨테이너명**인 `instagram` 을 사용하고 있음을 주목하자!

`application` 서비스는 `database` 서비스에 접근할 때 `instagram`을 호스트 이름으로 사용하여 MySQL에 연결하기 때문이다!!

❗ 해당 값들은 원래 사용 중인 실제 MySQL 데이터베이스의 정보와 동일하게 설정해야 함

<br />

```yaml
depends_on:
  database:
    condition: service_healthy
```

- `depends_on`
    - `application` 서비스가 시작되기 전에 `database` 서비스가 실행 중이어야 하며, **단순히 실행 순서만 보장**
- `condition: service_healthy`
    - `application` 서비스가 시작되기 전에 `database` 서비스가 **건강한 상태**(즉, `healthcheck`가 성공적으로 통과한 상태)일 때만 실행
    - `database` 서비스의 `healthcheck`가 성공할 때까지 `application` 서비스의 시작을 지연시킴

<br />

```yaml
networks:
  - network
```

- `application` 서비스가 `network` 네트워크에 연결되도록 설정
- `database` 서비스와 동일한 네트워크에 속하게 되어 서로 통신할 수 있음

<br />

### 3. network

```yaml
networks:
  network:
    driver: bridge
```

- 사용자 정의 네트워크를 정의
- `bridge`는 기본 Docker 네트워크 드라이버로, 이 네트워크에 연결된 컨테이너는 서로 통신할 수 있음

❗ 컨테이너 간 연결을 위해 네트워크는 필수!

<br />

## 📢 트러블 슈팅 - 1 < MySQL 서버와의 연결 실패 >
### 1. 문제점
1. `connection refused` 문제
2. `Communications link failure` 문제


#### 두 에러의 공통 원인:
- **MySQL 서버가 실행되지 않음** 또는 **연결 준비가 되지 않음**
- **잘못된 연결 정보** (호스트, 포트, 사용자명, 비밀번호 등)

두 문제 모두 MySQL 서버와의 연결이 실패했을 때 발생하는 문제라고 한다..

<br />

### 2. 발생 원인
**✅ 첫번째 원인으로는,** 

현재 나의 데이터베이스 비밀번호에 `$` 가 포함되어 있는데, `yml`에 환경변수를 사용하지 않고 바로 작성하다보니 해당 문자를 인식하지 못해 연결 거부 현상이 발생하였다.

**✅ 두번째 원인으로는,**

`depends_on` 만 사용하고 `Healthcheck` 는 해주지 않았다. 

`application` 서비스가 `database` 서비스에 연결하려고 시도할 때, MySQL 서버가 아직 완전히 시작되지 않았거나 준비되지 않은 상태일 수 있기 때문에 이를 확인해주는 로직이 필요하다고 한다!

#### ✔️️ `depends_on`과 `healthcheck`의 차이점

- `depends_on` : 서비스가 **실행되었는지**만 확인, 실행 순서를 보장하지만 서비스가 실제로 **준비되었는지**는 보장하지 않음
- `healthcheck` : **서비스가 정상적으로 작동하는지** (즉, MySQL이 준비되었는지) 확인하는 데 사용

`depends_on`만 사용했을 경우, `database` 서비스가 **실행 중**인지! **준비 상태**인지! 확인할 방법이 없다..

<br />

### 3. 해결 방법
> thanks to.. 최서지 (`@choiseoji`)
1. 환경변수 `.env` 파일을 만들어주었다!
2. `Healthcheck` 기능을 추가해주었다!

### ⭐ Healthcheck 의 역할

- 컨테이너가 정상적으로 동작하는지 주기적으로 검사하는 기능

- **컨테이너가 정상적으로 준비되었을 때만** 다른 서비스가 시작되도록 할 수 있음

- 예시) `database` 컨테이너 실행 OK!! 👉 `application` 컨테이너 실행 시작!!

<br />

### 4. 최종 결과
![스크린샷 2024-11-14 140257](https://github.com/user-attachments/assets/4ffc60e1-d461-4e98-b73d-a79155b7f436)
![스크린샷 2024-11-14 140336](https://github.com/user-attachments/assets/ecac7e43-8b4e-41c2-b2b5-925fd10ebb51)

<br />

## 📢 트러블 슈팅 - 2 < Access denied for user 'root'@'172.18.0.1' 오류  >
### 1. 문제점
Docker 연결 잘됐고, `8080`도 다 잘 떴어.

그래서 이제 API 리팩토링하고 스프링부트 `run` 했는데 `Access denied for user 'root'@'172.18.0.1'` 에러가 뜨네?

해당 오류는 MySQL 서버에서 root 사용자가 **IP 주소 172.18.0.1**에서 접속을 거부당했다는 것을 의미한다..

<br />

### 2. 발생 원인
MySQL에서 **root 사용자**는 기본적으로 로컬에서만 접속을 허용하도록 설정된다고 한다.

즉, root 사용자가 `localhost` 또는 `127.0.0.1`에서만 접속할 수 있도록 제한되어 있다는 의미이다.

이러한 상황에서 Docker 네트워크 내 다른 컨테이너 (외부 IP) 에서 접속을 시도하는 상황이기 때문에 접근을 차단하는 것이었다!

<br />

### 3. 해결 방법
root 사용자가 다른 IP에서도 접속할 수 있도록 권한을 수정해야 한다!

1. MySQL 컨테이너에 접속

```yaml
docker exec -it <mysql-container-name> mysql -u root -p
```

2. root 사용자에 대해 외부 호스트에서의 접속을 허용하도록 권한 변경
```yaml
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'your_password' WITH GRANT OPTION;

FLUSH PRIVILEGES;
```

❗ 이때, **DB_URL** 은 `localhost` 로 다시 수정하기 

<br />

<br />

# 6. Deploy
# 도커 이미지 배포하기

## 1. AWS 주요 용어 정리

## 🖥️ EC2 란?

> EC2 = Elastic Compute Cloud

AWS에서 원격으로 제어할 수 있는 **가상의 컴퓨터** 한 대를 빌리는 것이다.

EC2를 하나의 컴퓨터라고 생각하면 된다!

### EC2를 사용해야 하는 이유?

서버를 배포하기 위해서는 컴퓨터가 필요하다. 이때 나의 컴퓨터에서 서버를 배포해서 다른 사용자들이 인터넷을 통해 접근할 수 있게 만들 수도 있다. 하지만 내 컴퓨터로 서버를 배포하면 24시간 동안 컴퓨터를 켜놔야한다. 또한 인터넷을 통해 내 컴퓨터에 접근할 수 있게 만들다 보니 보안적으로도 위험할 수 있다.

이러한 불편함 때문에 내가 가지고 있는 컴퓨터를 사용하지 않고, AWS EC2라는 컴퓨터를 빌려서 사용하는 것이다!

### EC2 인스턴스 생성

EC2 인스턴스를 생성하면, 선택한 AMI에 따라 운영체제, CPU, RAM 등이 미리 구성된 컴퓨터를 사용할 수 있게 된다!

인스턴스를 만들 때, 여러 인스턴스 유형 가운데 하나를 고르고 사이즈 등을 고른다. 이러한 과정을 통해 내가 만들 가상 서버의 목적에 따라서 특화된 서버를 만들 수 있다.

### AMI

> AMI = Amazon Machine Image

내가 선택한 서버 특화 옵션을 모아둔 것으로, 인스턴스 생성 전에 미리 구성되는 **이미지 파일**이다.

EC2 인스턴스를 시작할 때 필요한 정보를 포함한다.

✅ 운영 체제, 애플리케이션 서버, 언어 런타임, 데이터베이스 등 인스턴스에서 실행되는 모든 소프트웨어의 설정이 포함

### 키 페어

EC2 인스턴스에 접속하기 위해 사용되는 암호화된 파일이다.

발급받은 프라이빗 키를 이용하여 인스턴스에 접근할 수 있기 때문에 키를 안전하게 보관하는 것이 중요하다!

### EBS 볼륨

> EBS = Elastic Block Storage

EBS란, 클라우드에서 사용하는 **가상 하드디스크**이다.

EC2 인스턴스가 연산(CPU, 메모리)에 관한 처리를 한다고 하면, **데이터를 저장하는 역할은 바로 EBS가 한다**고 보면 된다!

**EBS 볼륨**이란, EBS로 생성한 디스크 하나하나를 뜻하는 저장 단위를 말한다.

쉽게 말해, 윈도우의 C드라이브, D드라이브는 각각의 디스크이며 EBS 볼륨이다!

### 보안그룹

EC2 인스턴스에 허용되는 인바운드, 아웃바운드 트래픽을 제어하는**가상 방화벽**이다.

즉, 연결된 리소스에 도달하고 나갈 수 있는 트래픽을 제어한다.

⭐ **인바운드**

- **외부에서 인스턴스에 접근**하는 트래픽에 대한 허용 범위 제어

- 클라이언트가 자신의 서버 데이터에 들어올 수 있는 규칙

- 기본적으로 인바운드 규칙은 모든 포트를 닫는 것을 전제

⭐ **아웃바운드**

- **서버에서 외부로 나가는** 트래픽에 대한 허용 범위 제어

- 기본적으로 모든 아웃바운드 트래픽을 허용

### 스왑 메모리

실제 메모리 RAM이 가득 찼지만 더 많은 메모리가 필요할때 디스크 공간을 이용하여 부족한 메모리를 대체할 수 있는 공간을 의미한다.

EC2 프리티어를  사용하는 경우 RAM이 1GB이기 때문에 빌드나 실행을 진행하다 컴퓨터가 멈출 수 있다!

이를 방지하기 위해 사용하는 것이 **스왑 메모리 설정**이라고 한다.

## 🖥️ IP 주소 종류

처음 EC2 인스턴스를 만들면 퍼블릭 IPv4 주소와 프라이빗 IPv4 주소가 할당된다.

### 퍼블릭 IP

- 인터넷 상에서 개개인의 로컬 네트워크를 구분하기 위해 ISP에서 제공하는 IP 주소

- **외부에 공개**가 되어있어서 다른 인터넷 사용자들이 나에게 접속 할 수 있다.

SSH 접속 시 인스턴스의 퍼블릭 주소를 통해 인스턴스 내부로 접속할 수 있다.

### 프라이빗 IP

- 외부에서는 접속할 수 없는 네트워크 망

우리는 이 주소에 직접적으로는 접근하지 못한다. 오직 외부로 열려있는 퍼블릭 IP를 통해서만 인스턴스에 접근할 수 있다!

### 탄력적 IP

퍼블릭 IP만으로도 EC2 인스턴스를 사용하는데는 별 문제가 없어보이는데 **탄력적 주소**는 왜 있는것일까?

AWS는 인스턴스가 처음 생성되거나, 중지 후 다시 시작할 때마다 **퍼블릭 IP를 재할당**한다. IP 주소가 매번 바뀌게 되는 것이다!

서비스 운영의 안정성을 위해 중지 후 재시작마다 바뀌지 않는 **고정된 IP 주소**가 필수적이다!

## 🖥️ RDS 란?

> RDS = Relational Database Service

관계형 데이터베이스를 제공하는 AWS의 서비스이다.

RDS를 사용할 경우, AWS에서 모든 것을 관리하기 때문에 데이터베이스 부분에 대해 신경을 쓰지 않고 개발을 진행할 수 있다는 장점이 있다!

### VPC 보안그룹

> VPC = Virtual Private Cloud

![image](https://github.com/user-attachments/assets/5f21afdb-50e2-45db-953b-bd5610b613d7)
물리적으로 같은 클라우드 상에 있지만, 보안상의 목적을 위해 논리적으로 **다른 클라우드인 것처럼 동작하도록 만든 가상 클라우드 환경**이다.

VPC별로 다른 네트워크 설정을 할 수 있고, 독립된 네트워크처럼 작동한다.

## 2. 수동 배포하기

### 인텔리제이 (내 스프링 애플리케이션)

```java
// 테스트 생략하고 스프링 애플리케이션 빌드
./gradlew clean build -x test

// 도커 이미지를 빌드하고 생성하는 명령어
docker build --platform linux/amd64 -t [도커아이디]/[리포지토리명] 

// 도커 허브에 이미지 올리기
docker push [도커아이디]/[리포지토리명]
```

- `docker build` 는 현재 디렉토리에 있는 Dockerfile 기반으로 이미지 생성

✅ 내 스프링부트 애플리케이션을 실행할 수 있는 환경과 파일(JAR 파일)이 들어있는 이미지

### SSH 접속 (콘솔창)

```java
// 패키지 업데이트
sudo apt update

// 도커 설치
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
sudo apt update
sudo apt install docker-ce
docker --version

// Docker Hub에서 이미지 다운로드
sudo docker pull [도커아이디]/[리포지토리명]

// 이미지 기반으로 도커 컨테이너 실행
sudo docker run -e .env -d -p 80:8080 [도커아이디]/[리포지토리명]
```

- `-p 80:8080` : 호스트의 `포트 80`을 컨테이너 내부의 `포트 8080`으로 매핑

### .env

```java
DB_URL=jdbc:mysql://{RDS 엔드포인트 주소}:3306/{RDS 데이터베이스 이름}
DB_USERNAME=
DB_PASSWORD=
```

### 최종 결과
![스크린샷 2024-11-22 224604](https://github.com/user-attachments/assets/8892971b-bfd9-48f4-a1a6-7e7f00bb6f42)

✅퍼블릭 주소를 통해 들어가보면 올바르게 뜨는 것을 확인할 수 있었다!

<br />

# 배포환경에 대한 테스트
![스크린샷 2024-11-23 010424](https://github.com/user-attachments/assets/a41b247c-d52f-47fe-a968-0aeec1811374)

포스트맨을 통해 API 테스트를 진행해보았는데 `401 에러`가 발생하였다.

`signup` API의 경우, 스프링 시큐리티에서 permitAll 설정을 해주었는데도 불구하고 왜 `401 에러`가 발생할까? 응답 형태도 이상하다..

이거 왜 이러는 걸까요오..🥲

### 해결했습니다..

> 도현님 ) validation 하기 전에 예외가 발생한 느낌이다.. 필터쪽에 문제가 있는거면 헤더에 토큰을 넣었다던가.. 헤더를 확인해봐라

네 맞습니다. 저는 요청 헤더에 기존에 테스트했던 Authorization 필드를 그대로 넣고 회원가입 요청을 보내고 있었습니다..

이걸 빼니까 너무 잘되더라구요.. 

배포가 잘되었다니.. 다행입니다 흑🥲

![스크린샷 2024-11-24 014331](https://github.com/user-attachments/assets/99fd0ef7-9013-4907-b718-7e4608587679)
