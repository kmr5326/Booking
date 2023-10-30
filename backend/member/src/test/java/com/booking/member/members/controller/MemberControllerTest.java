package com.booking.member.members.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberControllerTest extends ControllerTest {

    private final String baseUrl = "/api/members";

    @Test
    @DisplayName("회원 가입 요청 성공한다")
    void t1 () throws Exception {

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("loginId", "email", 20, "MALE", "nickname", "fullName", "address");
        doNothing().when(memberService).signup(signUpRequestDto);

        mockMvc.perform(post(baseUrl + "/signup") // 요청 HTTP METHOD, 주소
            .contentType(MediaType.APPLICATION_JSON) // JSON
            .content(objectMapper.writeValueAsBytes(signUpRequestDto)) // Jackson
        ).andExpect(status().isOk()) // 200 반환
            .andDo(
                document("/member/signup", // restdocs 선언,
                    preprocessRequest(prettyPrint()), // json을 이쁘게 표시해라
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("age").description("나이"),
                        fieldWithPath("gender").description("성별, 남자 : MALE, 여자 : FEMALE"),
                        fieldWithPath("nickname").description("닉네임"),
                        fieldWithPath("fullName").description("풀네임"),
                        fieldWithPath("address").description("주소")
                    ))
            );
    }

}
