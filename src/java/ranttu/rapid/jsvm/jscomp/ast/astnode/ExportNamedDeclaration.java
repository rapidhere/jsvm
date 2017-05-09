package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Declaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Statement;

/**
 * a export statement
 *
 * @author rapidhere@gmail.com
 * @version $id: EmptyStatement.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ExportNamedDeclaration extends BaseAstNode implements Statement {
    private Declaration declaration;

    public ExportNamedDeclaration(JSONObject jsonObject) {
        super(jsonObject);

        // only export declaration is supported now
        declaration = Node.of(this, jsonObject, "declaration");
    }

    public Declaration getDeclaration() {
        return declaration;
    }
}
