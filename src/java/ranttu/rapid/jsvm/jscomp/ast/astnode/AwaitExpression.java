package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Expression;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * a await expression
 *
 * @author rapidhere@gmail.com
 * @version $id: Function.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class AwaitExpression extends BaseAstNode implements Expression {
    private Expression argument;

    public AwaitExpression(JSONObject jsonObject) {
        super(jsonObject);
        argument = Node.of(this, jsonObject, "argument");
    }

    public Expression getArgument() {
        return argument;
    }
}
