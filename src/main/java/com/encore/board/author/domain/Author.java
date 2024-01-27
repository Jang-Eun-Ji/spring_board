package com.encore.board.author.domain;

import com.encore.board.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor //jpa에서 객체를 조립할때 필요함

//@Builder
//@AllArgsConstructor
// 위와같이 모든 매개변수가 있는 생성자를 생성하는 어노테이션과 Builder를 클래스에 붙여
//모든 매개변수가 있는 생성자 위에 Builder어노테이션을 붙인것과 같은 효과가 있음.
public class Author {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long id;
    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false, length = 20, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
//    author를 조회할때 post객체가 필요할시에 선언
//    필요없으면 안쓰는게 낫다. -순환참조: Author객체를 json으로 직렬화시에 post를 조회하고 post는 author를 가져옴
//    mappedby를 연관관계의 주인이라 부르고, fk를 관리하는 변수명을 명시
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // post와 연결시 여기다 연결할거다 라고 하는 의미
//    @OneToOne - user 과 user_detail같은 관계에서 쓴다
    @Setter // cascade.persist를 위한 테스트
    private List<Post> posts;
//    post와 달리 데이터베이스에 Post의 컬럼이 안생겨유

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;
//    builder어노테이션을 통해 빌터 패턴으로 객체 생성
//    매개변수의 세팅순서, 매개변수의 개수 등을 유연하게 세팅
    @Builder
    public Author(String name, String email, String password, Role role, List<Post>posts){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.posts = posts;

    }
    public void updateAuthor(String name, String password){
        this.name = name;
        this.password = password;
    }
}
