/**
 * rapidhere@gmail.com
 * Copyright (c) 1995-2016 All Rights Reserved.
 * ===> GLORY TO THE FIRST BORN! <===
 */
package ranttu.rapid.jsvm.jscomp.ast.asttype;

import org.json.JSONException;
import org.json.JSONObject;
import ranttu.rapid.jsvm.common.$$;
import ranttu.rapid.jsvm.exp.ESTreeLoadFailed;
import ranttu.rapid.jsvm.jscomp.ast.Location;
import ranttu.rapid.jsvm.jscomp.ast.astnode.*;

import javax.annotation.Nullable;
import java.util.Optional;

import static ranttu.rapid.jsvm.common.$$.cast;

/**
 * Abstract Syntax Node according to es tree standard
 *
 * @author rapidhere@gmail.com
 * @version $id: Node.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public interface Node {
    /**
     * get the start location of the abstract node
     */
    Location getStartLocation();

    /**
     * get the end location of the abstract node
     */
    Location getEndLocation();

    /**
     * get the parent of the node
     *
     * if has no parent, return the node itself
     */
    Node getParent();

    /**
     * indicate if node has parent
     */
    boolean hasParent();

    /**
     * get a ast tree from es-tree node's child, which can be null
     *
     * @param parent parent node
     * @param jsonObject es-tree node
     * @param key child key
     * @return optional
     */
    static <T extends Node> Optional<T> ofNullable(Node parent, JSONObject jsonObject, String key) {
        if (jsonObject.isNull(key)) {
            return Optional.empty();
        }

        return Optional.of(of(parent, jsonObject, key));
    }

    /**
     * get a ast tree from es-tree node's child
     *
     * @param parent parent node
     * @param jsonObject es-tree node
     * @param key child key
     * @return ast tree
     */
    static <T extends Node> T of(Node parent, JSONObject jsonObject, String key) {
        return of(parent, jsonObject.getJSONObject(key));
    }

    /**
     * get a ast tree from es-tree json object
     *
     * @param parent parent node, if parent is null, indicate has no parent
     * @param jsonObject es-tree json object
     * @return the node
     */
    static <T extends Node> T of(@Nullable Node parent, JSONObject jsonObject) {
        BaseAstNode ret;

        try {
            String type = jsonObject.getString("type");

            switch (type) {
                case ES_AWAIT_EXPRESSION:
                    ret = new AwaitExpression(jsonObject);
                    break;
                case ES_EXPORT_NAMED_DECLARATION:
                    ret = new ExportNamedDeclaration(jsonObject);
                    break;
                case ES_TYPE_PROGRAM:
                    ret = new Program(jsonObject);
                    break;
                case ES_TYPE_IDENTIFIER:
                    ret = new Identifier(jsonObject);
                    break;
                case ES_TYPE_VAR_DEC:
                    ret = new VariableDeclaration(jsonObject);
                    break;
                case ES_TYPE_VAR_DECT:
                    ret = new VariableDeclarator(jsonObject);
                    break;
                case ES_TYPE_LITERAL:
                    ret = Literal.of(jsonObject);
                    break;
                case ES_TYPE_EXP_STM:
                    ret = new ExpressionStatement(jsonObject);
                    break;
                case ES_TYPE_BLOCK_STM:
                    ret = new BlockStatement(jsonObject);
                    break;
                case ES_TYPE_EMPTY_STM:
                    ret = new EmptyStatement(jsonObject);
                    break;
                case ES_TYPE_WITH_STM:
                    ret = new WithStatement(jsonObject);
                    break;
                case ES_TYPE_RETURN_STM:
                    ret = new ReturnStatement(jsonObject);
                    break;
                case ES_TYPE_LABELED_STM:
                    ret = new LabeledStatement(jsonObject);
                    break;
                case ES_TYPE_BREAK_STM:
                    ret = new BreakStatement(jsonObject);
                    break;
                case ES_TYPE_CONTINUE_STM:
                    ret = new ContinueStatement(jsonObject);
                    break;
                case ES_TYPE_IF_STM:
                    ret = new IfStatement(jsonObject);
                    break;
                case ES_TYPE_SWITCH_STM:
                    ret = new SwitchStatement(jsonObject);
                    break;
                case ES_TYPE_SWITCH_CASE:
                    ret = new SwitchCase(jsonObject);
                    break;
                case ES_TYPE_THROW_STM:
                    ret = new ThrowStatement(jsonObject);
                    break;
                case ES_TYPE_TRY_STM:
                    ret = new TryStatement(jsonObject);
                    break;
                case ES_TYPE_CATCH_CLAUSE:
                    ret = new CatchClause(jsonObject);
                    break;
                case ES_TYPE_WHILE_STM:
                    ret = new WhileStatement(jsonObject);
                    break;
                case ES_TYPE_DO_WHILE_STM:
                    ret = new DoWhileStatement(jsonObject);
                    break;
                case ES_TYPE_FOR_STM:
                    ret = new ForStatement(jsonObject);
                    break;
                case ES_TYPE_FOR_IN_STM:
                    ret = new ForInStatement(jsonObject);
                    break;
                case ES_TYPE_FUNCTION_DEC:
                    ret = new FunctionDeclaration(jsonObject);
                    break;
                case ES_TYPE_THIS_EXP:
                    ret = new ThisExpression(jsonObject);
                    break;
                case ES_TYPE_OBJECT_EXP:
                    ret = new ObjectExpression(jsonObject);
                    break;
                case ES_TYPE_PROPERTY:
                    ret = new Property(jsonObject);
                    break;
                case ES_TYPE_ARRAY_EXP:
                    ret = new ArrayExpression(jsonObject);
                    break;
                case ES_TYPE_FUNCTION_EXP:
                    ret = new FunctionExpression(jsonObject);
                    break;
                case ES_TYPE_UNARY_EXP:
                case ES_TYPE_UPDATE_EXP:
                    ret = new UnaryExpression(jsonObject);
                    break;
                case ES_TYPE_BINARY_EXP:
                case ES_TYPE_LOGICAL_EXP:
                    ret = new BinaryExpression(jsonObject);
                    break;
                case ES_TYPE_ASSIGNMENT_EXP:
                    ret = new AssignmentExpression(jsonObject);
                    break;
                case ES_TYPE_MEMBER_EXP:
                    ret = new MemberExpression(jsonObject);
                    break;
                case ES_TYPE_COND_EXP:
                    ret = new ConditionalExpression(jsonObject);
                    break;
                case ES_TYPE_CALL_EXP:
                    ret = new CallExpression(jsonObject);
                    break;
                case ES_TYPE_NEW_EXP:
                    ret = new NewExpression(jsonObject);
                    break;
                case ES_TYPE_SEQ_EXP:
                    ret = new SequenceExpression(jsonObject);
                    break;
                case ES_TYPE_FOR_OF_STM:
                    ret = new ForOfStatement(jsonObject);
                    break;
                case ES_TYPE_SUPER:
                    ret = new SuperExpression(jsonObject);
                    break;

                default:
                    throw new ESTreeLoadFailed("unknown type: " + type);
            }
        } catch (JSONException e) {
            throw new ESTreeLoadFailed("invalid es tree", e);
        }

        try {
            ret.setParent(parent);
            return cast(ret);
        } catch (ClassCastException e) {
            throw new ESTreeLoadFailed("invalid type", e);
        }
    }

    // ~~~ type helpers
    default boolean is(Class<? extends Node> clazz) {
        return clazz.isAssignableFrom(getClass());
    }

    default <T extends Node> T as(Class<T> clazz) {
        return $$.cast(this, clazz);
    }

    default <T extends Node> T as() {
        return $$.cast(this);
    }

    // ~~~ es tree types
    String ES_TYPE_PROGRAM = "Program", ES_TYPE_IDENTIFIER = "Identifier",
            ES_TYPE_VAR_DEC = "VariableDeclaration", ES_TYPE_VAR_DECT = "VariableDeclarator",
            ES_TYPE_EXP_STM = "ExpressionStatement", ES_TYPE_EMPTY_STM = "EmptyStatement",
            ES_TYPE_BLOCK_STM = "BlockStatement", ES_TYPE_WITH_STM = "WithStatement",
            ES_TYPE_RETURN_STM = "ReturnStatement", ES_TYPE_LABELED_STM = "LabeledStatement",
            ES_TYPE_BREAK_STM = "BreakStatement", ES_TYPE_CONTINUE_STM = "ContinueStatement",
            ES_TYPE_IF_STM = "IfStatement", ES_TYPE_SWITCH_STM = "SwitchStatement",
            ES_TYPE_SWITCH_CASE = "SwitchCase", ES_TYPE_THROW_STM = "ThrowStatement",
            ES_TYPE_TRY_STM = "TryStatement", ES_TYPE_CATCH_CLAUSE = "CatchClause",
            ES_TYPE_WHILE_STM = "WhileStatement", ES_TYPE_DO_WHILE_STM = "DoWhileStatement",
            ES_TYPE_FOR_STM = "ForStatement", ES_TYPE_FOR_IN_STM = "ForInStatement",
            ES_TYPE_FUNCTION_DEC = "FunctionDeclaration", ES_TYPE_THIS_EXP = "ThisExpression",
            ES_TYPE_ARRAY_EXP = "ArrayExpression", ES_TYPE_OBJECT_EXP = "ObjectExpression",
            ES_TYPE_PROPERTY = "Property", ES_TYPE_FUNCTION_EXP = "FunctionExpression",
            ES_TYPE_UNARY_EXP = "UnaryExpression", ES_TYPE_UPDATE_EXP = "UpdateExpression",
            ES_TYPE_BINARY_EXP = "BinaryExpression", ES_TYPE_LOGICAL_EXP = "LogicalExpression",
            ES_TYPE_ASSIGNMENT_EXP = "AssignmentExpression",
            ES_TYPE_MEMBER_EXP = "MemberExpression", ES_TYPE_COND_EXP = "ConditionalExpression",
            ES_TYPE_CALL_EXP = "CallExpression", ES_TYPE_NEW_EXP = "NewExpression",
            ES_TYPE_SEQ_EXP = "SequenceExpression", ES_TYPE_FOR_OF_STM = "ForOfStatement",
            ES_TYPE_SUPER = "Super", ES_TYPE_LITERAL = "Literal",
            ES_EXPORT_NAMED_DECLARATION = "ExportNamedDeclaration",
            ES_AWAIT_EXPRESSION = "AwaitExpression";
}
