package com.viettel.web.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * @author nhannt34
 *         Xu ly loi committed cua Flash scope khong reset du lieu sau khi chuyen trang xong
 */
public class FixedBufferSizeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
//        response.getWriter(); TAM THOI COMMENT DE SUA LOI getWriter() luc download
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
//        wrapper.setBufferSize(10000000);
        chain.doFilter(request, wrapper);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
