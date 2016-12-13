/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.comp;

import ranttu.rapid.jsvm.exp.CompileError;
import ranttu.rapid.jsvm.exp.DuplicateName;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * the naming environment
 *
 * @author rapidhere@gmail.com
 * @version $id: NamingEnvironment.java, v0.1 2016/12/11 dongwei.dq Exp $
 */
public class NamingEnvironment {
    private Map<Node, Map<String, Name>> scopes = new HashMap<>();

    /**
     * add a binding to the last scope
     * @param name the binding name
     */
    public void addBinding(Node node, Name name) {
        Map<String, Name> scope;
        Node origNode = node;

        // find last scope
        while(true) {
            if ((scope = scopes.get(node)) != null) {
                break;
            }

            if(node.hasParent()) {
                node = node.getParent();
            } else {
                break;
            }
        }

        // no scope exist, this should never happen
        if(scope == null) {
            throw new CompileError(origNode, "cannot find a scope");
        }

        if (! scope.containsKey(name.getId())) {
            scope.put(name.getId(), name);
        } else {
            // TODO
            throw new DuplicateName(node, name.getId());
        }
    }
}
