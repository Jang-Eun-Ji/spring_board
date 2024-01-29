package com.encore.board.author.contorller;

import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Slf4j - 쉽게 로그백 로그 라이브러리 사용 가능
@Slf4j
public class TestController {

//    @Slf4j 어노테이션(롬복)을 사용하지 않고, 직접 라이브러리 import하여 로거 생성 가능
//    private static final Logger log = LoggerFactory.getLogger(LogTestController.class);

    @Autowired
    private AuthorService authorService;

    @GetMapping("log/test1")
    public String testMethod1(){
        log.debug("디버그 로그입니다.");
        log.info("인포 로그입니다.");
        log.error("에러 로그입니다.");
        return "OK";
    }
    @GetMapping("exception/test1/{id}")
    public String exceptionTestMethod1(@PathVariable Long id){
        authorService.findById(id);
        return "OK";
    }

}
