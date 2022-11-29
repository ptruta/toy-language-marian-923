package controller.evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

import com.sun.jdi.Value;
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
import model.symbol.*;

public class EvalUtil {

    public static ISymbol lookUp(SymTable<String, ISymbol> table, String label) {
        // Null if it can't find one or the sym.
        return table.getSymbol(label);
    }

    // Evaluating the Integer symbols
    public static boolean isInt(ISymbol sym) {
        return sym.getType().startsWith("Int");
    }

    public static Integer evalInt(ISymbol sym) {
        if (EvalUtil.isInt(sym)) {
            return ((SymInteger) sym).getValue();
        }
        return null;
    }

    // Evaluating the Boolean symbols
    public static boolean isBool(ISymbol sym) {
        return sym.getType().startsWith("Bool");
    }

    public static Boolean evalBoolean(ISymbol sym) {
        if (EvalUtil.isBool(sym)) {
            return ((SymBoolean) sym).getValue();
        }
        return null;
    }

    // Evaluating the String symbols
    public static boolean isString(ISymbol sym) {
        return sym.getType().startsWith("String");
    }

    public static String evalString(ISymbol sym) {
        if (EvalUtil.isString(sym)) {
            return ((SymString) sym).getValue();
        }
        return null;
    }

    // Conversion functions
    public static Integer convNumeric(String strNum) {
        Integer num;
        if (strNum == null) {
            return null;
        }
        try {
            num = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return null;
        }
        return num;
    }

    public static Boolean convBoolean(String strBool) {
        if (strBool == null) {
            return null;
        }
        Boolean b;
        try {
            b = Boolean.parseBoolean(strBool);
        } catch (NumberFormatException nfe) {
            return null;
        }
        return b;
    }

    // Checker functions
    public static boolean isIfStmt(IStmt statement) {
        return ((IfStmt) statement).getContents().startsWith("If");
    }

    public static boolean isAssignStmt(IStmt statement) {
        String tok = "";
        if (((AssignStmt) statement).getWords().length > 1) {
            tok = ((AssignStmt) statement).getWords()[1];
        } else {
            tok = ((AssignStmt) statement).getWords()[0];
        }
        return tok.startsWith("=");
    }

    public static boolean isPrintStmt(IStmt statement) {
        String first = ((PrintStmt) statement).getContents();
        return first.startsWith("Print");
    }

    public static boolean isVarDecl(IStmt statement) {
        return ((VarDecl) statement).getWords()[0].startsWith("Int")
                || ((VarDecl) statement).getWords()[0].startsWith("Bool")
                || ((VarDecl) statement).getWords()[0].startsWith("String");
    }

    public static boolean isOpenRFile(IStmt statement) {
        return ((OpenRFile) statement).getContents().startsWith("open");
    }

    public static boolean isReadFile(IStmt statement) {
        return ((ReadFile) statement).getContents().startsWith("read");
    }

    public static boolean isCloseFile(IStmt statement) {
        return ((CloseRFile) statement).getContents().startsWith("close");
    }

    // They eval expressions, no matter the terms
    public static Integer evalArithemtic(ISymbol sym1, ISymbol sym2, String operator)
            throws DivisionByZero, TypeException {
        if (((SymInteger) sym1).getType() == "Int") {
            if (((SymInteger) sym2).getType() == "Int") {
                Integer nr1 = ((SymInteger) sym1).getValue();
                Integer nr2 = ((SymInteger) sym2).getValue();
                if (operator.startsWith("+")) {
                    return nr1 + nr2;
                }
                if (operator.startsWith("*")) {
                    return nr1 * nr2;
                }
                if (operator.startsWith("-")) {
                    return nr1 - nr2;
                }
                if (operator.startsWith("/")) {
                    if (nr2 != 0) {
                        return nr1 / nr2;
                    }
                    throw new DivisionByZero("Cannot divide by zero!");
                }
            }
            throw new TypeException("Operands are not of same type");
        }
        throw new TypeException("Operands are not of same type");
    }

