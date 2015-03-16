package jon.king.framework.sso;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.utils.StringUtils;

/** 
 * <P>
 * 用户身份证书对象，包含用户相关信息
 * </P>
 * 
 */
public class IdentityCard {
    private final static String charSet = "UTF-8";

    private Long id;  //用户编号
    private String name; //用户名称
    private String token; //令牌
    private String clientIp; //客户端IP
    private String sessionId; 
    private Object object; //信息

    /**
     * 根据用户信息创建身份对象，用于生成令牌
     * @param id
     * @param name
     * @param clientIp
     * @param sessionId
     */
    public IdentityCard(Long id, String name, String clientIp, String sessionId) {
        this.id = id;
        this.name = name;
        this.clientIp = clientIp;
        this.sessionId = sessionId;
    }

    /**
     * 根据令牌创建身份对象，用户解析令牌
     * @param token
     * @param clientIp
     * @param sessionId
     */
    public IdentityCard(String token, String clientIp, String sessionId) {
        this.token = token;
        this.clientIp = clientIp;
        this.sessionId = sessionId;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the object.
     */
    public Object getObject() {
        return object;
    }

    /**
     * @hibernate.property
     * @return Returns the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return Returns the clientIp.
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param token The token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @param object The object to set.
     */
    public void setObject(Object object) {
        this.object = object;
    }
    
    /**
     * <p>
     * 根据已有令牌、ip、sessionId校验合格后，生成新令牌
     * </p>
     * <p>
     * 解析令牌后，比对ip、sessionId，如果相同，则比对过期时间，如果还没有过期，则发放新令牌，否则返回Null。
     * </p>
     * <p>
     * 此处的SessionId为多应用系统平台统一SessionID
     * </p>
     * @return
     */
    public boolean recreate() {
        if (this != null) {
            if (token != null && token.length() > 0 
                    && clientIp != null && clientIp.length() > 0 
                    && sessionId != null && sessionId.length() > 0) {
                token = StringUtils.decodeString(token, charSet);               
                String[] contents = token.split(",");  //顺序：时间, ip, sessionId, userName, id
                if (contents.length == 5
                        && contents[1].equals(clientIp)
                        && contents[2].equals(sessionId)) {
                    this.setId(new Long(contents[4]));
                    this.setName(contents[3]);
                    long grantTime = Long.parseLong(contents[0]);
                    long nowTime = System.currentTimeMillis();
                    long lifecycleTime = Long.parseLong(ConfigurableContants.TOKEN_LIFECYCLE);

                    if (nowTime - grantTime <= lifecycleTime) {
                        token = nowTime + "," + contents[1] + "," + contents[2] + "," + contents[3] + "," + contents[4];
                        this.setToken(StringUtils.encodeString(token, charSet));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 根据用户账号、ip、sessionID创建令牌
     * </p>
     * @return
     */
    public boolean create() {
        if (this != null) {
            String token = System.currentTimeMillis() + ","
                    + this.clientIp + ","
                    + this.sessionId + ","
                    + this.name + "," 
                    + this.id;
            this.setToken(StringUtils.encodeString(token, charSet));
            return true;
        }
        return false;
    }

    /**
     * <p>
     * </p>
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        IdentityCard identityCard = new IdentityCard(new Long(0), "admin",
                "192.168.0.126", "123456789012qweqweqweqweqweqwewqeq");
        System.out.println(identityCard.getToken());
        identityCard.create();
        System.out.println(identityCard.getToken());
        Thread.sleep(1000);
        identityCard.recreate();
        System.out.println(identityCard.getToken());
    }
}
