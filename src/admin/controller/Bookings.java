package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Bookings {


    public static void showBookings(HttpServletRequest request, HttpServletResponse response) {


        request.setAttribute("viewUrl", "admin/show-bookings");
    }










}
