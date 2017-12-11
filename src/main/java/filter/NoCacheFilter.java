package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class NoCacheFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResp = (HttpServletResponse) response;

        httpResp.setDateHeader("Expires", -1);
        httpResp.setHeader("Cache-Control", "no-cache");
        httpResp.setHeader("Pragma", "no-cache");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
