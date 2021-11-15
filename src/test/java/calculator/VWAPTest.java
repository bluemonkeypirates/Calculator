package calculator;

import domain.MarketUpdate;
import domain.TwoWayPrice;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static domain.Instrument.INSTRUMENT0;
import static domain.Market.MARKET0;
import static domain.Market.MARKET1;
import static domain.State.FIRM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VWAPTest {

    @Test
    public void calculateBidReturnsCorrectlyForSingleMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 0, 0);
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice);
        List<MarketUpdate> marketUpdates = new ArrayList<>();
        marketUpdates.add(marketUpdate1);
        double bid = vwapCalculator.calculateBid(marketUpdates);
        assertEquals(10, bid);
    }

    @Test
    public void bidReturnsCorrectlyForManyMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice1 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 0, 0);
        TwoWayPrice twoWayPrice2 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 20, 100, 0, 0);

        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice1);
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, twoWayPrice2);

        List<MarketUpdate> marketUpdates = new ArrayList<>();
        marketUpdates.add(marketUpdate1);
        marketUpdates.add(marketUpdate2);
        double bid = vwapCalculator.calculateBid(marketUpdates);
        assertEquals(15, bid);
    }

    @Test
    public void bidForDifferentMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 0, 0));
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 20, 100, 0, 0));
        MarketUpdate marketUpdate3 = new VWAPMarketUpdate(MARKET1, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 0, 0));

        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);
        VWAPTwoWayPrice vwap3 = vwapCalculator.applyMarketUpdate(marketUpdate3);

        assertEquals(10, vwap1.getBidPrice());
        assertEquals(15, vwap2.getBidPrice());
        assertEquals(10, vwap3.getBidPrice());

    }

    @Test
    public void calculateOfferReturnsCorrectlyForSingleMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100);
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice);
        List<MarketUpdate> marketUpdates = new ArrayList<>();
        marketUpdates.add(marketUpdate1);
        double offer = vwapCalculator.calculateOffer(marketUpdates);
        assertEquals(20, offer);
    }

    @Test
    public void offerReturnsCorrectlyForManyMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice1 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100);
        TwoWayPrice twoWayPrice2 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100);

        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice1);
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, twoWayPrice2);

        List<MarketUpdate> marketUpdates = new ArrayList<>();
        marketUpdates.add(marketUpdate1);
        marketUpdates.add(marketUpdate2);
        double offer = vwapCalculator.calculateOffer(marketUpdates);
        assertEquals(15, offer);
    }

    @Test
    public void offerForDifferentMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100));
        MarketUpdate marketUpdate3 = new VWAPMarketUpdate(MARKET1, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));
        MarketUpdate marketUpdate4 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));

        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);
        VWAPTwoWayPrice vwap3 = vwapCalculator.applyMarketUpdate(marketUpdate3);
        VWAPTwoWayPrice vwap4 = vwapCalculator.applyMarketUpdate(marketUpdate4);

        assertEquals(10, vwap1.getOfferPrice());
        assertEquals(15, vwap2.getOfferPrice());
        assertEquals(10, vwap3.getOfferPrice());
        assertEquals(13.33, vwap4.getOfferPrice(), 0.01);
    }
}