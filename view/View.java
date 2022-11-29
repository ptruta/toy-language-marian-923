package view;

import controller.evaluation.EvalUtil;
import controller.exestack.ExeStack;
import controller.exestack.MyDeque;
import controller.progstate.ProgramState;
import controller.symtable.SymTable;
import exceptions.DivisionByZero;
import exceptions.FileException;
import exceptions.StmtException;
import exceptions.SymbolException;
import exceptions.TypeException;
import model.statements.AssignStmt;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.statements.PrintStmt;
import model.symbol.ISymbol;

public class View implements IView {

    private ProgramState progState;

    public View(String program) {
        this.progState = new ProgramState(new String(program));
    }

    @Override
    public void displayProgState() {
        SymTable<String, ISymbol> table = this.progState.getSymTable();
        ExeStack stack = this.progState.getStack();
        MyDeque<String> output = this.progState.getOutput();

        String tableStr = table.toString();
        String stackStr = stack.toString();
        String filetableStr = this.progState.getFileTable().toString();
        String outputStr = "";
        if (output.size() == 0) {
            outputStr = "";
        } else
            for (int i = 0; i < output.size(); i++) {
                outputStr += output.get(i) + "| ";
            }
        System.out.println("\n");
        System.out.println("################################");
        System.out.println("Table: " + tableStr);
        System.out.println("Stack: " + stackStr);
        System.out.println("Output: " + outputStr);
        System.out.println("FileTable: " + filetableStr);
        System.out.println("################################");
        System.out.println("\n");
    }

    @Override
    public void execute() {
        try {
            IStmt last;
            while (true) {
                this.displayProgState();
                this.progState.logProgramStateExec();
                // After each step we have to check if the program's execution has ended
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // Conditional case
                this.progState.nextIsIf();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
//                else {
//                    String[] ifStmtOutput = last.getContents().split(";");
//                    String output1 = ifStmtOutput[ifStmtOutput.length - 1];
//
//                   EvalUtil.conditionalHelper(,output1,last,this.progState.getStack(),this.progState.get)
//                }
                // Assign case
                this.progState.nextIsAssign();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // Declaration case
                this.progState.nextIsDecl();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // Print case
                this.progState.nextIsPrint();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // File open case
                this.progState.nextIsOpen();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // Reading from file case
                this.progState.nextIsRead();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // Closing a file case.
                this.progState.nextIsClose();
                last = this.progState.getStack().getLast();
                if (last.getType() == "NOP") {
                    this.displayProgState();
                    this.progState.logProgramStateExec();
                    System.exit(0);
                }
                // this.progState.nextStep();
                // ++;
            }
        } catch (SymbolException s) {
            System.out.println(s.getMessage());
        } catch (TypeException t) {
            System.out.println(t.getMessage());
        } catch (DivisionByZero d) {
            System.out.println(d.getMessage());
        } catch (StmtException st) {
            System.out.println(st.getMessage());
        } catch (FileException f) {
            System.out.println(f.getMessage());
        }
    }

}
