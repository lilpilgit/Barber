package admin.dispatcher;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
import java.util.List;

@WebServlet(name = "DispatcherAdmin", urlPatterns = {"/manage"})
public class DispatcherAdmin extends HttpServlet {
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

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            /*se non è una multipart tratto tale chiamata HTTP come tutte quelle trattate fin ora*/
            controllerAction = request.getParameter("controllerAction");
            System.err.println("DISPATCHER ==> controllerAction no multipart : " + controllerAction);
        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory(); /*serve a creare tutti i gestori per i file che arrivano in upload */
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null; /* vado a fare il parsing della request e ottengo una lista di FileItem ( che fa parte anche essa delle librerie di FileUpload)*/
            try {
                items = upload.parseRequest(request);
            } catch (FileUploadException e) {
                System.err.println("DISPATCHER ==> Errore nella items = upload.parseRequest(request);");
                e.printStackTrace();
            }
            /*Questa istruzione è però distruttiva per la request in quanto la posso fare solo una volta e dopo di chè dentro items
             *ho sia i file che i parametri HTTP normali dunque è evidente che devo trattarli in modo diverso, in particolare devo
             *salvare nella request come attributi proprio questa lista di items che contiene sia i files che i parametri HTTP */

            request.setAttribute("items", items); /*inserisco come attributi della request gli items*/

            for (FileItem item : items) { /*per ciascuno degli items verifico se si tratta di un file o di un form field*/
                if (item.isFormField() && item.getFieldName().equals("controllerAction")) {
                    controllerAction = item.getString(); /*riempio controllerAction solo dopo aver trovato il formField il cui nome è controllerAction*/
                    System.err.println("DISPATCHER ==> controllerAction yes multipart : " + controllerAction);
                }
            }
        }//fine ELSE

        if (controllerAction == null)
            controllerAction = "Welcome.welcome";

        String[] splittedAction = controllerAction.split("\\.");
        Class<?> controllerClass = null;
        try {
            controllerClass = Class.forName("admin.controller." + splittedAction[0]);
            System.err.println("DISPATCHER MANAGE ==> controllerClass :" + controllerClass.getName());
            //  aggiungo il nome del package ^^^^^^^^^^^^^^^^
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found Exception ==> CARICAMENTO DELLA CONTROLLER CLASS NEL DISPATCHER MANAGE");
            e.printStackTrace();
        }

        try {
            controllerMethod = controllerClass.getMethod(splittedAction[1], HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            System.err.println("No Such Method Exception ==> CARICAMENTO DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER MANAGE");
            e.printStackTrace();
        }

        try {
            controllerMethod.invoke(null, request, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Invocation Target Exception ==> INVOCAZIONE DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER MANAGE");
            e.printStackTrace();
        }
        /*Dopo aver invocato il metodo corrispondente per ogni HTTP request e aver comunicato
         * al DispatcherAdmin quale JSP mostrare tramite l'attributo viewUrl, recepisco i dati settati*/

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
            System.err.println("Servlet Exception ==> FORWARD VERSO LA JSP NEL DISPATCHER");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
