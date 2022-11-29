package controller.exestack;

import model.statements.*;

public interface IExeStack {
    public IStmt removeLast();

    public IStmt removeFirst();

    public void addLast(IStmt statement);

    public void addFirst(IStmt statement);

    public IStmt getElem(int index);
}
