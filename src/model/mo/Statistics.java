package model.mo;

public class Statistics {
    /* Totale dei guadagni fatti considerando gli sconti applicati */
    private Double totEarningsWithDiscount;
    /* Totale dei guadagni che si sarebbero fatti non considerando gli sconti applicati */
    private Double totEarningsWithoutDiscount;
    /* Perdita di guadagni data dalla differenza di totEarningsWithoutDiscount e totEarningsWithDiscount */
    private Double lostGain;

    public Double getTotEarningsWithDiscount() {
        return totEarningsWithDiscount;
    }

    public void setTotEarningsWithDiscount(Double totEarningsWithDiscount) {
        this.totEarningsWithDiscount = totEarningsWithDiscount;
    }

    public Double getTotEarningsWithoutDiscount() {
        return totEarningsWithoutDiscount;
    }

    public void setTotEarningsWithoutDiscount(Double totEarningsWithoutDiscount) {
        this.totEarningsWithoutDiscount = totEarningsWithoutDiscount;
    }

    public Double getLostGain() {
        return lostGain;
    }

    public void setLostGain(Double lostGain) {
        this.lostGain = lostGain;
    }
}
