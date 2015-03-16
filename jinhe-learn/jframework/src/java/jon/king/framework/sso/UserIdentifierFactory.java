package jon.king.framework.sso;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.utils.BeanUtil;

public class UserIdentifierFactory {
    private static UserIdentifierFactory factory = null;

    private UserIdentifierFactory() {
    }

    /**
     * 获取认证类
     * @return
     */
    public IUserIdentifier getUserIdentifier() {
        IUserIdentifier identifier = (IUserIdentifier) BeanUtil.newInstanceByName(ConfigurableContants.USER_IDENTIFIER_CLASS_NAME);
        return identifier;
    }

    /**
     * 实例化认证类工厂本身
     * @return
     */
    public static UserIdentifierFactory instance() {
        if (factory == null) {
            factory = new UserIdentifierFactory();
        }
        return factory;
    }
}
