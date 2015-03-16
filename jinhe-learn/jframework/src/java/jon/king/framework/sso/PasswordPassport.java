package jon.king.framework.sso;

import javax.servlet.http.HttpServletRequest;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * <p>
 * 帐号密码式通行证
 * </p>
 * 
 * @author 金普俊  2006-6-28
 *
 */
public class PasswordPassport {
    private final transient Logger log = Logger.getLogger(PasswordPassport.class);

    private String userName;//用户账号 
    private String password;//用户密码 
    private String clientIp; //客户端ip
    private String sessionId;//Session编号
    
    public boolean paserFromHeader(HttpServletRequest request) {
        this.userName = request.getHeader(ConfigurableContants.LOGIN_USERNAME);
        this.password = request.getHeader(ConfigurableContants.LOGIN_PASSWORD);
        
        return check();
    }

    public boolean paserFromParameters(HttpServletRequest request) {
        this.userName = request.getParameter(ConfigurableContants.LOGIN_USERNAME);
        this.password = request.getParameter(ConfigurableContants.LOGIN_PASSWORD);
               
        return check();
    }
    
    private boolean check(){
        if (this.userName == null || this.password == null
                || "".equals(this.userName) || "".equals(this.password)) {
            log.error("用户名或密码不能为空");
            return false;
        }
        if (new Boolean(ConfigurableContants.PASSWORD_ENCRYPT).booleanValue()) {
            log.debug("对用户 '" + this.userName + "' 的密码进行加密。。。。。。");
            this.password = StringUtils.encodePassword(this.password, ConfigurableContants.PASSWORD_ENCRYPT_ALGORITHM);
        } 
        return true;
    }

    public String getPassword() {
        return password;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public String getClientIp() {
        return clientIp;
    }
 
    public String getSessionId() {
        return sessionId;
    }
 
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
 
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
