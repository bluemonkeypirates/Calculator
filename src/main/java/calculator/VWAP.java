package calculator;

import domain.Calculator;
import domain.Instrument;
import domain.Market;
import domain.MarketUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VWAP implements Calculator {
    private final Map<Instrument, Map<Market, MarketUpdate>> instrumentMarketUpdates = new HashMap<>();

    @Override
    public VWAPTwoWayPrice applyMarketUpdate(MarketUpdate marketUpdate) {
        Instrument instrument = marketUpdate.getTwoWayPrice().getInstrument();
        // only keep the latest market update for each instrument-market pair
        instrumentMarketUpdates.computeIfAbsent(instrument, k -> new HashMap<>()).put(marketUpdate.getMarket(), marketUpdate);
        List<MarketUpdate> marketUpdates = instrumentMarketUpdates.get(instrument).values().stream().toList();

        CalculatedBid calculatedBid = new CalculatedBid(0,0);
        CalculatedOffer calculatedOffer = new CalculatedOffer(0,0);

        if (marketUpdate.getTwoWayPrice().getBidAmount() > 0) {
            //only process bid if there is an amount present
            calculatedBid = calculateBid(marketUpdates);
        }
        if (marketUpdate.getTwoWayPrice().getOfferAmount() > 0) {
            //only process bid if there is an amount present
            calculatedOffer = calculateOffer(marketUpdates);
        }

        //assume the state is the same as the update received
        return new VWAPTwoWayPrice(
                instrument,
                marketUpdate.getTwoWayPrice().getState(),
                calculatedBid.getBid(),
                calculatedBid.getSumAmounts(),
                calculatedOffer.getOffer(),
                calculatedOffer.getSumAmounts()
        );
    }

    CalculatedBid calculateBid(List<MarketUpdate> marketUpdates) {
        double sumBids = 0;
        double sumAmounts = 0;
        for (MarketUpdate marketUpdate : marketUpdates) {
            sumBids += marketUpdate.getTwoWayPrice().getBidPrice() * marketUpdate.getTwoWayPrice().getBidAmount();
            sumAmounts += marketUpdate.getTwoWayPrice().getBidAmount();
        }
        double bid = sumBids/sumAmounts;
        return new CalculatedBid(bid, sumAmounts);
    }

    CalculatedOffer calculateOffer(List<MarketUpdate> marketUpdates) {
        double sumOffers = 0;
        double sumAmounts = 0;
        for (MarketUpdate marketUpdate : marketUpdates) {
            sumOffers += marketUpdate.getTwoWayPrice().getOfferPrice() * marketUpdate.getTwoWayPrice().getOfferAmount();
            sumAmounts += marketUpdate.getTwoWayPrice().getOfferAmount();
        }
        double offer = sumOffers /sumAmounts;
        return new CalculatedOffer(offer, sumAmounts);
    }
}
