package model.statements;

public class NOP implements IStmt {

    private String type;
    private String contents;

    NOP() {
        this.type = "NOP";
        this.contents = "";
    }

    @Override
    public final String getType() {
        // No type for the No Operation stmt
        return this.type;
    }

    @Override
    public String[] getWords() {
        // No words for the No Operation stmt
        return this.getWords();
    }

    @Override
    public String getContents() {
        return this.contents;
    }

}
