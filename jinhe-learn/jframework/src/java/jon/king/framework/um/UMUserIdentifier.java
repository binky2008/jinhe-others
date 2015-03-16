/* ==================================================================   
 * Created [2006-6-28 20:30:57] by jinpujun 
 * ==================================================================  
 * JFramework 
 * ================================================================== 
 * Copyright (c) jinpj, 2006-2007 
 * ================================================================== 
*/

package jon.king.framework.um;

import jon.king.framework.sso.BaseUserIdentifier;
import jon.king.framework.sso.IdentityCard;
import jon.king.framework.sso.PasswordPassport;

/** 
 * <p> DBUserIdentifier.java </p> 
 * 
 * @author 金普俊 2006-6-28
 * 
 * 数据库方式身份认证器
 */

public class UMUserIdentifier extends BaseUserIdentifier {

    protected IdentityCard validate(PasswordPassport passport) {
        PasswordPassport myPassport = (PasswordPassport) passport;
        String userName = myPassport.getUserName();
        //String password = myPassport.getPassword();
        
        // TODO 验证用户名/密码是否合法
//        if (userService.userLogin(userName, password))
        return new IdentityCard(new Long(0), userName, myPassport.getClientIp(), myPassport.getSessionId());
    }

    protected Object getOperator(Long id) {
        // TODO　根据id从数据库读取该用户记录
        return new Object();
    }

    public PasswordPassport getPassport() {
        return new PasswordPassport();
    }
}

