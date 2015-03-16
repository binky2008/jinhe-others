package jon.king.framework.sso;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.online.OnlineUserManagerFactory;

/** 
 * 身份认证器超类
 */
public abstract class BaseUserIdentifier implements IUserIdentifier {

    public IdentityCard identify(PasswordPassport passport) {
        IdentityCard card = validate(passport);
        if(card == null){
            return null;
        }
        boolean isLegal = card.create();
        //如果合法，注册在线用户，并获取相关用户信息对象，放入身份对象
        if (isLegal) { //注册在线用户库
            String appCode = ConfigurableContants.APPLICATION_CODE;
            OnlineUserManagerFactory.instance().register(card.getName(), appCode, card.getClientIp(), card.getSessionId());
            card.setObject(getOperator(card.getId()));
            return card;
        }
        return null;
    }

    public IdentityCard identify(String token, String clientIp, String sessionId) {
        IdentityCard card = new IdentityCard(token, clientIp, sessionId);

        boolean isLegal = card.recreate();
        if (!isLegal) {//如果card不合法
            //判断用户是否已登录其他系统，如果登录则注册用户登录当前系统。（类似于FOA用款计划整合时，系统间调用只需传递一个token就行）
            String appCode = ConfigurableContants.APPLICATION_CODE;
            boolean registered = OnlineUserManagerFactory.instance()
                    .isRegistered(card.getName(), appCode, card.getClientIp(), card.getSessionId());
            if (!registered) {
                return null;
            }
        }        
        card.setObject(getOperator(card.getId()));
        return card;
    }

    /**
     * <p>
     * 获取操作这相关信息对象
     * </p>
     * @param Id
     * @return
     */
    protected abstract Object getOperator(Long Id);

    /**
     * <p>
     * 验证Passport通行证是否合法，如果合法，返回相应的IdentityCard对象，否则返回Null
     * </p>
     * @param passport
     * @return
     */
    protected abstract IdentityCard validate(PasswordPassport passport);
}
