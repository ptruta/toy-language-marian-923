package controller.exestack;

import model.statements.IStmt;

public class ExeStack implements IExeStack {

    private MyDeque<IStmt> stack;

    public ExeStack() {
        this.stack = new MyDeque<IStmt>();
    }

    @Override
    public IStmt removeLast() {
        return this.stack.removeLast();
    }

    @Override
    public IStmt removeFirst() {
        return this.stack.removeFirst();

    }

    @Override
    public void addLast(IStmt statement) {
        this.stack.addLast(statement);
    }

    @Override
    public void addFirst(IStmt statement) {
        this.stack.addFirst(statement);
    }

    @Override
    public IStmt getElem(int index) {
        return this.stack.get(index);
    }

    public IStmt getLast() {
        return this.stack.getLast();
    }

    public int size() {
        return this.stack.size();
    }

    @Override
    public String toString() {
        String rez = "";
        if (this.stack.size() == 0) {
            return "";
        }
        for (int i = 0; i < this.stack.size(); i++) {
            rez += this.stack.get(i).getContents() + "| ";
        }
        // for (IStmt stmt : this.stack) {
        // rez += stmt.getContents() + "| ";
        // }
        return rez;
    }
}
