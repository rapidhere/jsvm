package ranttu.rapid.jsvm.jscomp.ast.astnode;

import org.json.JSONObject;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Declaration;
import ranttu.rapid.jsvm.jscomp.ast.asttype.Node;

/**
 * @author rapidhere@gmail.com
 * @version $id: ImportDeclaration.java, v0.1 2016/12/9 dongwei.dq Exp $
 */
public class ImportDeclaration extends BaseAstNode implements Declaration {
    private Identifier namespace;
    private Literal source;

    public ImportDeclaration(JSONObject jsonObject) {
        super(jsonObject);
        source = Node.of(this, jsonObject, "source");

        JSONObject specifier = (JSONObject) jsonObject.getJSONArray("specifiers").get(0);
        namespace = Node.of(this, specifier, "local");
    }

    public Identifier getNamespace() {
        return namespace;
    }

    public Literal getSource() {
        return source;
    }
}
