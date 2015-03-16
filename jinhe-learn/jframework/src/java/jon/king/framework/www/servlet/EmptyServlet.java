package jon.king.framework.www.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 空servlet，啥事都不做。
 * 作用：在WebSphere6.1等版本应用服务器中，应用间请求转发时，
 * 假设从应用A转发一个请求到应用B，如果请求地址在A中不存在，那么及时通过filter拦截转发后取回应用B的数据，
 * 应用A中还是会在Response相应里加入一个 404 文件找不到 的错误。
 * 
 * 通过将本servlet配置成 /*.in，/*.do（配置servlet最后位置），以将处理找不到真实地址的请求。
 * 
 */
@WebServlet(urlPatterns = {"*.do", "*.in"}, loadOnStartup = 3 )
public class EmptyServlet extends HttpServlet {

    private static final long serialVersionUID = 5470879889942418562L;

    Logger log = Logger.getLogger(this.getClass());
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        log.debug("请求：" + request.getRequestURI() + " 被 dispatche 到 EmptyServlet。");
        // do nothing
    }
}
