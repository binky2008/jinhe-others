package jon.king.framework.www.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebFilter(filterName = "EncodingFilter", urlPatterns = "/*")
public class SetCharacterEncodingFilter implements Filter {

	protected String encoding = null;

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * Should a character encoding specified by the client be ignored?
	 */
	protected boolean ignore = true;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
		
		log.info("------SetCharacterEncodingFilter");
		
		if (ignore || (request.getCharacterEncoding() == null)) {
			if (this.encoding != null)
				request.setCharacterEncoding(this.encoding);
		}
		HttpServletResponse hsr = (HttpServletResponse) response;
		hsr.setHeader("Cache-Control", "No-Cache");
		
		chain.doFilter(request, response); // Pass control on to the next filter
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			this.ignore = true;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
		
		log.info("设置编码的Filter初始化完成！");
	}
	
	private static Logger log = Logger.getLogger(SetCharacterEncodingFilter.class);
	
}
