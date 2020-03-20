package cn.demo.beike.config;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 var token= "LtSFVqKxvpS1nPARxS2lpUs2Q2IpGstidMrS8zMhNV3rT7RKnhLN6d2FFirkVEzVIeexgEHgI/PtnynGqjZlyGkJa4+zYIXxtDMoK/N+AB6wtsskYXereH3AR8kWErwIRvx+UOFveH3dgmdw1347SYjbL/ilGKX5xkoZCbfb1f0=,LZkg22zbNsUoHAgAUapeBn541X5OHUK7rLVNHsHWDM/BA4DCIP1f/3Bnu4GAElQU6cds/0fg9Li5cSPHe8pyhr1Ii/TNcUYxqHMf9bHyD6ugwOFTfvlmtp6RDopVrpG24RSjJbWy2kUOOjjk5uv6FUTmbrSTVoBEzAXYKZMM2m4=,R4QeD2psvrTr8tkBTjnnfUBw+YR4di+GToGjWYeR7qZk9hldUVLlZUsEEPWjtBpz+UURVmplIn5WM9Ge29ft5aS4oKDdPlIH8kWNIs9Y3r9TgH3MnSUTGrgayaNniY9Ji5wNZiZ9cE2CFzlxoyuZxOcSVfOxUw70ty0ukLVM/78=";
 var xhr = new XMLHttpRequest();
 xhr.open('GET', 'https://www.lideshuai.cn/beike/hello/get');
 xhr.setRequestHeader("x-access-token",token);
 xhr.send(null);
 xhr.onload = function(e) {
 var xhr = e.target;
 console.log(xhr.responseText);
 }


 */
@Component
public class CORSConfig implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}

