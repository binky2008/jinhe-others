package jon.king.framework.www.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(urlPatterns = "/logout.in", loadOnStartup = 1)
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 807941967787932751L;
    
    private final transient Logger log = Logger.getLogger(LogoutServlet.class);
 
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
        doPost(arg0, arg1);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getSession().invalidate();
        log.info("注销成功");
        
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter(); 
        out.println("<Script>alert('注销成功');window.location='"+request.getContextPath() + "/pages/login.jsp';</Script>");
        out.flush();
        out.close();
    }
}
