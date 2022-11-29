package model.statements;

public class VarDecl implements IStmt {
    private String type;
    private String contents;

    public VarDecl(String contents) {
        this.type = "VarDecl";
        this.contents = contents;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String[] getWords() {
        // Return model: "Type + name"
        return this.contents.split(" ");
    }

    @Override
    public String getContents() {
        return this.contents;
    }

}
