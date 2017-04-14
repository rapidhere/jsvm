/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * a javascript Object
 *
 * @author rapidhere@gmail.com
 * @version $id: JsObjectObject.java, v0.1 2017/4/14 dongwei.dq Exp $
 */
public class JsObjectObject {
    protected Map<String, Object> properties = new HashMap<>();

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public Object setProperty(String name, Object val) {
        properties.put(name, val);
        return val;
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }
}
