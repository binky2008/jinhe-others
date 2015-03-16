package jon.king.framework.sso;

/** 
 * <p>
 * 虚拟身份认证器：只要有用户账号、密码即可登录的虚拟身份认证器
 * </p>
 * 
 * @author 金普俊  2006-4-26
 *
 */
public class DemoUserIdentifier extends BaseUserIdentifier {

    protected IdentityCard validate(PasswordPassport passport) {
        PasswordPassport myPassport = (PasswordPassport) passport;
        String name = myPassport.getUserName();
        String password = myPassport.getPassword();
        if (name.equals("jinpj") && password.equals("121212")) {
            return new IdentityCard(new Long(12), name, myPassport.getClientIp(), myPassport.getSessionId());
        }        
        return null;       
    }

    protected Object getOperator(Long id) {
        return new Object();
    }

    public PasswordPassport getPassport() {
        return new PasswordPassport();
    }
}
