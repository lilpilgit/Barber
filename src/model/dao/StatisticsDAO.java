package model.dao;

import javafx.util.Pair;
import model.exception.DuplicatedObjectException;
import model.mo.Booking;
import model.mo.Structure;
import model.mo.User;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public interface StatisticsDAO {

    Pair<Double, Double> totalEarningsWithAndWithoutDiscount();
}
