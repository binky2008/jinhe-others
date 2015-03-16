package jon.king.framework.www.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import jon.king.framework.excption.FrameworkException;

import org.apache.log4j.Logger;

/**
 * 捕获异常并输出
 */
@WebFilter(filterName = "CatchExceptionFilter", urlPatterns = {"*.do", "*.in"})
public class CatchExceptionFilter implements Filter {
    private static Logger log = Logger.getLogger(CatchExceptionFilter.class);
 
    public void destroy() {
    }
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
        	log.info("------CatchExceptionFilter");
            chain.doFilter(request, response);
        } catch (FrameworkException be) {
            encodeException(response, be);
        }  catch (Throwable t) {
            encodeException(response, t);
        }
    }

    private void encodeException(ServletResponse response, Throwable be)  throws IOException {
        printErrorMessage(be);
        if (!response.isCommitted()) {
            response.reset();
        }
        response.setContentType("text/html;charset=gbk");
        PrintWriter writer = response.getWriter();
        writer.print(be.toString());
        writer.flush();
        writer.close();
    }

    /**
     * <p>
     * 打印详细错误信息到日志中
     * </p>
     * @param be
     */
    private void printErrorMessage(Throwable be) {
        printStackTrace(be);
        Throwable first = be;
        Throwable cause = be.getCause();
        while (cause != null) {
            first = cause;
            cause = cause.getCause();
        }
        if (first != be) {
            printStackTrace(first);
        }
    }

    /**
     * <p> 打印错误的堆栈信息到错误日志中 </p>
     * 
     * @param be
     */
    private void printStackTrace(Throwable be) {
        StringWriter writer = new StringWriter();
        be.printStackTrace(new PrintWriter(writer));
        log.error(writer.toString());
    }
 
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("异常处理服务初始化完成！");
    }
}
