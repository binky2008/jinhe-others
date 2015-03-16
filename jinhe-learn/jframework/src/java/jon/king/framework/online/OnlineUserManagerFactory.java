package jon.king.framework.online;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.excption.FrameworkException;

import org.apache.log4j.Logger;

public class OnlineUserManagerFactory {
	private static IOnlineUserManager manager = null;
	private static final Logger log = Logger.getLogger(OnlineUserManagerFactory.class);
	
	public static IOnlineUserManager instance(){
	    if(manager == null){
	        String className = ConfigurableContants.ONLINE_MANAGER_CLASS_NAME;
            if(className == null){
                return new CacheOnlineUserManager();
            }
            try {
                Class<?> clazz = Class.forName(className);
                manager = (IOnlineUserManager) clazz.newInstance();
            } catch (ClassNotFoundException e) {
                throw new FrameworkException("在线用户库管理类未找到", e);
            } catch (InstantiationException ie) {
                throw new FrameworkException("在线用户库管理类实例化失败", ie);
            } catch (IllegalAccessException iae) {
                throw new FrameworkException("非法操作：实例化在线用户管理类", iae);
            }
	        log.info("在线用户库初始化成功！");
	    }
	    return manager;
	}
}
