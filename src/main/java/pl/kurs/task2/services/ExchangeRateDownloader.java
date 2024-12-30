package pl.kurs.task2.services;

import java.util.Random;

public class ExchangeRateDownloader implements IExchangeRateDownloader{
    @Override
    public double downloadExchangeRate(String currencyFrom, String currencyTo) {

        //symulacja pobierania kursów ze strony internetowej

        System.out.println("Downloaded exchange rate for " + currencyFrom + " to " + currencyTo);

        Random random = new Random();
        return random.nextDouble() * 5;
    }
}
