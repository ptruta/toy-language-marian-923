package controller.progstate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import controller.evaluation.EvalUtil;
import controller.exestack.ExeStack;
import controller.exestack.MyDeque;
import controller.filetable.FileTable;
import controller.symtable.SymTable;
import exceptions.DivisionByZero;
import exceptions.FileException;
import exceptions.StmtException;
import exceptions.SymbolException;
import exceptions.TypeException;
import model.statements.*;
import model.symbol.ISymbol;
import model.symbol.SymBoolean;
import model.symbol.SymInteger;
import model.symbol.SymString;

public class ProgramState implements IProgramState {

    private SymTable<String, ISymbol> symtable;
    private ExeStack stack;
    private MyDeque<String> output;
    private FileTable<SymString, BufferedReader> filetable;

    public ProgramState(String program) {
        this.symtable = new SymTable<String, ISymbol>();
        this.stack = new ExeStack();
        this.output = new MyDeque<String>();
        this.filetable = new FileTable<>();
        this.stack.addLast(new CompStmt(new String(program)));
    }

    @Override
    public SymTable<String, ISymbol> getSymTable() {
        return this.symtable;
    }

    @Override
    public ExeStack getStack() {
        return this.stack;
    }

    @Override
    public MyDeque<String> getOutput() {
        return this.output;
    }

    @Override
    public FileTable<SymString, BufferedReader> getFileTable() {
        return this.filetable;
    }

    @Override
    public void nextIsAssign() throws SymbolException, TypeException, DivisionByZero {
        CompStmt comp;
        comp = (CompStmt) this.stack.getLast();
        String assignContent = comp.getStmt();
        try {
            // IStmt nexCompStmt = ((CompStmt) comp).nextCompStmt();
            AssignStmt stmt = new AssignStmt(assignContent);
            AssignStmt astmt = EvalUtil.processAssign(symtable, stmt);
            if (astmt == null)
                return;
            this.stack.removeLast();
            this.stack.addLast(((CompStmt) comp).nextCompStmt());
            this.stack.addFirst(astmt);
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        } catch (TypeException t) {
            throw new TypeException(t.getMessage());
        } catch (DivisionByZero d) {
            throw new DivisionByZero(d.getMessage());
        }

    }

    @Override
    public void nextIsDecl() throws SymbolException {
        CompStmt comp = (CompStmt) this.stack.getLast();
        String declContent = comp.getStmt();
        try {
            VarDecl v = new VarDecl(declContent);
            VarDecl dstmt = EvalUtil.processDecl(symtable, v);
            if (dstmt == null)
                return;
            this.stack.removeLast();
            this.stack.addLast(comp.nextCompStmt());
            this.stack.addFirst(v);

        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        }
    }

    @Override
    public void nextIsIf() throws SymbolException, TypeException, DivisionByZero, StmtException {

        CompStmt comp = (CompStmt) this.stack.getLast();
        String condContent = comp.getStmt();
        try {
            IfStmt conditional = new IfStmt(condContent);
            IfStmt cond = EvalUtil.processConditional(stack, output, symtable, conditional);
            // String prev = this.output.getFirst();
            if (cond == null) {
                return;
            }
            this.stack.removeLast();
            this.stack.addLast(comp.nextCompStmt());
            this.stack.addFirst(cond);
            // return this.toPrint(prev);
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        } catch (TypeException t) {
            throw new TypeException(t.getMessage());
        } catch (DivisionByZero d) {
            throw new DivisionByZero(d.getMessage());
        } catch (StmtException st) {
            throw new StmtException(st.getMessage());
        }

    }

    @Override
    public void nextIsPrint() throws SymbolException {
        CompStmt comp = (CompStmt) this.stack.getLast();
        String printContent = comp.getStmt();
        try {
            PrintStmt print = new PrintStmt(printContent);
            PrintStmt check = EvalUtil.processPrint(output, symtable, print);
            if (check == null) {
                return;
            }
            this.stack.removeLast();
            this.stack.addLast(comp.nextCompStmt());
            this.stack.addFirst(check);
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        }
    }

    @Override
    public void nextIsOpen() throws FileException, SymbolException {
        CompStmt comp = (CompStmt) this.stack.getLast();
        String openContent = comp.getStmt();
        try {
            OpenRFile open = new OpenRFile(openContent);
            OpenRFile check = EvalUtil.processOpen(symtable, filetable, open);
            if (check == null) {
                return;
            }
            this.stack.removeLast();
            this.stack.addFirst(check);
            this.stack.addLast(comp.nextCompStmt());
        } catch (FileException f) {
            throw new FileException(f.getMessage());
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        }
    }

