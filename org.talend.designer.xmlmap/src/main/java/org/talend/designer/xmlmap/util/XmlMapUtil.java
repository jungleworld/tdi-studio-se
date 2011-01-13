// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.util;

import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapUtil {

    public static final String DOCUMENT = "id_Document";

    public static final String XPATH_SEPARATOR = "/";

    public static final String EXPRESSION_SEPARATOR = ".";

    public static final String DEFAULT_DATA_TYPE = "id_String";

    public static final String CHILDREN_SEPARATOR = ":";

    public static final String EXPRESSION_LEFT = "[";

    public static final String EXPRESSION_RIGHT = "]";

    public static final String XPATH_ATTRIBUTE = "@";

    public static final String XPATH_NAMESPACE = "xmlns";

    public static int getXPathLength(String xPath) {
        if (xPath == null) {
            return 0;
        }
        return xPath.split(XPATH_SEPARATOR).length;

    }

    public static String convertToExpression(String xPath) {
        if (xPath == null) {
            return xPath;
        }
        String[] split = xPath.split(XPATH_SEPARATOR);
        // normal column
        if (split.length == 2) {
            return xPath.replaceAll(XPATH_SEPARATOR, EXPRESSION_SEPARATOR);
        } else if (split.length > 2) {
            // separator after root
            int indexOf = xPath.indexOf("/", xPath.indexOf("/") + 1);
            if (indexOf != -1) {
                String rootPath = xPath.substring(0, indexOf);
                rootPath = rootPath.replace(XPATH_SEPARATOR, EXPRESSION_SEPARATOR);
                String childrenPath = xPath.substring(indexOf, xPath.length());

                return EXPRESSION_LEFT + rootPath + CHILDREN_SEPARATOR + childrenPath + EXPRESSION_RIGHT;

            }
        }

        return xPath.replaceAll(EXPRESSION_SEPARATOR, XPATH_SEPARATOR);

    }

    /*
     * convert from output expression to xpath
     */
    public static String convertToXpath(String expression) {
        if (expression == null) {
            return expression;
        }
        if (expression.indexOf(EXPRESSION_LEFT) == 0 && expression.indexOf(EXPRESSION_RIGHT) == expression.length() - 1
                && expression.indexOf(expression) != -1) {
            expression = expression.substring(expression.indexOf(EXPRESSION_LEFT), expression.indexOf(EXPRESSION_RIGHT));
            expression = expression.replaceAll("", CHILDREN_SEPARATOR);

        }
        return expression.replace(XPATH_SEPARATOR, EXPRESSION_SEPARATOR);

    }

    public static TreeNode getInputTreeNodeRoot(TreeNode model) {
        if (model.eContainer() instanceof InputXmlTree) {
            return model;
        } else if (model.eContainer() instanceof TreeNode) {
            return getInputTreeNodeRoot((TreeNode) model.eContainer());
        }
        return null;
    }

    public static OutputTreeNode getOutputTreeNodeRoot(OutputTreeNode model) {
        if (model.eContainer() instanceof OutputXmlTree) {
            return model;
        } else if (model.eContainer() instanceof OutputTreeNode) {
            return getOutputTreeNodeRoot((OutputTreeNode) model.eContainer());
        }
        return null;
    }

    public static void cleanSubGroup(OutputTreeNode node) {
        for (TreeNode treeNode : node.getChildren()) {
            OutputTreeNode outputNode = (OutputTreeNode) treeNode;
            if (outputNode.isGroup()) {
                outputNode.setGroup(false);
            }
            cleanSubGroup(outputNode);

        }

    }

    public static void main(String[] args) {

        String a = "row1/xml/cf/dd/e/f";
        convertToExpression(a);
    }

}
