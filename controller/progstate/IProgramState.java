package controller.progstate;

import java.io.BufferedReader;

import controller.exestack.ExeStack;
import controller.exestack.MyDeque;
import controller.filetable.FileTable;
import controller.symtable.SymTable;
import exceptions.*;
import model.symbol.ISymbol;
import model.symbol.SymString;

public interface IProgramState {
    // Getters
    public SymTable<String, ISymbol> getSymTable();

    public ExeStack getStack();

    public MyDeque<String> getOutput();

    public FileTable<SymString, BufferedReader> getFileTable();

    // Steppers
    public void nextIsAssign() throws SymbolException, TypeException, DivisionByZero;

    public void nextIsIf() throws SymbolException, TypeException, DivisionByZero, StmtException;

    public void nextIsPrint() throws SymbolException, TypeException, DivisionByZero;

    public void nextIsDecl() throws SymbolException;

    public void nextIsOpen() throws FileException, SymbolException;

    public void nextIsRead() throws FileException, SymbolException;

    public void nextIsClose() throws FileException, SymbolException;

    public void nextStep() throws SymbolException, TypeException, DivisionByZero, StmtException, FileException;

    // Loggers
    public void logProgramStateExec() throws FileException;
}
