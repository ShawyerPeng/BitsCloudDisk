package exception;//package exception;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.io.IOException;
//
//@Slf4j
//@ControllerAdvice
//public class RestfulResponseExceptionHandler extends ResponseEntityExceptionHandler {
//    /**
//     * 数据找不到异常
//     */
//    @ExceptionHandler({DataNotFoundException.class})
//    public ResponseEntity<Object> handleDataNotFoundException(RuntimeException ex, WebRequest request) throws IOException {
//        return getResponseEntity(ex, request, ReturnStatusCode.DataNotFoundException);
//    }
//
//    /**
//     * 根据各种异常构建 ResponseEntity 实体. 服务于以上各种异常
//     */
//    private ResponseEntity<Object> getResponseEntity(RuntimeException ex, WebRequest request, ReturnStatusCode specificException) {
//        ReturnTemplate returnTemplate = new ReturnTemplate();
//        returnTemplate.setStatusCode(specificException);
//        returnTemplate.setErrorMsg(ex.getMessage());
//
//        return handleExceptionInternal(ex, returnTemplate,
//                new HttpHeaders(), HttpStatus.OK, request);
//    }
//
//}
