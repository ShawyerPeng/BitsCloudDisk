package filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT 验证 Interceptor
 */
public class TokenInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String tokenStr = httpServletRequest.getParameter("token");
        String token = httpServletRequest.getHeader("token");
        if (token == null && tokenStr == null) {
            System.out.println("real token:======================is null");
            String str = "{'statusCode':801,'message':'缺少token，无法验证','data':null}";
            dealErrorReturn(httpServletRequest, httpServletResponse, str);
            return false;
        }
        if (tokenStr != null) {
            token = tokenStr;
        }
        JwtUtil jwtUtil = new JwtUtil();
        token = jwtUtil.updateToken(token);
        System.out.println("real token:==============================" + token);
        System.out.println("real other:==============================" + httpServletRequest.getHeader("Cookie"));

        // 解决Token验证问题
        httpServletResponse.setHeader("token", token);
        // 解决跨域问题
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    // 检测到没有token，直接返回不验证
    private void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) {
        String json = (String) obj;
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html; charset=utf-8");
        try {
            writer = httpServletResponse.getWriter();
            writer.print(json);

        } catch (IOException ex) {
            logger.error("response error", ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
