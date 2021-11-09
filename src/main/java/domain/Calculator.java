package domain;

public interface Calculator {
    TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice);
}