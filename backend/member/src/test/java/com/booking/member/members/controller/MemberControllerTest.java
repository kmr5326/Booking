package com.booking.member.members.controller;

import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    private final String baseUrl = "/api/members";

    @Test
    @DisplayName("회원 가입 요청 성공한다")
    void t1() throws Exception {

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
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별, 남자 : MALE, 여자 : FEMALE"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullName").description("풀네임"),
                                        fieldWithPath("address").description("주소")
                                ))
                );
    }

    @Test
    @DisplayName("회원 정보를 조회한다.")
    void t2() throws Exception {
        String loginId = "1234";
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto("1234", "email", 10, "MALE",
                "mono", "monono", "addr", "img", "google");

        when(memberService.loadMemberInfo(any())).thenReturn(responseDto);

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/memberInfo/{loginId}", loginId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()) // 200 반환
                .andDo(
                        document("/member/info",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("loginId").description("로그인 id")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").description("로그인 id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullname").description("이름"),
                                        fieldWithPath("address").description("주소"),
                                        fieldWithPath("profileImage").description("프로필 이미지"),
                                        fieldWithPath("provider").description("google,kakao")
                                ))

                );
    }

}
