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
 * WTF: in javascript, every object is throwable
 *
 * @author rapidhere@gmail.com
 * @version $id: JsObjectObject.java, v0.1 2017/4/14 dongwei.dq Exp $
 */
public class JsObjectObject extends Throwable {
    protected Map<String, Object> properties = new HashMap<>();

    public JsObjectObject() {
    }

    // only for throwable usage
    public JsObjectObject(String message) {
        super(message);
    }

    public Object getProperty(Object name0) {
        String name = name0.toString();
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

    public void setProperty(Object name, Object val) {
        properties.put(name.toString(), val);
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
