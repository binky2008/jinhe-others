package jon.king.framework.sso;

/** 
 * <p>
 * 身份鉴定器接口类
 * </p>
 * 
 */
public interface IUserIdentifier {

    /**
     * 根据用户通行证识别身份，如果合法返回用户身份信息，否则返回Null
     * @param passport
     * @return
     */
    IdentityCard identify(PasswordPassport passport);

    /**
     * <p>
     * 判断令牌是否合法，如果合法返回用户身份信息，否则返回Null
     * </p>
     * @param token
     * @param clientIp
     * @param sessionId
     * @return
     */
    IdentityCard identify(String token, String clientIp, String sessionId);
    
    /**
     * <p>
     * 获取空Passport对象
     * </p>
     * @return
     */
    PasswordPassport getPassport();

}