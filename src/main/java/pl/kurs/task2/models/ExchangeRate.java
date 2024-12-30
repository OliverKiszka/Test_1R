package pl.kurs.task2.models;

public class ExchangeRate {
    private final double rate;
    private final long timestamp;

    public ExchangeRate(double rate, long timestamp) {
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public double getRate() {
        return rate;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
