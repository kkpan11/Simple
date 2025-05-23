package com.seaofnodes.simple;

import com.seaofnodes.simple.type.*;


/**
 *  The tracked fields are now complex enough to deserve a array-of-structs layout
 */
public class Var {

    public final String _name;   // Declared name
    // These fields are not final for forward reference late updates or
    // promotions.
    public int _idx;             // index in containing scope
    private Type _type;          // Declared type
    public boolean _final;       // Final field
    public boolean _fref;        // Forward ref
    public Parser.Lexer _loc;    // Source location

    public Var(int idx, String name, Type type, boolean xfinal, Parser.Lexer loc ) {
        this(idx,name,type,xfinal,loc,false);
    }
    public Var(int idx, String name, Type type, boolean xfinal, Parser.Lexer loc, boolean fref) {
        _idx = idx;
        _name = name;
        _type = type;
        _final = xfinal;
        _fref = fref;
        _loc = loc;
    }
    public Type type() {
        if( !_type.isFRef() ) return _type;
        // Update self to no longer use the forward ref type
        Type def = Parser.TYPES.get(((TypeMemPtr)_type)._obj._name);
        return (_type=_type.meet(def));
    }

    // Forward reference variables (not types) must be BOTTOM and
    // distinct from inferred variables
    public boolean isFRef() { return _fref; }

    public void defFRef( Type type, boolean xfinal, Parser.Lexer loc ) {
        assert isFRef() && xfinal;
        _type = type;
        _final = true;
        _fref = false;
        _loc = loc;
    }

    @Override public String toString() {
        return _type.toString()+(_final ? " ": " !")+_name;
    }
}
