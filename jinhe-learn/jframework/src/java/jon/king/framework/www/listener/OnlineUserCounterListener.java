package jon.king.framework.www.listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import jon.king.framework.ConfigurableContants;
import jon.king.framework.online.OnlineUserManagerFactory;

import org.apache.log4j.Logger;


/**
 * OnlineUserCounterListener class used to count the current number of active users for the applications.  
 * Does this by counting how many user objects are stuffed into the session.  It Also grabs
 * these users and exposes them in the servlet context.
 *
 * HttpSessionListener: 
 * 这个监听器主要监听一个Session对象被生成和销毁时发生的事件.对应有两个方法: 
 *   public void sessionCreated(HttpSessionEvent se) 
 *   public void sessionDestroyed(HttpSessionEvent se) 
 *   
 * HttpSessionAttributeListener: 
 * 和ServletContextAttributeListener一样
 * 它监听一个session对象的Attribut被Add(一个特定名称的Attribute每一次被设置),replace(已有名称的Attribute的值被重设)和remove时的事件. 
 * 对应的有三个方法. 
 *   public void attributeAdded(HttpSessionBindingEvent se) 
 *   public void attributeRemoved(HttpSessionBindingEvent se) 
 *   public void attributeReplaced(HttpSessionBindingEvent se) 
 *   
 * @author 金普俊  2006-6-23
 *
 */
@WebListener
public class OnlineUserCounterListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    
    private final transient Logger log = Logger.getLogger(OnlineUserCounterListener.class);
    
    public static final String COUNT_KEY = "userCounter";
    public static final String USER_CARDS_KEY = "user.cards";
       
    private transient ServletContext servletContext;   //transient 阻止对象的序列化，不准备写入ObjectStream
    private int counter;
    private Set<Object> cards;

    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    public synchronized void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
        cards = null;
        counter = 0;
    }
    
    public void sessionCreated(HttpSessionEvent arg0) {
        log.debug("session created!");
    }

    /* 
     * Session超时监听程序：相关Session超时时，注销在线用户库中对应记录信息
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        log.debug("session destroyed!");
        
        //删除用户在在线用户库中注册信息
        String sessionId = event.getSession().getId();
        OnlineUserManagerFactory.instance().delete(ConfigurableContants.APPLICATION_CODE, sessionId);
        
        removeUserCard(event.getSession().getAttribute(ConfigurableContants.IDENTIFY_CARD_NAME)); 
    }
    
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(ConfigurableContants.IDENTIFY_CARD_NAME)) {
            addUserCard(event.getValue());
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals(ConfigurableContants.IDENTIFY_CARD_NAME)) {
            removeUserCard(event.getValue());
        }
    }
    
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // I don't really care if the user changes their information
    }

    synchronized void incrementUserCounter() {
        counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));

        log.debug("User Count: " + counter);
    }

    synchronized void decrementUserCounter() {
        int counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;
        if (counter < 0) {
            counter = 0;
        }
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
        
        log.debug("User Count: " + counter);
    }

    @SuppressWarnings("unchecked")
    synchronized void addUserCard(Object card) {
        cards = (Set<Object>) servletContext.getAttribute(USER_CARDS_KEY);
        if (cards == null) {
            cards = new HashSet<Object>();
        }        
        if (cards.contains(card)) {
            log.info("User already logged in, adding anyway...");
        }
        cards.add(card);
        servletContext.setAttribute(USER_CARDS_KEY, cards);
        
        incrementUserCounter();
    }

    @SuppressWarnings("unchecked")
	synchronized void removeUserCard(Object card) {
        cards = (Set<Object>) servletContext.getAttribute(USER_CARDS_KEY);
        if (cards != null) {
            cards.remove(card);
        }
        servletContext.setAttribute(USER_CARDS_KEY, cards);
        
        decrementUserCounter();
    }
}