    public static Boolean evalLogical(ISymbol sym1, ISymbol sym2, String operator) throws TypeException {
        if (sym1.getType() == "Bool") {
            if (sym2.getType() == "Bool") {
                Boolean s1 = ((SymBoolean) sym1).getValue();
                Boolean s2 = ((SymBoolean) sym2).getValue();
                if (operator.startsWith("and")) {
                    return s1 && s2;
                }
                if (operator.startsWith("or")) {
                    return s1 || s2;
                }
            }
            throw new TypeException("Operands are not of same type");
        }
        if (sym1.getType() == "Int") {
            if (sym2.getType() == "Int") {
                Integer s1 = ((SymInteger) sym1).getValue();
                Integer s2 = ((SymInteger) sym2).getValue();
                if (operator.startsWith("<")) {
                    return s1 < s2;
                }
                if (operator.startsWith("<=")) {
                    return s1 <= s2;
                }
                if (operator.startsWith(">")) {
                    return s1 > s2;
                }
                if (operator.startsWith(">=")) {
                    return s1 >= s2;
                }
                if (operator.startsWith("==")) {
                    return s1 == s2;
                }
                if (operator.startsWith("!=")) {
                    return s1 != s2;
                }
            }
            throw new TypeException("Operands are not of the same type!");
        }
        throw new TypeException("Cannot evaluate");
    }

    public static String evalString(ISymbol sym1, ISymbol sym2, String operator) throws TypeException {
        if (EvalUtil.isString(sym1)) {
            if (EvalUtil.isString(sym2)) {
                if (operator.startsWith("+")) {
                    return ((SymString) sym1).getValue() + ((SymString) sym2).getValue();
                }
            }
            throw new TypeException("Operands are not of same type!");
        }
        return null;
    }

    // Processing the next step

    /**
     * @param table
     * @param stmt
     * @return returns the statement if it is of the type it's processing, null
     * otherwise
     * @throws TypeException
     * @throws SymbolException
     * @throws DivisionByZero
     */
    public static AssignStmt processAssign(SymTable<String, ISymbol> table, IStmt stmt)
            throws TypeException, SymbolException, DivisionByZero {
        Integer nextFinal;
        if (EvalUtil.isAssignStmt(stmt)) { // First we check if it's an assign statement
            try {
                String[] exp = ((AssignStmt) stmt).getWords();
                ISymbol sym = EvalUtil.lookUp(table, exp[0]);
                // We get the symbol, lookUp automatically checks if it's in table
                if (sym == null) {
                    throw new SymbolException("Variable is not declared.");
                }
                Integer rez1 = 0;
                if (EvalUtil.isInt(sym)) { // If our symbol is an integer
                    int i = 3;
                    Integer rez = 0;
                    if (i < exp.length - 1) {
                        boolean added = false;
                        while (i < exp.length - 1) {
                            if (exp[i].startsWith("*") || exp[i].startsWith("/")) {
                                Integer next = getNextInteger(table, exp, i);
                                rez1 = next;
                                int parseInt = i - 1;
                                exp[i - 1] = String.valueOf(next);
                                String[] arr_new = new String[exp.length - 1];
                                for (int count = 0, k = 0; count < exp.length; count++) {
                                    if (count != parseInt + 1) {
                                        arr_new[k] = exp[count];
                                        k++;
                                    }
                                }
                                String[] arr_new1 = new String[arr_new.length - 1];
                                for (int count = 0, k = 0; count < arr_new.length; count++) {
                                    if (count != parseInt + 1) {
                                        arr_new1[k] = arr_new[count];
                                        k++;
                                    }
                                }
                                exp = arr_new1;

                            }
                            i += 2;
                        }
                        rez = rez1;
                        i = 3;
                        if (i + 3 < exp.length && !Objects.equals(exp[i + 3], "else")) {
                            while (i < exp.length - 1) {
                                Integer next = getNextInteger(table, exp, i);

                                if (i + 1 <= exp.length) {
                                    exp[i + 1] = String.valueOf(next);
                                    rez = next;
                                } else {
                                    rez += next;
                                }

                                i += 2;
                            }
                            added = true;
                        }
                        i = 3;
                        if (i < exp.length && !added) {
                            while (i < exp.length - 1) {
                                Integer next = getNextInteger(table, exp, i);

                                if (i + 1 <= exp.length) {
                                    exp[i + 1] = String.valueOf(next);
                                    rez = next;
                                } else {
                                    rez += next;
                                }

                                i += 2;
                            }
                        }
                    } else {
                        Integer next = EvalUtil.convNumeric(exp[2]);
                        if (next == null) {
                            next = ((SymInteger) EvalUtil.lookUp(table, exp[2])).getValue();
                        }
                        rez = next;
                    }
                    table.setSymbol(sym.getLabel(), new SymInteger(rez, sym.getLabel()));
                    return (AssignStmt) stmt;
                }


                // Analog for the String value
                if (EvalUtil.isString(sym)) {
                    if (exp.length == 3) {
                        ISymbol rez = ((SymString) EvalUtil.lookUp(table, exp[2]));
                        if (rez == null) {
                            table.setSymbol(exp[0], new SymString(exp[0], exp[2]));
                            return (AssignStmt) stmt;
                        }
                        table.setSymbol(sym.getLabel(), new SymString(sym.getLabel(), ((SymString) rez).getValue()));
                        return (AssignStmt) stmt;
                    }
                }

                // Analog for the boolean value
                if (EvalUtil.isBool(sym)) {
                    Boolean rez = null;
                    if (exp.length == 3) {
                        rez = EvalUtil.convBoolean(exp[2]);
                        if (rez != null) {
                            table.setSymbol(sym.getLabel(), new SymBoolean(rez, sym.getLabel()));
                            return (AssignStmt) stmt;
                        } else {
                            rez = ((SymBoolean) EvalUtil.lookUp(table, exp[2])).getValue();
                            if (rez == null) {
                                throw new SymbolException("Variable is not declared.");
                            }
                            table.setSymbol(sym.getLabel(), new SymBoolean(rez, sym.getLabel()));
                            return (AssignStmt) stmt;
                        }
                    } else {
                        rez = EvalUtil.convBoolean(exp[2]);
                        if (rez == null)
                            rez = ((SymBoolean) EvalUtil.lookUp(table, exp[2])).getValue();
                        if (rez == null) {
                            throw new SymbolException("Variable is not declared.");
                        }
                        for (int i = 3; i < exp.length - 1; i = i + 2) {
                            ISymbol s2 = EvalUtil.lookUp(table, exp[i + 1]);
                            if (s2 != null) {
                                rez = EvalUtil.evalLogical(new SymBoolean(rez, ""), s2, exp[i]);
                            } else {
                                Boolean perm = EvalUtil.convBoolean(exp[i + 1]);
                                if (perm == null) {
                                    throw new SymbolException("Variable is not declared.");
                                }
                                rez = EvalUtil.evalLogical(new SymBoolean(rez, ""),
                                        new SymBoolean(perm, ""), exp[i]);
                            }
                        }
                        table.setSymbol(sym.getLabel(), new SymBoolean(rez, sym.getLabel()));
                        return (AssignStmt) stmt;
                    }
                }
            } catch (
                    SymbolException e) {
                throw new SymbolException(e.getMessage());
            } catch (
                    DivisionByZero d) {
                throw new DivisionByZero(d.getMessage());
            } catch (
                    TypeException t) {
                throw new TypeException(t.getMessage());
            }
        }
        return (AssignStmt) null;

    }

