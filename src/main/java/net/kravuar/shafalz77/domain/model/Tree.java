package net.kravuar.shafalz77.domain.model;

import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class Tree {
    public SymbolMapping mapping;
    public Tree left;
    public Tree right;

    public Tree(byte symbol) {this.mapping = new SymbolMapping(symbol, new BigEndianBitSet());}


    public Set<Tree> getLeaves() {
        var leaves = new HashSet<Tree>();
        getLeaves(leaves, this);
        return leaves;
    }
    private static void getLeaves(Set<Tree> leaves, Tree node) {
        if (node == null)
            return;
        if (node.left == null && node.right == null)
            leaves.add(node);
        else {
            getLeaves(leaves, node.left);
            getLeaves(leaves, node.right);
        }
    }
}
