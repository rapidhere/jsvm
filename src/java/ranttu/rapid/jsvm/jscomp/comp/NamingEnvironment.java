/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.exp.DuplicateName;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * the naming environment
 *
 * @author rapidhere@gmail.com
 * @version $id: NamingEnvironment.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class NamingEnvironment {
    private Stack<Set<String>> scopeStack = new Stack<>();

    /**
     * enter a new scope
     */
    public void enter() {
        scopeStack.push(new HashSet<>());
    }

    /**
     * leave a scope
     */
    public void leave() {
        scopeStack.pop();
    }

    /**
     * add a binding
     * @param id the binding id
     */
    public void addBinding(String id) {
        if (!currentHasBinding(id)) {
            scopeStack.peek().add(id);
        } else {
            // TODO
            throw new DuplicateName(id);
        }
    }

    /**
     * test if current binding have the id
     * @param id the binding id
     */
    public boolean currentHasBinding(String id) {
        return !scopeStack.empty() && scopeStack.peek().contains(id);
    }
}
