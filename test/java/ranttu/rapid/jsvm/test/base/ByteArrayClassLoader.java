/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.test.base;

import ranttu.rapid.jsvm.common.$$;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rapidhere@gmail.com
 * @version $id: ByteArrayClassLoader.java, v0.1 2017/4/8 dongwei.dq Exp $
 */
public class ByteArrayClassLoader extends ClassLoader {
    private Map<String, byte[]> extraClassDefs = new HashMap<>();

    public <T> Class<? extends T> loadClass(String clsName, byte[] bytes) {
        extraClassDefs.put(clsName, bytes);

        try {
            return findClass(clsName);
        } catch (Exception e) {
            return $$.shouldNotReach(e);
        }
    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        if (extraClassDefs.containsKey(name)) {
            byte[] bytes = extraClassDefs.get(name);

            return defineClass(name, bytes, 0, bytes.length);
        } else {
            throw new ClassNotFoundException(name);
        }
    }
}
