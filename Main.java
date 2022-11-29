import view.View;
// import model.statements.*;
// import model.symbol.SymString;

public class Main {
    public static void main(String[] args) {
        // Actual program
        // String program = "String exp; exp = \"test.in\"; openRFile(exp); Int b; b = 2
        // + 3; Int c; c = 2; b = 3 * c; readFile(exp, c); closeRFile(exp); Bool bol;
        // bol = true; Print(bol); Print(\"Condition not met\"); If c > b Then
        // Print(\"In fiecare zi vad garda\") Else b = 1;";
        String program = "Int a; Int b; a = 10 - 5 * 7 * 4; b = a + a + 1; Print(b)";
//        String program1 = "IF(a) THEN(v = 2) ELSE (v = 3) then a = true; int v; bool a;";
        View view = new View(program);
//        View view1 = new View(program1);
        view.execute();
//        view1.execute();
    }
}