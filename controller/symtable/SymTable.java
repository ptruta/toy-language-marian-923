package controller.symtable;

import java.util.HashMap;

public class SymTable<Key, Value> implements ISymTable<Key, Value> {

    private HashMap<Key, Value> symbols;

    public SymTable() {
        this.symbols = new HashMap<Key, Value>();
    }

    // We'll need it in order to put symbols in the table.
    @Override
    public void addSymbol(Key label, Value sym) {
        this.symbols.put(label, sym);
    }

    @Override
    public void setSymbol(Key label, Value sym) {
        this.symbols.remove(label);
        this.addSymbol(label, sym);
    }

    // We'll need it to evaluate expressions
    @Override
    public Value getSymbol(Key label) {
        Value s = this.symbols.get(label);
        return s;
    }

    @Override
    public HashMap<Key, Value> getAll() {
        return this.symbols;
    }

    @Override
    public String toString() {
        String rez = "";
        if (symbols.size() == 0) {
            return "";
        }
        for (Key k : this.symbols.keySet()) {
            rez += k.toString() + " " + this.getSymbol(k).toString() + "| ";
        }
        return rez;
    }

    public int size() {
        return this.symbols.size();
    }
}
