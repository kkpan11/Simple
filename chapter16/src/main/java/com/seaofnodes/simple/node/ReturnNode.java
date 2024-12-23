package com.seaofnodes.simple.node;

import com.seaofnodes.simple.type.*;

import java.util.BitSet;

/**
 * The Return node has two inputs.  The first input is a control node and the
 * second is the data node that supplies the return value.
 * <p>
 * In this presentation, Return functions as a Stop node, since multiple <code>return</code> statements are not possible.
 * The Stop node will be introduced in Chapter 6 when we implement <code>if</code> statements.
 * <p>
 * The Return's output is the value from the data node.
 */
public class ReturnNode extends CFGNode {

    public ReturnNode(Node ctrl, Node data, ScopeNode scope) {
        // Add memory slices to Return, so all memory updates are live-on-exit.
        super(ctrl, data);
        if( scope!=null )
            for( int i=2; i<scope.mem().nIns(); i++ )
                addDef(scope.mem().in(i));
    }

    public Node ctrl() { return in(0); }
    public Node expr() { return in(1); }

    @Override
    public String label() { return "Return"; }

    @Override
    StringBuilder _print1(StringBuilder sb, BitSet visited) {
        sb.append("return ");
        expr()._print0(sb, visited);
        return sb.append(";");
    }

    @Override
    public Type compute() {
        return TypeTuple.make(ctrl()._type,expr()._type);
    }

    @Override
    public Node idealize() {
        if( ctrl()._type==Type.XCONTROL )
            return ctrl();
        return null;
    }

    @Override public Node getBlockStart() { return ctrl().getBlockStart(); }
}