    private static Integer getNextInteger(SymTable<String, ISymbol> table, String[] exp, int i) throws DivisionByZero, TypeException {
        Integer next = EvalUtil.convNumeric(exp[i - 1]);
        if (next == null) {
            next = ((SymInteger) EvalUtil.lookUp(table, exp[i - 1])).getValue();
        }
        Integer next2 = EvalUtil.convNumeric(exp[i + 1]);
        if (next2 == null) {
            next2 = ((SymInteger) EvalUtil.lookUp(table, exp[i + 1])).getValue();
        }
        next = EvalUtil.evalArithemtic(new SymInteger(next, ""), new SymInteger(next2, ""),
                exp[i]);
        return next;
    }

    public static VarDecl processDecl(SymTable<String, ISymbol> table, IStmt v) throws SymbolException {
        String[] exp = ((VarDecl) v).getWords();
        if (EvalUtil.isVarDecl(v)) {
            if (exp[0].startsWith("Int")) {
                SymInteger s = new SymInteger(exp[1]);
                if (EvalUtil.lookUp(table, exp[1]) == null) {
                    table.addSymbol(s.getLabel(), s);
                    return (VarDecl) v;
                }
                throw new SymbolException("Variable: " + s.getLabel() + " cannot be added twice.");
            }
            if (exp[0].startsWith("Bool")) {
                SymBoolean s = new SymBoolean(exp[1]);
                if (EvalUtil.lookUp(table, exp[1]) == null) {
                    table.addSymbol(s.getLabel(), s);
                    return (VarDecl) v;
                }
                throw new SymbolException("Variable: " + s.getLabel() + " cannot be added twice.");
            }
            if (exp[0].startsWith("String")) {
                SymString s = new SymString(exp[1]);
                if (EvalUtil.lookUp(table, exp[1]) == null) {
                    table.addSymbol(s.getLabel(), s);
                    return (VarDecl) v;
                }
                throw new SymbolException("Variable: " + s.getLabel() + " cannot be added twice.");
            }
            throw new SymbolException("Syntax error!");
        }
        return (VarDecl) null;
    }

