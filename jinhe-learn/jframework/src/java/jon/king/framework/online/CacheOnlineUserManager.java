package jon.king.framework.online;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jon.king.framework.excption.FrameworkException;

import org.apache.log4j.Logger;

/** 
 * Cache在线用户库: 用一个HashSet做为存放的容器，每个元素为一个OnlineUserInfo对象
 * 
 */
public class CacheOnlineUserManager implements IOnlineUserManager {
    private final transient Logger log = Logger.getLogger(CacheOnlineUserManager.class);
    
	private static Set<OnlineUserInfo> onlineUserSet = new HashSet<OnlineUserInfo>();
 
    public boolean isRegistered(String userId, String appCode, String clientIp, String sessionId) {
        OnlineUserInfo info = new OnlineUserInfo(userId, appCode, clientIp, sessionId);
        if(onlineUserSet.contains(info)){
            log.debug("用户 " + info.getUserId() + " 已经在在线用户库中");
            return true;
        }
        for( OnlineUserInfo temp : onlineUserSet ) {
            if(info.equalsIgnoreAppCode(temp)) {
                add(info);
                log.debug("用户 " + info.getUserId() + " 成功注册到线用户库中");
                return true;
            }
        }
        return false;
    }
 
	public void register(String userId, String appCode, String clientIp, String sessionId){
        OnlineUserInfo info = new OnlineUserInfo(userId, appCode, clientIp, sessionId);
        add(info);
        log.debug("用户 " + info.getUserId() + " 成功注册到线用户库中");
	}	
    
    private boolean add(OnlineUserInfo info){
        info.setLoginTime(new Date());
        return onlineUserSet.add(info);
    }
 
	public void delete(String appCode, String sessionId){
        for(OnlineUserInfo temp : onlineUserSet) {
            if(appCode.equals(temp.getAppCode()) && sessionId.equals(temp.getSessionId())){
                onlineUserSet.remove(temp);
                log.debug("用户 " + temp.getUserId() + " 从线用户库中中删除");
                break;
            }
        }
	}
 
    public void deleteAll(String sessionId) {
        for(Iterator<OnlineUserInfo> it = onlineUserSet.iterator(); it.hasNext();){
            OnlineUserInfo temp  = it.next();
            if(sessionId.equals(temp.getSessionId())){
                it.remove();
            }
        }
        log.debug("删除在线用户库中所有的用户");
    }
    
    class OnlineUserInfo {
        private String userId;    //用户编号
        private String appCode;   //应用code
        private String clientIp;  //客户端IP
        private String sessionId; //Session编号
        private Date   loginTime; //登陆时间

        /**
         * 根据用户信息创建对象
         * 
         * @param userId
         * @param appCode
         * @param clientIp
         * @param sessionId
         */
        public OnlineUserInfo(String userId, String appCode, String clientIp,
                String sessionId) {
            if(userId == null || appCode == null || clientIp == null || sessionId == null){
                throw new FrameworkException("有参数值为null，在线用户对象各个属性值要求非空！");
            }
            this.userId = userId;
            this.appCode = appCode;
            this.clientIp = clientIp;
            this.sessionId = sessionId;
        }

        public OnlineUserInfo(String userId, String appCode, String clientIp,
                String sessionId, Date loginTime) {
            this(userId, appCode, clientIp, sessionId);
            this.loginTime = loginTime;
        }
 
        public String getUserId() {
            return userId;
        }
 
        public String getAppCode() {
            return appCode;
        }
 
        public String getClientIp() {
            return clientIp;
        }
 
        public String getSessionId() {
            return sessionId;
        }
 
        public Date getLoginTime() {
            return loginTime;
        }
 
        public void setLoginTime(Date loginTime) {
            this.loginTime = loginTime;
        }

        public int hashCode() {
            int hash = 1;
            hash = hash * 31 + this.userId.hashCode();
            hash = hash * 31 + this.appCode.hashCode();
            hash = hash * 31 + this.sessionId.hashCode();
            hash = hash * 31 + this.clientIp.hashCode();

            return hash;
        }

        public boolean equals(Object obj) {
            if(obj == null){
                return false;
            }
            if (!(obj instanceof OnlineUserInfo)) {
                return false;
            }
            OnlineUserInfo temp = (OnlineUserInfo) obj;
            return temp.userId == null ? this.userId == null : temp.userId.equals(this.userId)
                    && temp.appCode == null ? this.appCode == null : temp.appCode.equals(this.appCode)
                    && temp.clientIp == null ? this.clientIp == null : temp.clientIp.equals(this.clientIp)
                    && temp.sessionId == null ? this.sessionId == null : temp.sessionId.equals(this.sessionId);
        }
        
        public boolean equalsIgnoreAppCode(OnlineUserInfo info) {
            return info.userId.equals(this.userId)
                    && info.clientIp.equals(this.clientIp)
                    && info.sessionId.equals(this.sessionId);
        }

    }
}


