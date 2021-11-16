package calculator;

import domain.MarketUpdate;
import domain.TwoWayPrice;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static domain.Instrument.INSTRUMENT0;
import static domain.Market.*;
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
        CalculatedBid bid = vwapCalculator.calculateBid(marketUpdates);
        assertEquals(10, bid.getBid());
        assertEquals(100, bid.getSumAmounts());
    }

    @Test
    public void bidReturnsCorrectlyForManyMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice1 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 0, 0);
        TwoWayPrice twoWayPrice2 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 20, 100, 0, 0);

        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice1);
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, twoWayPrice2);

        List<MarketUpdate> marketUpdates = new ArrayList<>();
        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);
        CalculatedBid bid = vwapCalculator.calculateBid(marketUpdates);
        assertEquals(10, vwap1.getBidPrice());
        assertEquals(20, vwap2.getBidPrice());
        assertEquals(100, vwap1.getBidAmount());
        assertEquals(100, vwap2.getBidAmount());
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
        assertEquals(20, vwap2.getBidPrice());
        assertEquals(15, vwap3.getBidPrice());
        assertEquals(100, vwap1.getBidAmount());
        assertEquals(100, vwap2.getBidAmount());
        assertEquals(200, vwap3.getBidAmount());

    }

    @Test
    public void calculateOfferReturnsCorrectlyForSingleMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100);
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice);
        List<MarketUpdate> marketUpdates = new ArrayList<>();
        marketUpdates.add(marketUpdate1);
        CalculatedOffer offer = vwapCalculator.calculateOffer(marketUpdates);
        assertEquals(20, offer.getOffer());
        assertEquals(100, offer.getSumAmounts());
    }

    @Test
    public void offerReturnsCorrectlyForManyMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        TwoWayPrice twoWayPrice1 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100);
        TwoWayPrice twoWayPrice2 = new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100);

        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, twoWayPrice1);
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET0, twoWayPrice2);

        List<MarketUpdate> marketUpdates = new ArrayList<>();
        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);

        CalculatedOffer offer = vwapCalculator.calculateOffer(marketUpdates);
        assertEquals(10, vwap1.getOfferPrice());
        assertEquals(20, vwap2.getOfferPrice());
        assertEquals(100, vwap1.getOfferAmount());
        assertEquals(100, vwap2.getOfferAmount());
    }

    @Test
    public void offerForDifferentMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET1, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 20, 100));
        MarketUpdate marketUpdate3 = new VWAPMarketUpdate(MARKET2, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));
        MarketUpdate marketUpdate4 = new VWAPMarketUpdate(MARKET3, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 0, 0, 10, 100));

        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);
        VWAPTwoWayPrice vwap3 = vwapCalculator.applyMarketUpdate(marketUpdate3);
        VWAPTwoWayPrice vwap4 = vwapCalculator.applyMarketUpdate(marketUpdate4);

        assertEquals(10, vwap1.getOfferPrice());
        assertEquals(15, vwap2.getOfferPrice());
        assertEquals(13.33, vwap3.getOfferPrice(), 0.01);
        assertEquals(12.5, vwap4.getOfferPrice());
        assertEquals(400, vwap4.getOfferAmount());
    }

    @Test
    public void bidAndOfferForMarketUpdates() {
        VWAP vwapCalculator = new VWAP();
        MarketUpdate marketUpdate1 = new VWAPMarketUpdate(MARKET0, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 10, 100));
        MarketUpdate marketUpdate2 = new VWAPMarketUpdate(MARKET1, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 15, 100, 20, 100));
        MarketUpdate marketUpdate3 = new VWAPMarketUpdate(MARKET2, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 10, 100, 10, 100));
        MarketUpdate marketUpdate4 = new VWAPMarketUpdate(MARKET3, new VWAPTwoWayPrice(INSTRUMENT0, FIRM, 20, 100, 10, 100));

        VWAPTwoWayPrice vwap1 = vwapCalculator.applyMarketUpdate(marketUpdate1);
        VWAPTwoWayPrice vwap2 = vwapCalculator.applyMarketUpdate(marketUpdate2);
        VWAPTwoWayPrice vwap3 = vwapCalculator.applyMarketUpdate(marketUpdate3);
        VWAPTwoWayPrice vwap4 = vwapCalculator.applyMarketUpdate(marketUpdate4);

        assertEquals(10, vwap1.getOfferPrice());
        assertEquals(15, vwap2.getOfferPrice());
        assertEquals(13.33, vwap3.getOfferPrice(), 0.01);
        assertEquals(12.5, vwap4.getOfferPrice());
        assertEquals(400, vwap4.getOfferAmount());

        assertEquals(10, vwap1.getBidPrice());
        assertEquals(12.5, vwap2.getBidPrice());
        assertEquals(11.66, vwap3.getBidPrice(), 0.01);
        assertEquals(13.75, vwap4.getBidPrice());
        assertEquals(400, vwap4.getBidAmount());
    }
}