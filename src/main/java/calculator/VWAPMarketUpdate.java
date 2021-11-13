package calculator;

import domain.Market;
import domain.MarketUpdate;
import domain.TwoWayPrice;

public class VWAPMarketUpdate implements MarketUpdate {
    Market market;
    TwoWayPrice twoWayPrice;

    public VWAPMarketUpdate(Market market, TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    @Override
    public Market getMarket() {
        return market;
    }

    @Override
    public TwoWayPrice getTwoWayPrice() {
        return twoWayPrice;
    }
}
