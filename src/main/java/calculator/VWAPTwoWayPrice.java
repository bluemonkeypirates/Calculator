package calculator;

import domain.Instrument;
import domain.State;
import domain.TwoWayPrice;

public class VWAPTwoWayPrice implements TwoWayPrice {
    private final Instrument instrument;
    private final State state;
    private final double bidPrice;
    private final double bidAmount;
    private final double offerPrice;
    private final double offerAmount;

    public VWAPTwoWayPrice(Instrument instrument, State state, double bidPrice, double bidAmount, double offerPrice, double offerAmount) {
        this.instrument = instrument;
        this.state = state;
        this.bidPrice = bidPrice;
        this.bidAmount = bidAmount;
        this.offerPrice = offerPrice;
        this.offerAmount = offerAmount;
    }

    @Override
    public double getBidPrice() {
        return bidPrice;
    }

    @Override
    public double getOfferAmount() {
        return offerAmount;
    }

    @Override
    public double getOfferPrice() {
        return offerPrice;
    }

    @Override
    public double getBidAmount() {
        return bidAmount;
    }

    @Override
    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public State getState() {
        return state;
    }
}
