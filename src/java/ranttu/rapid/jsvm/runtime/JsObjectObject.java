/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.runtime;

import ranttu.rapid.jsvm.common.$$;

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
        if(properties.containsKey(name)) {
            return properties.get(name);
        } else {
            JsObjectObject proto = $$.cast(properties.get("__proto__"));

            if(proto != null) {
                return proto.getProperty(name);
            }
        }

        return null;
    }

    public void setProperty(String name, Object val) {
        properties.put(name, val);
    }

    public Boolean instanceOf(Object obj) {
        Object prototype = $$.cast(obj, JsObjectObject.class).getProperty("prototype");

        if(prototype == null) {
            return false;
        }

        JsObjectObject curProto = $$.cast(getProperty("__proto__"));

        while(curProto != null) {
            if(curProto == prototype) {
                return true;
            }
            curProto = $$.cast(curProto.getProperty("__proto__"));
        }

        return false;
    }
}