    public static PrintStmt processPrint(MyDeque<String> output, SymTable<String, ISymbol> table, IStmt v)
            throws SymbolException {
        if (EvalUtil.isPrintStmt(v)) {
            try {
                String[] exp = ((PrintStmt) v).getWords();
                if (exp[1].startsWith("\"")) {
                    String rez = ((PrintStmt) v).getWords()[1].split("\"")[1];
                    output.addFirst(rez);
                    return (PrintStmt) v;
                }
                String label = exp[1].split("\\)")[0];
                ISymbol sym = EvalUtil.lookUp(table, label);
                if (sym == null) {
                    throw new SymbolException("Variable is not declared.");
                }
                if (sym.getType() == "Int") {
                    output.addFirst(Integer.toString(((SymInteger) sym).getValue()));
                    return (PrintStmt) v;
                }
                if (sym.getType() == "Bool") {
                    output.addFirst(Boolean.toString(((SymBoolean) sym).getValue()));
                    return (PrintStmt) v;
                }

            } catch (SymbolException s) {
                throw new SymbolException(s.getMessage());
            }
        }
        return (PrintStmt) null;

    }

    public static IfStmt conditionalHelper(Boolean cond, String[] exp, IStmt conditional,
                                           ExeStack stack, SymTable<String, ISymbol> table, MyDeque<String> output)
            throws StmtException, TypeException, SymbolException, DivisionByZero {
        if (cond) {
            // If firstExp is true, then second exp can only be Assign or Print
            AssignStmt checkAssign = new AssignStmt(exp[3]);
            checkAssign = EvalUtil.processAssign(table, checkAssign);
            if (checkAssign == null) { // If it's not one, the it's the other
                PrintStmt checkPrint = new PrintStmt(exp[3]);
                checkPrint = EvalUtil.processPrint(output, table, checkPrint);
                if (checkPrint == null) {
                    throw new StmtException("Instruction cannot be done");
                }
                stack.addFirst(checkPrint);
            } else
                stack.addFirst(checkAssign);
        }
        if (exp.length == 4) {
            // Now we do the else branch
            AssignStmt checkAssign = new AssignStmt(exp[3]);
            checkAssign = EvalUtil.processAssign(table, checkAssign);
            if (checkAssign == null) { // If it's not one, the it's the other
                PrintStmt checkPrint = new PrintStmt(exp[3]);
                checkPrint = EvalUtil.processPrint(output, table, checkPrint);
                if (checkPrint == null) {
                    throw new StmtException("Instruction cannot be done");
                }
                stack.addFirst(checkPrint);
                return (IfStmt) conditional;
            } else {
                stack.addFirst(checkAssign);
                return (IfStmt) conditional;
            }
        } else {
            // Now we do the else branch
            AssignStmt checkAssign = new AssignStmt(exp[5]);
            checkAssign = EvalUtil.processAssign(table, checkAssign);
            if (checkAssign == null) { // If it's not one, the it's the other
                PrintStmt checkPrint = new PrintStmt(exp[5]);
                checkPrint = EvalUtil.processPrint(output, table, checkPrint);
                if (checkPrint == null) {
                    throw new StmtException("Instruction cannot be done");
                }
                stack.addFirst(checkPrint);
                return (IfStmt) conditional;
            } else {
                stack.addFirst(checkAssign);
                return (IfStmt) conditional;
            }
        }
    }

