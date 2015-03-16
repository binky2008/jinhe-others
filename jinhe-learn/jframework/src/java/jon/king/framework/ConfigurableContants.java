package jon.king.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/** 
 * JFramework系统级静态常量.
 * 可通过JFramework.properties初始化,同时保持常量static & final的特征.
 */
public class ConfigurableContants {
    
private static final Logger logger = Logger.getLogger(ConfigurableContants.class);
    
    protected static Properties p = new Properties();

    protected static void init(String propertyFileName) {
        InputStream in = null;
        try {
            in = ClassLoader.getSystemResourceAsStream(propertyFileName);
            if (in != null) p.load(in);
        } catch (IOException e) {
            logger.error("load " + propertyFileName + " into Contants error");
        } finally {
            if (in != null) {
                try { in.close();
                } catch (IOException e) { }
            }
        }
    }

    protected static String getProperty(String key, String defaultValue) {
        return p.getProperty(key, defaultValue);
    }
    
    protected static String getProperty(String key) {
        return p.getProperty(key);
    }

    static {
        init("JFramework.properties");
    }
    
    public final static String DEFAULT_DATE_PATTERN = getProperty("constant.default_date_pattern", "yyyy-MM-dd hh:mm:ss");

    public final static String MESSAGE_BUNDLE_KEY = getProperty("constant.message_bundle_key", "messages");
    public final static String ERROR_BUNDLE_KEY = getProperty("constant.error_bundle_key", "errors");
    
    /**
     * 系统分页默认页记录数
     */
    public final static int DEFAULT_PAGE_SIZE = Integer.parseInt(getProperty("constant.default_page_size", String.valueOf(12)));
    
    /**
     * 在线用户库管理类
     */
    public static final String ONLINE_MANAGER_CLASS_NAME = getProperty("constant.online_manager_className", "jon.king.framework.online.CacheOnlineUserManager");
   
    /**
     * 用户验证器
     */
    public static final String USER_IDENTIFIER_CLASS_NAME = getProperty("constant.user_identifier_className", "jon.king.framework.sso.DemoUserIdentifier");;
    /**
     * 当前应用Code
     */
    public static final String APPLICATION_CODE = getProperty("constant.application_code", "JFramework");
    
    /**
     * 令牌的生命周期，默认为半小时
     */
    public static final String TOKEN_LIFECYCLE = getProperty("constant.token_lifecycle", "1800000");
    
    /**
     * 登陆用户名和密码标记
     */
    public static final String LOGIN_USERNAME = "userName";
    public static final String LOGIN_PASSWORD = "password";
    
    public static final String IDENTIFY_CARD_NAME = "idenify.card";
    public static final String TOKEN_NAME = "token";
    
    /**
     * 密码是否加密和密码加密方式
     */
    public static final String PASSWORD_ENCRYPT = getProperty("constant.password_encrypt", "false");
    public static final String PASSWORD_ENCRYPT_ALGORITHM = getProperty("constant.password_encrypt_algorithm", "SHA");
   
    
    public static void main(String[] args){
        System.out.println(ConfigurableContants.DEFAULT_PAGE_SIZE);
        System.out.println(ConfigurableContants.getProperty("constant.default_page_size"));
    }
}


