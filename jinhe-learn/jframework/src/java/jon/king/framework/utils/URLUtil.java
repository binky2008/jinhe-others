package jon.king.framework.utils;

import java.net.URL;

import org.apache.log4j.helpers.Loader;

/**
 * @author 沈超奇
 * 
 * 相对路径转换成绝对路径
 *  
 */
public class URLUtil {
	/**
	 * 获取资源文件的绝对路径
	 * @param file
	 * @return
	 */
	public static URL getResourceFileUrl(String file) {
		URL url = Loader.getResource(file);
		if (url == null) {
			url = ClassLoader.class.getResource(file);
		}
		return url;
	}

	public static void main(String[] args) {
		URL url = getResourceFileUrl("JFramework.properties");
		System.out.println(url);
	}

}
