package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorReqUpdateDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuthorServiceTest { //c -> s -> r 레포에 목(가라)값을 넣고 서비스에
    @Autowired
    private AuthorService authorService;

//    가짜객체를 만드는 작업을 목킹이라고 한다.
    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void findAllTest(){
//        Mock repository 기능 구현
        List<Author> authors = new ArrayList<>();
        authors.add(new Author());
        authors.add(new Author());
        Mockito.when(authorRepository.findAll()).thenReturn(authors);
        for (Author author : authorRepository.findAll()){
            System.out.println(author.getId());
        }
//    검증                      가짜 레포의 갯수
        Assertions.assertEquals(authors.size(), authorService.findAll().size());
//      findAllTest를 실행하면 authorRepo에 2명의 author이 들어가고 그걸 authorService에도 2명이 들어갔는지 확인용
    }
    @Test
    void update(){
        Long authorId = 1L;
        Author author  = Author.builder() //이게 원래 DB에 있던 값이라고 치는것
                .name("test1")
                .email("email1")
                .password("1234")
                .build();
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorReqUpdateDto authorReqUpdateDto = new AuthorReqUpdateDto();
        authorReqUpdateDto.setName("test2");
        authorReqUpdateDto.setEmail("email2");
        authorReqUpdateDto.setPassword("4321");
        authorService.update(authorId, authorReqUpdateDto);
        Assertions.assertEquals(author.getName(), authorReqUpdateDto.getName());
        Assertions.assertEquals(author.getPassword(), authorReqUpdateDto.getPassword());

    }

    @Test
    void AuthorDetail(){
//       Mockito을 이용해 가짜 레포데이터 값을 만들고, Dto에 service의
//       메소드 값(매개변수에 목키토와 서비스 모두 공통된 값을 넌다)을 집어 넣어 목키토에 값과 비교한다.
        Long authorId = 1L;
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder()
                .title("hi")
                .contents("hihi")
                .build();
        posts.add(post);
        Author author  = Author.builder() //이게 원래 DB에 있던 값이라고 치는것
                .name("test1")
                .email("email1")
                .password("1234")
                .password("1234")
                .role(Role.ADMIN)
                .posts(posts)
                .build();
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        AuthorDetailResDto authorDetailResDto = authorService.findAuthorDetail(authorId);
        Assertions.assertEquals(author.getName(), authorDetailResDto.getName());
        Assertions.assertEquals(author.getPosts().size(), authorDetailResDto.getPostCount());
        Assertions.assertEquals("관리자", authorDetailResDto.getRole());
    }


}
