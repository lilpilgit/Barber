package home.dispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(name = "Dispatcher", urlPatterns = {"/app"})
public class Dispatcher extends HttpServlet {
    String controllerAction = null;
    PrintWriter out = null;
    Method controllerMethod = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonOperations(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonOperations(request, response);
    }

    protected void commonOperations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        controllerAction = request.getParameter("controllerAction");
        System.err.println("DISPATCHER HOME ==> controllerAction : " + controllerAction);

        if (controllerAction == null)
            controllerAction = "Home.view";

        String[] splittedAction = controllerAction.split("\\.");
        Class<?> controllerClass = null;
        try {
            controllerClass = Class.forName("home.controller." + splittedAction[0]);
            System.err.println("DISPATCHER HOME ==> controllerClass :" + controllerClass.getName());
            //  aggiungo il nome del package ^^^^^^^^^^^^^^^^
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found Exception ==> CARICAMENTO DELLA CONTROLLER CLASS NEL DISPATCHER HOME");
            e.printStackTrace();
        }

        try {
            controllerMethod = controllerClass.getMethod(splittedAction[1], HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            System.err.println("No Such Method Exception ==> CARICAMENTO DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER HOME");
            e.printStackTrace();
        }

        try {
            controllerMethod.invoke(null, request, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Invocation Target Exception ==> INVOCAZIONE DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER HOME");
            e.printStackTrace();
        }
        /*Dopo aver invocato il metodo corrispondente per ogni HTTP request e aver comunicato
         * al Dispatcher quale JSP mostrare tramite l'attributo viewUrl, recepisco i dati settati*/

        String viewUrl = (String) request.getAttribute("viewUrl");
        RequestDispatcher jspToShow = request.getRequestDispatcher("jsp/" + viewUrl + ".jsp");
        /*^^^^^^^^^^^^^^^ viene recuperato un nuovo oggetto RequestDispatcher associato alla nuova risorsa
         *  1) Il forward è un’operazione interna al server o, nel caso di più server coinvolti, è un’operazione server-to-server.
         *  2) Il browser è completamente inconsapevole che l’operazione ha avuto luogo, quindi il suo URL originale rimane intatto.
         *  3) Qualsiasi ricaricamento del browser nella pagina risultante ripeterà semplicemente la richiesta originale, con l’URL originale.
         * */

        try {
            jspToShow.forward(request, response); /*LINK INTERESSANTE => https://www.javaboss.it/redirect-vs-forward/*/
        } catch (ServletException e) {
            System.err.println("Servlet Exception ==> FORWARD VERSO LA JSP NEL DISPATCHER HOME");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
