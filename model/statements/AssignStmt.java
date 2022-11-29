package model.statements;

public class AssignStmt implements IStmt {
    private String type;
    private String contents;

    public AssignStmt(String contents) {
        this.type = "AssignStmt";
        this.contents = contents;
    }

    public String getType() {
        return this.type;
    }

    public String[] getWords() {
        // Return model: Name + "=" + value
        return this.contents.split(" ");
    }

    @Override
    public String getContents() {
        return this.contents;
    }
}
