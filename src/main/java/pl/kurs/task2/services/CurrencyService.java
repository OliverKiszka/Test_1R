package pl.kurs.task2.services;

import pl.kurs.task2.models.ExchangeRate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyService {
    private static final long CACHE_EXPIRATION_TIME = 10_000;
    private final Map<String, ExchangeRate> exchangeRateCache = new ConcurrentHashMap<>();
    private final IExchangeRateDownloader exchangeRateDownloader;

    public CurrencyService(IExchangeRateDownloader exchangeRateDownloader) {
        this.exchangeRateDownloader = exchangeRateDownloader;
    }

    public double exchange(String currencyFrom, String currencyTo, double amount) {
        String cacheKey = currencyFrom + "->" + currencyTo;
        long currentTime = System.currentTimeMillis();

        synchronized (exchangeRateCache) {
            ExchangeRate cachedRate = exchangeRateCache.get(cacheKey);
            if (cachedRate != null && (currentTime - cachedRate.getTimestamp()) < CACHE_EXPIRATION_TIME) {
                return amount * cachedRate.getRate();
            }
            double rate = exchangeRateDownloader.downloadExchangeRate(currencyFrom, currencyTo);
            exchangeRateCache.put(cacheKey, new ExchangeRate(rate, currentTime));
            return amount * rate;
        }
    }


}

