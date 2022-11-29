package model.symbol;

public class SymBoolean implements ISymbol {
    private Boolean value;
    private String type;
    private String label;

    public SymBoolean(Boolean val, String label) {
        this.type = "Bool";
        this.value = val;
        this.label = label;
    }

    public SymBoolean(String label) {
        this.defaultValue();
        this.type = "Bool";
        this.label = label;
    }

    public void setValue(Boolean val) {
        this.value = val;
    }

    public Boolean getValue() {
        return this.value;
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
    public String toString() {
        if (this.value == null) {
            return "Unassigned";
        }
        return this.type + ": " + this.value.toString();
    }

    @Override
    public void defaultValue() {
        this.setValue(null);
    }

}
