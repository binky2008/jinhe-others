package jon.king.framework.online;

/** 
 * 在线用户管理类接口
 * 
 */
public interface IOnlineUserManager {

    /**
     * <p>
     * 判断是否已登录其他系统，如果登录则注册用户登录此系统
     * </p>
     * <p>
     * 根据用户ID、应用Code、客户端IP、sessionID查询在线用户，
     * 如果在线用户库中有同userID,clientIp,sessionId的用户存在，则表示用户已登录；
     * 否则表示用户没有登录，返回false。
     * </p>
     * <p>
     * 如果用户已登录，同时没有一条userId, appCode, clientIp, sessionId都相同的用户存在，
     * 则在在线用户库中添加此记录，并返回true；
     * 否则直接返回true。
     * </p>
     * 
     * @param userId        用户id
     * @param appCode       应用Code
     * @param clientIp      客户端IP
     * @param sessionId     SessionId
     */
    public boolean isRegistered(String userId, String appCode, String clientIp,
            String sessionId);
    
    /**
     * <p>
     * 注册并创建在线用户
     * </p>
     * <p>
     * 如果在线用户库中没有一条userId, appCode, clientIp, sessionId都相同的用户存在，
     * 则在在线用户库中添加此记录
     * </p>
     * 
     * @param userId        用户id
     * @param appCode       应用Code
     * @param clientIp      客户端IP
     * @param sessionId     SessionId
     */
    public void register(String userId, String appCode, String clientIp,
            String sessionId);
    

    /**
     * <p>
     * 删除在线用户或访问应用：根据应用Code，SessionId删除相应的记录
     * </p>
     * 
     * @param appCode 
     * @param sessionId
     */
    public void delete(String appCode, String sessionId);
    
    /**
     * <p>
     * 注销所有系统：从在线用户中删除相应sessionId的所有用户
     * </p>
     * @param sessionId
     */
    public void deleteAll(String sessionId);
}
