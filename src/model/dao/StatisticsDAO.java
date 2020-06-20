package model.dao;

import model.mo.StatisticsEarnings;

import java.sql.Time;
import java.time.LocalDate;
import java.util.TreeMap;

public interface StatisticsDAO {

    StatisticsEarnings totalEarningsWithAndWithoutDiscount();

    TreeMap<Time,Integer> totalAppointmentGroupByHourStartInAYear(LocalDate date_for_year);
}
