package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.Booking;
import model.mo.User;
import model.mo.Structure;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public interface BookingDAO {

    /*
     * questi metodi rappresentano la business logic per l'oggetto BOOKING
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe BOOKING
     */

    /*
     * Based on the SHOWCASE flag, it lists all the products that must be shown in the showcase
     *
     * @return all the products that must be shown in the showcase of the homepage
     */

    Booking insert(LocalDate date, Time hourStart, User customer, Structure structure) throws DuplicatedObjectException;

    ArrayList<Booking> findBookingsByDate(LocalDate date);
}
