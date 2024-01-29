package com.encore.board.commone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AopLogService {
    //aop의 대상이 되는 controller, service 등 위치를 정의
    //모든 컨트롤러의 모든 메서드가 대상 : 패키지명과 메서드명의 표현식
//    @Pointcut("execution(* com.encore.board..controller..*.*(..)")
    //모든 컨트롤러의 모든 메서드가 대상 : 어노테이션 표현식
    @Pointcut("within(@org.springframework.stereotype.Controller *)") //RestController 사용 시 변경
    public void controllerPointcut(){

    }

//    @Before("controllerPointcut()") // Pointcut에서 정의한 위치를 대상으로 하겠다
//    public void beforeController(JoinPoint joinPoint){
//        log.info("Before Controller");
////        메서드가 실행되기 전에 인증, 입력 값 검증 등을 수행하는 용도로 사용하는 사전단계
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest req = requestAttributes.getRequest();
////		JSON 형태로 사용자의 요청을 조립하기 위한 로직
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.createObjectNode();
//        objectNode.put("Method Name ", joinPoint.getSignature().getName());
//        objectNode.put("CRUD Name ", req.getMethod());
//        Map<String, String[]> paramMap = req.getParameterMap();
//        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
//        objectNode.set("user Inputs ", objectNodeDetail);
//        log.info("User Request INFO " + objectNode);
//    }
//    @After("controllerPointcut()")
//    public void afterController(){
//        log.info("end controller");
//    }

//      방식 2 around사용/ 가장 빈번히 사용
//    Before, After가 실행
@Around("controllerPointcut()") // join point란 AOP 대상으로 하는 컨트롤러의 특정 메서드를 의미
public Object controllerLogger(ProceedingJoinPoint proceedingJoinPoint) {

//		log.info("request Method" + proceedingJoinPoint.getSignature().toString());
//		사용자의 요청값을 출력하기 위해 HttpServletRequest객체를 꺼내는 로직
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest req = requestAttributes.getRequest();
//		JSON 형태로 사용자의 요청을 조립하기 위한 로직
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("Method Name ", proceedingJoinPoint.getSignature().getName());
    objectNode.put("CRUD Name ", req.getMethod());
    Map<String, String[]> paramMap = req.getParameterMap();
    ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
    objectNode.set("user Inputs ", objectNodeDetail);
    log.info("User Request INFO " + objectNode);
    try {
        return proceedingJoinPoint.proceed();
//			본래의 Controller Method 호출
    } catch (Throwable e) {
        log.error(e.getMessage());
        throw new RuntimeException(e);
    } finally{
        log.info("end controller");
    }
}

}

