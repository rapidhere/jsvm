package ranttu.rapid.jsvm.runtime;

/**
 * a javascript array Object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsObjectObject.java, v0.1 2017/4/14 dongwei.dq Exp $
 */
public class JsArrayObject extends JsObjectObject {
    @Override
    public Object getProperty(Object key) {
        if (key.equals("length")) {
            // TODO: to tricky
            // exclude __proto__
            return properties.size() - 1;
        } else {
            if (key instanceof Number) {
                return super.getProperty(((Number) key).intValue());
            } else {
                return super.getProperty(key);
            }
        }
    }
}
