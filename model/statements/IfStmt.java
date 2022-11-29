package model.statements;

public class IfStmt implements IStmt {
    private String type;
    private String contents;

    public IfStmt(String contents) {
        this.type = "IfStmt";
        this.contents = contents;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String[] getWords() {
        // Return model: "If" + exp1 + "Then" + exp2 + "Else" + exp3
        String[] exp = this.contents.split(" ");
        String firstExp = "";
        String secondExp = "";
        int i;
        // Computing the first 2 expressions
        for (i = 1; i < exp.length - 1; i++) {
            if (exp[i + 1].startsWith("Then")) {
                firstExp += exp[i];
                break;
            }
            firstExp += exp[i] + " ";
        }
        if (firstExp.split(" ").length == 1) {
            firstExp = firstExp.split(" ")[0];
        }
        i++;
        int j;
        for (j = i + 1; j < exp.length - 1; j++) {
            if (exp[j + 1].endsWith(";")) {
                secondExp += exp[j] + " ";
                secondExp += exp[j + 1].split(";")[0];
                j++;
                break;
            }
            if (exp[j + 1].startsWith("Else")) {
                secondExp += exp[j];
                break;
            }
            secondExp += exp[j] + " ";
        }
        i = j++;
        if (i == exp.length - 1) {
            String[] finalExp = new String[4];
            finalExp[0] = "If";
            finalExp[1] = firstExp;
            finalExp[2] = "Then";
            finalExp[3] = secondExp;
            return finalExp;
        }
        // Computing the third one, if it exists
        i++;
        String thirdExp = "";
        for (int k = i + 1; k < exp.length - 1; k++) {
            if (exp[k + 1].endsWith(";")) {
                thirdExp += exp[k] + " ";
                thirdExp += exp[k + 1].split(";")[0];
                k++;
                break;
            }
            thirdExp += exp[k] + " ";
        }
        
        String[] finalExp = new String[6];
        finalExp[0] = "If";
        finalExp[1] = firstExp;
        finalExp[2] = "Then";
        finalExp[3] = secondExp;
        finalExp[4] = "Else";
        finalExp[5] = thirdExp;
        return finalExp;
        // return this.contents.split(" ");
    }

    @Override
    public String getContents() {
        return this.contents;
    }

}
