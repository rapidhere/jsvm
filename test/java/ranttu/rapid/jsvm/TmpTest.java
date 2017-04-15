/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm;

import java.util.HashMap;
import java.util.Map;

/**
 * ONLY FOR OUTLINE-USAGE
 *
 * @author rapidhere@gmail.com
 * @version $id: TmpTest.java, v0.1 2017/4/13 dongwei.dq Exp $
 */
public class TmpTest {
    Map<String, Integer> map = new HashMap<>();

    TmpTest() {
        Object o = 2;
        map.put("key", (Integer) o);
    }
}
