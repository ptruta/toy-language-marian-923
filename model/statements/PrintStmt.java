package model.statements;

public class PrintStmt implements IStmt {
    private String type;
    private String contents;

    public PrintStmt(String content) {
        this.type = "PrintStmt";
        this.contents = content;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String[] getWords() {
        // Return model1: Print + "exp)"
        // Return model2: Print + "Symbol)"
        return this.contents.split("\\(");
    }

    @Override
    public String getContents() {
        return this.contents;
    }

}
