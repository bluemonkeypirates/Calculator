package calculator;

import domain.Calculator;
import domain.Instrument;
import domain.Market;
import domain.MarketUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VWAP implements Calculator {
    private final Map<Instrument, List<MarketUpdate>> instrumentMap = new HashMap<>();

    @Override
    public VWAPTwoWayPrice applyMarketUpdate(MarketUpdate marketUpdate) {
        instrumentMap.computeIfAbsent(marketUpdate.getTwoWayPrice().getInstrument(), k -> new ArrayList<>()).add(marketUpdate);
        List<MarketUpdate> marketUpdates = getMarketUpdatesForInstruments(marketUpdate.getMarket());

        double bidPrice = calculateBid(marketUpdates);
        double officePrice = calculateOffer(marketUpdates);

        return new VWAPTwoWayPrice(
                marketUpdate.getTwoWayPrice().getInstrument(),
                marketUpdate.getTwoWayPrice().getState(),
                bidPrice,
                marketUpdate.getTwoWayPrice().getBidAmount(),
                officePrice,
                marketUpdate.getTwoWayPrice().getOfferAmount()
        );
    }

    List<MarketUpdate> getMarketUpdatesForInstruments(Market market) {
        return instrumentMap.get(Instrument.INSTRUMENT0).stream()
                .filter(VWAPTwoWayPrice -> VWAPTwoWayPrice.getMarket().equals(market))
                .collect(Collectors.toList());
    }

    double calculateBid(List<MarketUpdate> marketUpdates) {
        double sumBids = 0;
        double sumAmounts = 0;
        for (MarketUpdate marketUpdate : marketUpdates) {
            sumBids += marketUpdate.getTwoWayPrice().getBidPrice() * marketUpdate.getTwoWayPrice().getBidAmount();
            sumAmounts += marketUpdate.getTwoWayPrice().getBidAmount();
        }
        return sumBids / sumAmounts;
    }

    double calculateOffer(List<MarketUpdate> marketUpdates) {
        double sumOffers = 0;
        double sumAmounts = 0;
        for (MarketUpdate marketUpdate : marketUpdates) {
            sumOffers += marketUpdate.getTwoWayPrice().getOfferPrice() * marketUpdate.getTwoWayPrice().getOfferAmount();
            sumAmounts += marketUpdate.getTwoWayPrice().getBidAmount();
        }
        return sumOffers / sumAmounts;
    }
}
