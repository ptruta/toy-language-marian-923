package model.statements;

public class OpenRFile implements IStmt {

    private String type;
    private String contents;

    public OpenRFile(String contents) {
        this.type = "OpenRFile";
        this.contents = contents;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getContents() {
        return this.contents;
    }

    @Override
    public String[] getWords() {
        // Expression: openRFile(exp) ->
        String[] words = this.getContents().split("\\(");
        words[1] = words[1].split("\\)")[0];
        // -> String[] = ["openRFile", "exp"]
        return words;
    }

}
