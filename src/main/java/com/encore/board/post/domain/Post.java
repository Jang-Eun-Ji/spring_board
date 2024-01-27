package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor //jpa에서 객체를 조립할때 필요함
@Builder
@AllArgsConstructor
public class Post {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 300, unique = true)
    private String contents;

//    author_id 는 DB의 컬럼명이다., 별다른 옵션없으면 pk(post의 id)에 fk가 설정된다
    @ManyToOne(fetch = FetchType.LAZY) //LAZY 지연, EAGER: 즉시로딩
    // post입장에서 한사람이 여러개의 글을 쓸수 있으므로 N:1
    @JoinColumn(name = "author_id")
//    @JoinColum(nullable = false, name = "author_email", referenceColumnName="email" - /fk로 가지고올 컬럼 이름/)
    private Author author; // 이게 있으면 post테이블 조회할때 author테이블의 조회 후 author의 raw를 가져옴

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;


    private String appointMent;
    private LocalDateTime appointMentTime;


//    public Post(String title, String contents, Author author){
//        this.title = title;
//        this.contents = contents;
//        this.author = author;
////        author객체의 posts를 초기화 시켜준 후
////        this.author.getPosts().add(this);
//    }
    public void updatePost(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
    public void UpdateAppointment(String appointMent){
        this.appointMent= appointMent;
    }


}