    public static IfStmt processConditional(ExeStack
                                                    stack, MyDeque<String> output, SymTable<String, ISymbol> table,
                                            IStmt conditional) throws SymbolException, TypeException, DivisionByZero, StmtException {
        if (EvalUtil.isIfStmt(conditional)) {
            try {
                String[] exp = ((IfStmt) conditional).getWords(); // Splitting into expressions
                String[] firstExp = exp[1].split(" ");
                if (firstExp.length == 1) { // Case 1: it has only a variable or a number
                    Integer a = EvalUtil.convNumeric(firstExp[0]);
                    Boolean b = EvalUtil.convBoolean(firstExp[0]);
                    ISymbol sym = EvalUtil.lookUp(table, firstExp[0]);
                    if (sym == null && a == null && b == null) {
                        throw new SymbolException("Condition cannot be evaluated");
                    }
                    if (sym.getType() == "Int" || a != null) {
                        Boolean cond = ((SymInteger) sym).getValue() != 0 || a != 0;
                        return EvalUtil.conditionalHelper(cond, exp, conditional, stack, table, output);
                    }
                    if (sym.getType() == "Bool" || b != null) {
                        Boolean cond = ((SymBoolean) sym).getValue() || b;
                        return EvalUtil.conditionalHelper(cond, exp, conditional, stack, table, output);
                    }
                }

                if (firstExp.length == 3) { // Case 2: It's comparing two variables
                    String operator = firstExp[1];
                    ISymbol var1 = EvalUtil.lookUp(table, firstExp[0]);
                    ISymbol var2 = EvalUtil.lookUp(table, firstExp[2]);
                    if (var1 != null && ((SymInteger) var1).getType() == "Int") {
                        // TODO - do the other cases of comparison ([var,int], [int, var], [int, int])
                        // Checking if it's comparing two variables (can only be integers)
                        if (var2 != null && ((SymInteger) var2).getType() == "Int") {
                            // Evaluating the exp as true
                            Boolean cond = EvalUtil.evalLogical(var1, var2, operator);
                            return EvalUtil.conditionalHelper(cond, exp, conditional, stack, table, output);
                        }
                        throw new SymbolException("Operands canot be of different type!");
                    }
                }

            } catch (SymbolException e) {
                throw new SymbolException(e.getMessage());
            } catch (TypeException t) {
                throw new TypeException(t.getMessage());
            } catch (DivisionByZero d) {
                throw new DivisionByZero(d.getMessage());
            }
        }
        return (IfStmt) null;
    }

    public static OpenRFile processOpen(SymTable<String, ISymbol> table,
                                        FileTable<SymString, BufferedReader> filetable, IStmt open) throws FileException, SymbolException {
        if (EvalUtil.isOpenRFile(open)) {
            String varLabel = ((OpenRFile) open).getWords()[1];
            ISymbol var = EvalUtil.lookUp(table, varLabel);
            try {
                if (var != null) {
                    String filename = ((SymString) var).getValue();
                    filetable.add((SymString) var, new BufferedReader(new FileReader(filename)));
                    return (OpenRFile) open;
                }
                throw new SymbolException("Variable " + varLabel + " is not declared!");
            } catch (FileNotFoundException fne) {
                throw new FileException("Cannot open file!");
            }
        }
        return (OpenRFile) null;
    }

    public static ReadFile processRead(SymTable<String, ISymbol> table,
                                       FileTable<SymString, BufferedReader> filetable, IStmt read) throws FileException, SymbolException {
        if (EvalUtil.isReadFile(read)) {
            // Extracting all the variables we need
            String filenameVarLabel = ((ReadFile) read).getWords()[1];
            String varLabel = ((ReadFile) read).getWords()[2];
            ISymbol filenameVar = EvalUtil.lookUp(table, filenameVarLabel);
            ISymbol var = EvalUtil.lookUp(table, varLabel);
            try {
                // Var is our int value so we have to check it
                if (var != null && EvalUtil.isInt(var)) {
                    // Checking if we have a valid filename
                    if (filenameVar != null) {
                        BufferedReader file = filetable.get((SymString) filenameVar);
                        // I do not know yet how this works so let's hope I do it right
                        while (file.ready()) {
                            String input = file.readLine();
                            Integer val = EvalUtil.convNumeric(input);
                            if (val != null) {
                                table.setSymbol(varLabel, new SymInteger(val, varLabel));
                                return (ReadFile) read;
                            }
                            throw new SymbolException("Cannot give string value to an Integer");
                        }
                    }
                    throw new FileException("Cannot open file, no path is given!");
                }
                throw new SymbolException("Variable is not declared!");
            } catch (IOException io) {
                throw new FileException("Cannot read from file!");
            }
        }
        return (ReadFile) null;
    }

    public static CloseRFile processClose(SymTable<String, ISymbol> table,
                                          FileTable<SymString, BufferedReader> filetable, IStmt read) throws SymbolException, FileException {
        if (EvalUtil.isCloseFile(read)) {
            String filenameVarLabel = ((CloseRFile) read).getWords()[1];
            ISymbol filenameVar = table.getSymbol(filenameVarLabel);
            try {
                if (filenameVar != null) {
                    BufferedReader file = filetable.get((SymString) filenameVar);
                    file.close();
                    filetable.remove((SymString) filenameVar);
                    return (CloseRFile) read;
                }
            } catch (IOException io) {
                throw new FileException("Cannot close unopened file!");
            }
            throw new SymbolException("Cannot open file! Undeclared variable: " + filenameVarLabel);
        }
        return (CloseRFile) null;
    }
}