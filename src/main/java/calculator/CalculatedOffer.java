package calculator;

public class CalculatedOffer {
    private final double offer;
    private final double sumAmounts;

    public CalculatedOffer(double offer, double sumAmounts){

        this.offer = offer;
        this.sumAmounts = sumAmounts;
    }

    public double getOffer() {
        return offer;
    }

    public double getSumAmounts() {
        return sumAmounts;
    }
}
