package com.encore.board.author.repository;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

// DataJpaTest 어노테이션을 사용하면 매 테스트가 종료되면 자동으로 DB 원상복구
//모든 스프링빈을 생성하지 않고, DB테스트 특화 어노테이션 따라서 @Authowire안됨 빈이 생성이 안돼서, 근데 안해서 테스트코드안에서는 생성이 된다.
@DataJpaTest //(Transactional이 없어도)롤백 됨, 디비 관련한 빈을 만든다 속도 빠름
//replace = AutoConfigureTestDatabase.Replace.Any : H2DB(spring 내장 인메모리) 가 기본설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest 는 자동롤백기능은 지원하지 않고, 별도로 롤백 코드 또는 어노테이션 필요.
//SpringbootTest 어노테이션은 실제 스프링 실행과 동일하게 스프링빈 생성 및 주입 따라서 @Authowire됨
//@SpringBootTest // 실행하자마자 스프링빈을 다 만듦
//@Transactional // 이거 없으면 데이터베이스에서 값이 계속 들어감 그래서 테스트 할때마다 데이터 바꿔줘야함
@ActiveProfiles("test") //application-test.yml 파일을 찾아 설정값 세팅
public class AuthorReopsitoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest(){
//        객체를 만들고 save하고 -> 재조회 -> 방금 만든 객체랑 비교
//        준비(prepare, given)
        Author author = Author.builder()
                .email("123333")
                .name("444")
                .password("555")
                .role(Role.ADMIN)
                .build();
//        실행(excute, when)
        authorRepository.save(author);
        Author authorDb = authorRepository.findByEmail("123333").orElse(null);
//        검증(then)
//        Assertions클래스의 기능을 통해 오류의 원인파악, null처리, 가시적으로 성공/실패 여부 확인
        Assertions.assertEquals(author.getEmail(), authorDb.getEmail());
    }

}
