package model.symbol;

public class SymInteger implements ISymbol {
    private Integer value;
    private String label;
    private String type;

    public SymInteger(Integer val, String label) {
        this.type = "Int";
        this.value = val;
        this.label = label;
    }

    public SymInteger(String label) {
        this.defaultValue();
        this.label = label;
        this.type = "Int";
    }

    public void setValue(Integer val) {
        this.value = val;
    }

    public Integer getValue() {
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
