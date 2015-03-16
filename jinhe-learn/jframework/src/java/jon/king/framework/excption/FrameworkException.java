/* ==================================================================   
 * Created [2007-3-19 10:07:22] by 金普俊 
 * ==================================================================  
 * JFramework1.0 
 * ================================================================== 
 * Copyright (c) jinpj, 2007-2008 
 * ================================================================== 
*/
package jon.king.framework.excption;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jon.king.framework.ConfigurableContants;


/** 
 * 异常基类.
 * 带有错误代码与错误信息.
 * 
 */
public class FrameworkException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误代码,默认为未知错误
     */
    private String errorCode = "UNKNOW_ERROR";

    /**
     * 错误信息中的参数
     */
    protected String[] errorArgs = null;

    /**
     * 错误信息
     */
    private String errorMessage = null;

    public String getErrorCode() {
        return errorCode;
    }

    public FrameworkException(String errorCode, String[] errorArgs) {
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
    }

    public FrameworkException(String errorCode, String[] errorArgs, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
    }

    public FrameworkException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public FrameworkException(String errorMessage, Throwable t) {
        super(errorMessage, t);
        this.errorMessage = errorMessage;
    }


    /**
     * 错误信息I18N的ResourceBundle.
     * 默认Properties文件名定义于Constants的ERROR_BUNDLE_KEY
     */
    static private ResourceBundle rb = ResourceBundle
            .getBundle(ConfigurableContants.ERROR_BUNDLE_KEY, Locale.getDefault());
    
    /**
     * 获得出错信息.
     * 读取i18N properties文件中Error Code对应的message,并组合参数获得i18n的出错信息.
     */
    public String getMessage() {
        if (errorMessage != null) 
            return errorMessage;

        String message;
        try {
            message = rb.getString(errorCode);
        }catch (MissingResourceException mse) {
            message = "Can't get the message of the Error Code";
        }
        message = MessageFormat.format(message, (Object[]) errorArgs);

        return errorCode + ": " + message;
    }
}

