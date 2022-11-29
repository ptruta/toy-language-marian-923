package model.symbol;

public class SymString implements ISymbol {

    private String label;
    private String value;
    private String type;

    public SymString(String label, String value) {
        this.type = "String";
        this.label = label;
        this.value = new String(value);
    }

    public SymString(String label) {
        this.type = "String";
        this.label = label;
        this.defaultValue();
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void defaultValue() {
        this.setValue("");
    }

    @Override
    public String toString() {
        if (this.value == null) {
            return "Unassigned";
        }
        return this.getType() + ": " + this.getValue();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        if (this.value == "") {
            return "";
        }
        return this.value.split("\"")[1];
    }

}