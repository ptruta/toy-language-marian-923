package model.statements;

public class ReadFile implements IStmt {

    private String type;
    private String contents;

    public ReadFile(String contents) {
        this.type = "ReadFile";
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
        String[] words = new String[3];
        words[0] = this.contents.split("\\(")[0];// readFile
        words[1] = this.contents.split("\\(")[1].split(", ")[0];// exp
        words[2] = this.contents.split("\\(")[1].split(", ")[1].split("\\)")[0]; // var
        return words;
    }

}
