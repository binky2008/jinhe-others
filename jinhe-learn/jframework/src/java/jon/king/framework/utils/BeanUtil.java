package jon.king.framework.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import jon.king.framework.excption.FrameworkException;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtil {

    /**
     * 根据对象的class名，创建相应的Class对象
     * 
     * @param className
     * @return
     */
    public static Class<?> createClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new FrameworkException("实体: " + className + " 无法加载", e);
        }
    }

    /**
     * 根据对象的class名，创建相应的对象
     * 
     * @param className
     * @return
     */
    public static Object newInstanceByName(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw new FrameworkException("实例化失败：" + className, e);
        } catch (IllegalAccessException e) {
            throw new FrameworkException("没有合适的构造函数，实例化失败：" + className, e);
        } catch (ClassNotFoundException e) {
            throw new FrameworkException("类文件无法找到，实例化失败：" + className, e);
        }
    }

    /**
     * <p>
     * 通过带参数的构造函数实例化对象
     * </p>
     * 
     * @param className
     *            String
     * @param clazzes
     *            Class[]
     * @param args
     *            Object[]
     * @return Object
     */
    public static Object newInstanceByName(String className, Class<?>[] clazzes, Object[] args) {
        Class<?> clazz = createClassByName(className);
        try {
            return clazz.getConstructor(clazzes).newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new FrameworkException("非法参数类型，实例化失败：" + className, e);
        } catch (SecurityException e) {
            throw new FrameworkException("安全性限制，实例化失败：" + className, e);
        } catch (InstantiationException e) {
            throw new FrameworkException("实例化失败：" + className, e);
        } catch (IllegalAccessException e) {
            throw new FrameworkException("没有相应的构造函数，实例化失败：" + className, e);
        } catch (InvocationTargetException e) {
            throw new FrameworkException("非法调用，实例化失败：" + className, e);
        } catch (NoSuchMethodException e) {
            throw new FrameworkException("没有相应的构造函数，实例化失败：" + className, e);
        }
    }

    /**
     * 根据Class对象创建Object对象
     * 
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new FrameworkException("实例化失败：" + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new FrameworkException("实例化失败：" + clazz.getName());
        }
    }

    /**
     * 判断属性是否在实体中
     * 
     * @param bean
     * @param propertyName
     * @return
     */
    public static boolean isPropertyInBean(Object bean, String propertyName) {
        PropertyDescriptor[] descr = PropertyUtils.getPropertyDescriptors(bean);
        for (int i = 0; i < descr.length; i++) {
            PropertyDescriptor d = descr[i];
            if (propertyName.equals(d.getName())) {
                return true;
            }
        }
        return false;
    }

 }

