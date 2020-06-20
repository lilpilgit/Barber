package model.mo;

import java.sql.Time;
import java.util.HashMap;

public class StatisticsEarnings {
    /* Totale dei guadagni fatti considerando gli sconti applicati */
    private Double totEarningsWithDiscount;
    /* Totale dei guadagni che si sarebbero fatti non considerando gli sconti applicati */
    private Double totEarningsWithoutDiscount;
    /* Perdita di guadagni data dalla differenza di totEarningsWithoutDiscount e totEarningsWithDiscount */
    private Double lostGain;

    /* Per ogni ora di inizio di un appuntamento, il numero totale di appuntamenti richiesti */
    private HashMap<Time,Integer> totAppointmentForHourStart;

    public StatisticsEarnings(){
        this.totAppointmentForHourStart = new HashMap<Time,Integer>();
        this.totEarningsWithDiscount = 0D;
        this.totEarningsWithoutDiscount = 0D;
        this.lostGain = 0D;
    }

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

    public HashMap<Time, Integer> getTotAppointmentForHourStart() {
        return totAppointmentForHourStart;
    }

    public void setTotAppointmentForHourStart(HashMap<Time, Integer> totAppointmentForHourStart) {
        this.totAppointmentForHourStart = totAppointmentForHourStart;
    }
}
