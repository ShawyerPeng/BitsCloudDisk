package exception;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 进行全局的异常控制
 */
public class RestfulExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        System.out.println("This is exception handler method!");
        if (ex == null) {
            return null;
        }

        ModelAndView mv = null;
        try {
            int statusCode = 500;

            try {
                if (ex instanceof NoSuchRequestHandlingMethodException) {
                    statusCode = HttpServletResponse.SC_NOT_FOUND;
                } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                    statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
                } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                    statusCode = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
                } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                    statusCode = HttpServletResponse.SC_NOT_ACCEPTABLE;
                } else if (ex instanceof MissingServletRequestParameterException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof ServletRequestBindingException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof ConversionNotSupportedException) {
                    statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                } else if (ex instanceof TypeMismatchException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof HttpMessageNotReadableException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof HttpMessageNotWritableException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof MethodArgumentNotValidException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof MissingServletRequestPartException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof BindException) {
                    statusCode = HttpServletResponse.SC_BAD_REQUEST;
                } else if (ex instanceof NoHandlerFoundException) {
                    statusCode = HttpServletResponse.SC_NOT_FOUND;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> data = new HashMap<>();
            data.put("type", ex.getClass().getSimpleName());
            data.put("message", ex.getMessage());

            mv.addObject("data", data);
            mv.addObject("statusCode", statusCode);
            mv.addObject("message", ex.getLocalizedMessage());

            return mv;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
