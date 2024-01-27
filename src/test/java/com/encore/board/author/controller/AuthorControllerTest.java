package com.encore.board.author.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

// WebMvsTest를 이용해서 Controller계층을 테스트. 모든 스프링빈을 생성하고 주입하지는 않음.
//@WebMvcTest(AutoCloseable.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {
//    @Autowired //싱글톤으로 만들어진 객체 주입 받은거
//    private MockMvc mockMvc;
//    @MockBean
//    private AuthorService authorService;
//    @Test
////    @WithMockUser // security 의존성 추가 필요
//    void authorDetailTest() throws Exception{
//        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
//        authorDetailResDto.setName("testname");
//        authorDetailResDto.setEmail("test@test");
//        authorDetailResDto.setPassword("1234");
//        Mockito.when(authorService.findAuthorDetail(1L)).thenReturn(authorDetailResDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("author/1/circle/dto"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$"))
//
//    }
}
