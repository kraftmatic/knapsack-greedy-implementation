import java.math.BigDecimal;

public class Item {

    private Item(){};
    public Item(int objectWeight, BigDecimal objectValue){
        this.objectWeight = objectWeight;
        this.objectValue = objectValue;
    }

    private int objectWeight;
    private BigDecimal objectValue;

    public int getObjectWeight() {
        return objectWeight;
    }

    public void setObjectWeight(int objectWeight) {
        this.objectWeight = objectWeight;
    }

    public BigDecimal getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(BigDecimal objectValue) {
        this.objectValue = objectValue;
    }

}
