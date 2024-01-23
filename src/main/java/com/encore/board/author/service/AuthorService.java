package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorReqUpdateDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class AuthorService {
    private final AuthorRepository authorRepository;
    @Autowired // 생성자 하나면 자동으로 오토와이어 기능이 붙는디 그냥 써봄
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto){
        Role role = null;
        if (authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")){
            role = Role.USER;
        }else {
            role = Role.ADMIN;
        }
//        일반생성자 방식
//        Author author = new Author(authorSaveReqDto.getName(), authorSaveReqDto.getEmail(), authorSaveReqDto.getPassword(), role);
//        빌터패턴 // 이메일 이름 비번 순서 상관없음
        Author author = Author.builder()
                .email(authorSaveReqDto.getEmail())
                .name(authorSaveReqDto.getName())
                .password(authorSaveReqDto.getPassword())
                .build();
        authorRepository.save(author);
    }
    public List<AuthorListResDto> findAll(){
        List<Author> authors = authorRepository.findAll(); // spring JPA 에서 제공하는 findAll이다
        List<AuthorListResDto> authorLists = new ArrayList<>();
        for(Author author: authors){
            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(author.getId());
            authorListResDto.setName(author.getName());
            authorListResDto.setEmail(author.getEmail());
            authorLists.add(authorListResDto);
        }
        return authorLists;
    }
    public Author findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);//값이 없으면
        return author;
    }
    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);//값이 없으면
        String role = null;
        if (author.getRole() == null || author.getRole().equals(Role.USER)){
            role = "일반유저";
        }else {
            role = "관리자";
        }
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        authorDetailResDto.setRole(role);
        return authorDetailResDto;
    }
    public void update(Long id, AuthorReqUpdateDto authorReqUpdateDto) throws EntityNotFoundException {
        Author author = this.findById(id);
        author.updateAuthor(authorReqUpdateDto.getName(), authorReqUpdateDto.getPassword());
        authorRepository.save(author);
    }

    public void delete(Long id) throws EntityNotFoundException{
        authorRepository.deleteById(id);
    }

}
