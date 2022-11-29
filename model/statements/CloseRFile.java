package model.statements;

public class CloseRFile implements IStmt {

    private String type;
    private String contents;

    public CloseRFile(String contents) {
        this.type = "CloseRFile";
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
        // Expression: closeRFile(exp) ->
        String[] words = this.getContents().split("\\(");
        words[1] = words[1].split("\\)")[0];
        // -> String[] = ["closeRFile", "exp"]
        return words;
    }

}
