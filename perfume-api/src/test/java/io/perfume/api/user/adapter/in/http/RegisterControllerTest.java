package io.perfume.api.user.adapter.in.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.perfume.api.user.adapter.in.http.dto.EmailVerifyConfirmRequestDto;
import io.perfume.api.user.adapter.in.http.dto.SendEmailVerifyCodeRequestDto;
import io.perfume.api.user.application.port.in.dto.ConfirmEmailVerifyResult;
import io.perfume.api.user.application.port.in.dto.SendVerificationCodeResult;
import io.perfume.api.user.application.service.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterService registerService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("본인 이메일을 인증한다.")
    void confirmEmail() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        ConfirmEmailVerifyResult result = new ConfirmEmailVerifyResult("sample@mail.com", now);
        given(registerService.confirmEmailVerify(anyString(), anyString(), any())).willReturn(result);
        EmailVerifyConfirmRequestDto dto = new EmailVerifyConfirmRequestDto("code", "key");

        // when & then
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/signup/email-verify/confirm")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("verify-email",
                                requestFields(
                                        fieldWithPath("code").description("본인 인증 코드"),
                                        fieldWithPath("key").description("본인 인증 요청 시 발급받은 키")
                                ),
                                responseFields(
                                        fieldWithPath("email").description("인증 완료된 이메일"),
                                        fieldWithPath("verifiedAt").description("본인 인증 완료 시간")
                                )));
    }

    @Test
    @DisplayName("본인 이메일 인증을 위한 코드 발송을 요청한다.")
    void testEmailVerifyRequest() throws Exception {
        // given
        String email = "sample@mail.com";
        SendEmailVerifyCodeRequestDto dto = new SendEmailVerifyCodeRequestDto(email);
        LocalDateTime now = LocalDateTime.now();
        SendVerificationCodeResult result = new SendVerificationCodeResult("key", now);
        given(registerService.sendEmailVerifyCode(any())).willReturn(result);

        // when & then
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/signup/email-verify/request")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("send-verify-code",
                                requestFields(
                                        fieldWithPath("email").description("본인 인증을 위한 이메일")
                                ),
                                responseFields(
                                        fieldWithPath("key").description("이메일 본인 인증 시 필요한 키"),
                                        fieldWithPath("sentAt").description("본인 확인 이메일 발송 시간")
                                )));
    }

    @Test
    @DisplayName("잘못된 이메일 형식을 요청한 경우 BAD_PARAMETER 응답을 한다.")
    void testEmailVerifyRequestIfInvalidEmailFormat() throws Exception {
        // given
        String email = "sample";
        SendEmailVerifyCodeRequestDto dto = new SendEmailVerifyCodeRequestDto(email);

        // when & then
        mockMvc
                .perform(MockMvcRequestBuilders.post("/v1/signup/email-verify/request")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
