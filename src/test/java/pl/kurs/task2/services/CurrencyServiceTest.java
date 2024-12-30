package pl.kurs.task2.services;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class CurrencyServiceTest {

    private CurrencyService currencyService;
    private IExchangeRateDownloader exchangeRateDownloader;
    private String testCurrency1;
    private String testCurrency2;
    private String testCurrency3;
    private String testCurrency4;
    private double amount;
    @Before
    public void init(){
        exchangeRateDownloader = Mockito.mock(IExchangeRateDownloader.class);
        currencyService = new CurrencyService(exchangeRateDownloader);

        testCurrency1 = "USD";
        testCurrency2 = "EUR";
        testCurrency3 = "PLN";
        testCurrency4 = "THB";
        amount = 100;
    }
    @Test
    public void shouldUseCachedExchangeRateInExpectedExpirationTime(){
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency1,testCurrency2)).thenReturn(0.95);

        double firstResult = currencyService.exchange(testCurrency1,testCurrency2,amount);
        Assertions.assertThat(firstResult).isEqualTo(95.0,Assertions.within(0.0001));

        double secondResult = currencyService.exchange(testCurrency1,testCurrency2,amount);
        Assertions.assertThat(secondResult).isEqualTo(95.0,Assertions.within(0.0001));

        Mockito.verify(exchangeRateDownloader, Mockito.times(1)).downloadExchangeRate(testCurrency1,testCurrency2);
    }

    @Test
    public void shouldDownloadNewExchangeRateAfterCacheExpires() throws InterruptedException {
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency1,testCurrency2))
                .thenReturn(0.95)
                .thenReturn(0.90);

        double firstResult = currencyService.exchange(testCurrency1,testCurrency2,amount);
        Assertions.assertThat(firstResult).isEqualTo(95.0,Assertions.within(0.0001));

        Thread.sleep(11_000);

        double secondResult = currencyService.exchange(testCurrency1,testCurrency2,amount);
        Assertions.assertThat(secondResult).isEqualTo(90.0,Assertions.within(0.0001));


        Mockito.verify(exchangeRateDownloader,Mockito.times(2)).downloadExchangeRate(testCurrency1,testCurrency2);

    }

    @Test
    public void shouldExchangeMultiplePairsOfCurrencyIndependently(){
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency1,testCurrency2)).thenReturn(0.95);
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency3,testCurrency4)).thenReturn(8.30);

        double usdToEur = currencyService.exchange(testCurrency1, testCurrency2, 100);
        double plnToThb = currencyService.exchange(testCurrency3, testCurrency4, 100);

        Assertions.assertThat(usdToEur).isEqualTo(95.0, Assertions.within(0.0001));
        Assertions.assertThat(plnToThb).isEqualTo(830.0,Assertions.within(0.0001));

        Mockito.verify(exchangeRateDownloader,Mockito.times(1)).downloadExchangeRate(testCurrency1,testCurrency2);
        Mockito.verify(exchangeRateDownloader,Mockito.times(1)).downloadExchangeRate(testCurrency3,testCurrency4);

    }

    @Test
    public void shouldReturnZeroWhenAmountIsZero(){
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency1,testCurrency2)).thenReturn(0.95);

        double result = currencyService.exchange(testCurrency1,testCurrency2,0);
        Assertions.assertThat(result).isEqualTo(0);

        Mockito.verify(exchangeRateDownloader, Mockito.times(1)).downloadExchangeRate(testCurrency1,testCurrency2);

    }

    @Test
    public void shouldBeThreadSafeWhenCallingDownloadExchangeRate() throws InterruptedException {
        Mockito.when(exchangeRateDownloader.downloadExchangeRate(testCurrency1,testCurrency2)).thenReturn(0.95);

        Runnable exchangeTask = ()->{
            double result = currencyService.exchange(testCurrency1,testCurrency2,amount);
            Assertions.assertThat(result).isEqualTo(95.0,Assertions.within(0.0001));
        };
        Thread t1 = new Thread(exchangeTask);
        Thread t2 = new Thread(exchangeTask);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Mockito.verify(exchangeRateDownloader,Mockito.times(1)).downloadExchangeRate(testCurrency1,testCurrency2);


    }

}