package jon.king.framework.www.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.excption.FrameworkException;
import jon.king.framework.sso.IUserIdentifier;
import jon.king.framework.sso.IdentityCard;
import jon.king.framework.sso.PasswordPassport;
import jon.king.framework.sso.UserIdentifierFactory;

import org.apache.log4j.Logger;

/**
 * <pre>
 * 自动登录: 
 *      if(Session中已有用户信息){通过
 *      }else if(cookie中有令牌 && 令牌合法){       通过
 *      }else if(header中有令牌 && 令牌合法){       通过
 *      }else if(parameters中有令牌 && 令牌合法{    通过
 *      }else if(header中有账号密码 && 账号密码合法){       通过
 *      }else if(parameters中有账号密码 && 账号密码合法){   通过
 *      }else{
 *          返回尚未登录或超时异常
 *      }
 *      
 * 令牌校验与更新：
 *      由UserIdentifier统一发放令牌和校验身份合法性
 *      校验通过后，返回身份对象，否则返回null
 *      从身份对象中获取新的令牌，更新令牌
 * </pre>
 * 
 */
@WebFilter(filterName = "AutoLoginFilter", urlPatterns = "*.do")
public class AutoLoginFilter implements Filter {
    private final transient Logger log = Logger.getLogger(AutoLoginFilter.class);
    
    private IUserIdentifier userIdentifier;
 
    public void init(FilterConfig filterConfig) throws ServletException {
        userIdentifier = UserIdentifierFactory.instance().getUserIdentifier(); 
        log.info("自动登录Filter初始化完成！");
    }

    public void destroy() { }
 
    private IdentityCard autoLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException{
       try{
            String clientIp = req.getRemoteAddr();
            IdentityCard card = null;                                            
//          *********************************** 取令牌Token *******************************************  
            String[] tokens = new String[3];
            //tokens[0] = ServletUtil.getCookie(req, Constants.TOKEN_NAME).getValue();                  
            tokens[1] = req.getHeader(ConfigurableContants.TOKEN_NAME);    
            tokens[2] = req.getParameter(ConfigurableContants.TOKEN_NAME);
            for(int i = 0; i < tokens.length; i++){
                String token =  tokens[i];
                if (token != null) 
                    if((card = userIdentifier.identify(token, clientIp, req.getSession().getId())) != null)
                        return card;
            }            
//          *********************************** 取Passport *******************************************  
            PasswordPassport passport = userIdentifier.getPassport();
            if(passport.paserFromHeader(req) && (card = userIdentifier.identify(passport)) != null){
                return card;
            }
            if(passport.paserFromParameters(req) && (card = userIdentifier.identify(passport)) != null){
                return card;
            }
            return null;
        } catch (Exception e) {
            log.error("自动登陆时出错");
            throw new FrameworkException("自动登陆时出错", e);
        }       
    }
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	
    	log.info("------AutoLoginFilter");
    	
        HttpServletRequest req = (HttpServletRequest) request;     
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        
        IdentityCard card = (IdentityCard) session.getAttribute(ConfigurableContants.IDENTIFY_CARD_NAME);
        if(card == null){
            card = autoLogin(req, res);
            if(card == null){
                log.error("登陆出错或超时");
                reLogin(req, res);
            }else{
                log.debug("自动登陆成功！");
                session.setAttribute(ConfigurableContants.IDENTIFY_CARD_NAME, card);
                res.addCookie(new Cookie(ConfigurableContants.TOKEN_NAME, card.getToken()));           
            }       
        }                              
        chain.doFilter(request, response);
    }
    
    private void reLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=GBK");
        PrintWriter pw = response.getWriter();
        pw.println("<Script>alert('尚未登录或操作已超过30分钟，请重新登录');window.location='"+request.getContextPath() + "/pages/login.jsp';</Script>");
        pw.flush();
        pw.close();
    }
}
