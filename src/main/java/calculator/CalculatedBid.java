package calculator;

public class CalculatedBid {
    private final double bid;
    private final double sumAmounts;

    public CalculatedBid(double bid, double sumAmounts){
        this.bid = bid;
        this.sumAmounts = sumAmounts;
    }

    public double getBid() {
        return bid;
    }

    public double getSumAmounts() {
        return sumAmounts;
    }
}