    @Override
    public void nextIsRead() throws FileException, SymbolException {
        CompStmt comp = (CompStmt) this.stack.getLast();
        String readContent = comp.getStmt();
        try {
            ReadFile read = new ReadFile(readContent);
            ReadFile check = EvalUtil.processRead(symtable, filetable, read);
            if (check == null) {
                return;
            }
            this.stack.removeLast();
            this.stack.addFirst(check);
            this.stack.addLast(comp.nextCompStmt());
        } catch (FileException f) {
            throw new FileException(f.getMessage());
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        }
    }

    @Override
    public void nextIsClose() throws FileException, SymbolException {
        CompStmt comp = (CompStmt) this.stack.getLast();
        String closeContent = comp.getStmt();
        try {
            CloseRFile close = new CloseRFile(closeContent);
            CloseRFile check = EvalUtil.processClose(symtable, filetable, close);
            if (check == null) {
                return;
            }
            this.stack.removeLast();
            this.stack.addFirst(check);
            this.stack.addLast(comp.nextCompStmt());
        } catch (FileException f) {
            throw new FileException(f.getMessage());
        } catch (SymbolException s) {
            throw new SymbolException(s.getMessage());
        }
    }

    @Override
    public void nextStep() throws SymbolException, TypeException, DivisionByZero, StmtException, FileException {
        // try {
        // IStmt last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsIf();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsAssign();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsDecl();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsPrint();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsOpen();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsRead();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }
        // this.nextIsClose();
        // last = this.stack.getLast();
        // if (last.getType() == "NOP") {
        // System.exit(0);
        // }

        // } catch (SymbolException s) {
        // throw new SymbolException(s.getMessage());
        // } catch (TypeException t) {
        // throw new TypeException(t.getMessage());
        // } catch (DivisionByZero d) {
        // throw new DivisionByZero(d.getMessage());
        // } catch (StmtException st) {
        // throw new StmtException(st.getMessage());
        // } catch (FileException f) {
        // throw new FileException(f.getMessage());
        // }
    }

    @Override
    public void logProgramStateExec() throws FileException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(
                    "logFile.txt", true)));
            logFile.flush();
            // Writing the stack to the log file
            logFile.append("#################################\n");
            logFile.append("Execution stack:\n");
            for (int i = 0; i < this.stack.size(); i++) {
                // logFile.println(this.stack.getElem(i).getContents());
                logFile.append(this.stack.getElem(i).getContents() + "\n");
            }
            logFile.append("\n");

            logFile.append("Symbols symtable:\n");
            for (String key : this.symtable.getAll().keySet()) {
                ISymbol sym = this.symtable.getSymbol(key);
                if (sym.getType() == "Int") {
                    if (((SymInteger) sym).getValue() == null)
                        logFile.append(sym.getType() + " " + sym.getLabel() + ": "
                                + "Not declared" + "\n");
                    else
                        logFile.append(sym.getType() + " " + sym.getLabel() + ": "
                                + ((SymInteger) sym).getValue().toString() + "\n");
                }

                if (sym.getType() == "Bool") {
                    if (((SymBoolean) sym).getValue() == null)
                        logFile.append(sym.getType() + " " + sym.getLabel() + ": "
                                + "Not declared" + "\n");
                    else
                        logFile.append(sym.getType() + " " + sym.getLabel() + ": "
                                + ((SymBoolean) sym).getValue().toString() + "\n");
                }

                if (sym.getType() == "String")
                    logFile.append(sym.getType()).append(" ").append(sym.getLabel()).append(": ").append(((SymString) sym).getValue()).append("\n");
            }
            logFile.append("\n");

            logFile.append("File table:\n");
            for (SymString key : this.filetable.getAll().keySet()) {
                BufferedReader sym = this.filetable.get(key);
                boolean b = sym.ready();
                logFile.append(key.getValue()).append(": ").append(Boolean.toString(b)).append("\n");
            }
            logFile.append("\n");

            logFile.append("Output stack:\n");
            for (int i = 0; i < this.output.size(); i++) {
                logFile.append(this.output.get(i).toString()).append("\n");
            }
            logFile.append("\n");
            logFile.close();
            logFile.append("#################################\n");
        } catch (IOException e) {
            throw new FileException("Cannot open log file");
        }
    }
}
