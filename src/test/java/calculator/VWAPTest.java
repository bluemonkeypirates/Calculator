package calculator;

import domain.Instrument;
import domain.Market;
import domain.MarketUpdate;
import domain.TwoWayPrice;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    @Test
    public void vwapForManyMarketUpdate() {
        VWAP vwapCalculator = new VWAP();
        Map<Market, ArrayList<VWAPTwoWayPrice>> vwapTwoWayPricesForMarket = new HashMap<>();
        Map<Market, ArrayList<MarketUpdate>> marketUpdateForInstrumentMarkets = new HashMap<>();

        IntStream.range(1, 1000).forEach(x -> {
            double bidPrice = ThreadLocalRandom.current().nextDouble();
            double bidAmount = ThreadLocalRandom.current().nextDouble();
            double offerPrice = ThreadLocalRandom.current().nextDouble();
            double offerAmount = ThreadLocalRandom.current().nextDouble();

            Market market = Market.class.getEnumConstants()[new Random().nextInt(Market.class.getEnumConstants().length)];
            Instrument instrument = Instrument.class.getEnumConstants()[new Random().nextInt(Instrument.class.getEnumConstants().length)];

            MarketUpdate marketUpdate = new VWAPMarketUpdate(market, new VWAPTwoWayPrice(instrument, FIRM, bidPrice, bidAmount, offerPrice, offerAmount));
            VWAPTwoWayPrice vwapTwoWayPrice = vwapCalculator.applyMarketUpdate(marketUpdate);
            vwapTwoWayPricesForMarket.computeIfAbsent(market, k -> new ArrayList<>()).add(vwapTwoWayPrice);
            marketUpdateForInstrumentMarkets.computeIfAbsent(market, k -> new ArrayList<>()).add(marketUpdate);
        });

        marketUpdateForInstrumentMarkets.forEach((market, marketUpdates) -> {
            Arrays.stream(Instrument.class.getEnumConstants()).toList().forEach(instrument -> {
                List<VWAPTwoWayPrice> results = vwapTwoWayPricesForMarket.get(market).stream().filter(x -> x.getInstrument().equals(instrument)).collect(Collectors.toList());
                List<MarketUpdate> marketUpdatesForInstrument = marketUpdates.stream().filter(x -> x.getTwoWayPrice().getInstrument().equals(instrument)).collect(Collectors.toList());
                if (results.size() > 0) {
                    double sumBids = 0;
                    double sumBidAmounts = 0;
                    double sumOffers = 0;
                    double sumOfferAmounts = 0;
                    for (MarketUpdate marketUpdate : marketUpdatesForInstrument) {
                        sumBids += marketUpdate.getTwoWayPrice().getBidPrice() * marketUpdate.getTwoWayPrice().getBidAmount();
                        sumBidAmounts += marketUpdate.getTwoWayPrice().getBidAmount();
                        sumOffers += marketUpdate.getTwoWayPrice().getOfferPrice() * marketUpdate.getTwoWayPrice().getOfferAmount();
                        sumOfferAmounts += marketUpdate.getTwoWayPrice().getOfferAmount();
                    }

                    double expectedBid  = sumBids / sumBidAmounts;
                    double expectedOffer  = sumOffers / sumOfferAmounts;

                    assertEquals(expectedBid, results.get(results.size() - 1).getBidAmount());
                    assertEquals(expectedOffer, results.get(results.size() - 1).getOfferAmount());
                }
            });

        });
    }

    ;
}