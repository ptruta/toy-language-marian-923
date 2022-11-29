package controller.symtable;

import java.util.HashMap;

public interface ISymTable<Key, Value> {
    public void addSymbol(Key label, Value sym);

    public void setSymbol(Key label, Value sym);

    public Value getSymbol(Key label);

    public HashMap<Key, Value> getAll();
}
