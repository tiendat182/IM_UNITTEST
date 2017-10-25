package com.viettel.bccs.inventory.dto;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Created by sinhhv on 12/10/2015.
 */
public class CheckboxTreeNodeNotLeaf extends CheckboxTreeNode {
    boolean isLeaf = false;

    public CheckboxTreeNodeNotLeaf(Object data, TreeNode parent, Boolean selected) {
        super(data, parent);
        super.setSelected(selected);
        isLeaf = false;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
}
