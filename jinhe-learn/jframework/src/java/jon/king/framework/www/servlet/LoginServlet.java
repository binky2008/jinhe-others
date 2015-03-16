package jon.king.framework.www.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.sso.IUserIdentifier;
import jon.king.framework.sso.IdentityCard;
import jon.king.framework.sso.PasswordPassport;
import jon.king.framework.sso.UserIdentifierFactory;

import org.apache.log4j.Logger;

/**
 * 获取userName和password，passwor4d在被送到管理容器中认证前（authentication）先加密（encrypt）
 * 
 * @author 金普俊  2006-6-21
 *
 */
@WebServlet(urlPatterns = "/login.in", loadOnStartup = 1)
public final class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 3906934490856239409L;

    private final transient Logger log = Logger.getLogger(LoginServlet.class);
 
    public void init() throws ServletException {       
    }
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userName = request.getParameter("userName");
        log.debug("开始验证用户： '" + userName + "'");
        
        IUserIdentifier userIdentifier = UserIdentifierFactory.instance().getUserIdentifier();
        PasswordPassport passport = userIdentifier.getPassport(); //从验证器中获取一个空passport
        passport.setClientIp(request.getRemoteAddr());
        passport.setSessionId(request.getSession().getId());
        
        IdentityCard card = null;
        if(passport.paserFromParameters(request)){ //将httpServletRequest中的参数值赋给passport（一般为用户名和密码）
            card = userIdentifier.identify(passport);
        }
        
        String redirectPage = "";
        if(card != null){
            log.debug("用户" + userName + " 登陆成功！");
            
            request.getSession().setAttribute(ConfigurableContants.IDENTIFY_CARD_NAME, card);
            response.addCookie(new Cookie(ConfigurableContants.TOKEN_NAME, card.getToken()));               
            redirectPage = request.getContextPath() + "/index.htm";
        }else{
            log.debug("登陆失败！");
            redirectPage = request.getContextPath() + "/pages/login.jsp";
        }
        response.sendRedirect(response.encodeRedirectURL(redirectPage));
    }
}
