/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2017 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.codegen.ir;

import com.google.common.collect.ImmutableList;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import ranttu.rapid.jsvm.common.$$;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rapidhere@gmail.com
 * @version $id: IrLabel.java, v0.1 2017/5/8 dongwei.dq Exp $
 */
public class IrLabel extends IrNode {
    public LabelNode label;

    // ~~~ for frames
    public boolean useFull = false;

    // we only store stacks, no locals
    public List<Type> extraLocals = new ArrayList<>();

    // stacks of frame map
    public List<Type> extraStacks = new ArrayList<>();

    public boolean needFrame = false;

    public static IrLabel label() {
        IrLabel irLabel = new IrLabel();
        irLabel.label = new LabelNode();

        return irLabel;
    }

    public IrLabel frame() {
        needFrame = true;
        return this;
    }

    public IrLabel full() {
        useFull = true;
        return frame();
    }

    public IrLabel exStack(Object...extraStacks) {
        this.extraStacks = ImmutableList.copyOf(resolve(extraStacks));
        return frame();
    }

    private Type[] resolve(Object...os) {
        Type[] arr = new Type[os.length];
        for(int i = 0;i < arr.length;i ++) {
            arr[i] = $$.getType(os[i]);
        }

        return arr;
    }
}
