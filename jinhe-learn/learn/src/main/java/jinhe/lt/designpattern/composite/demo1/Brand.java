package jinhe.lt.designpattern.composite.demo1;

/** 
 * <p> Brand.java </p> 
 * 
 * @author Jon.King 2006-5-6
 * 
 * Brand类继承于SoftwareComposite类，对应于品牌，
 * 包含了品牌名属性，并且用来存储Product类的实例
 */

public class Brand extends SoftwareComposite {

    public SoftwareComponent findSoftwareComponentById(Long id) {
        return null;
    }

}

