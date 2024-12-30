package pl.kurs.task2.services;

public interface IExchangeRateDownloader {
    double downloadExchangeRate(String currencyFrom, String currencyTo);
}
