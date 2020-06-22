package dispatcher;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet(name = "Dispatcher", urlPatterns = {"/app"})
public class Dispatcher extends HttpServlet {
    String controllerAction = null;
    Method controllerMethod = null;
    boolean errorPage = false;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonOperations(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonOperations(request, response);
    }

    protected void commonOperations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        boolean isMultipart = ServletFileUpload.isMultipartContent(request); /* true se è stato submittato un form di tipo multipart/form-data*/

        if (!isMultipart) {
            /* se non è una multipart tratto tale chiamata HTTP come tutte quelle trattate fin ora */
            controllerAction = request.getParameter("controllerAction");
            System.err.println("DISPATCHER ==> controllerAction nel caso NO MULTIPART : " + controllerAction);
        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory(); /* serve a creare tutti i gestori per i file che arrivano in upload */
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = null; /* vado a fare il parsing della request e ottengo una lista di FileItem ( che fa parte anche essa delle librerie di FileUpload)*/
            try {
                items = upload.parseRequest(request); /* Dentro items ho sia i parametri HTTP che i file binari */
            } catch (FileUploadException e) {
                System.err.println("DISPATCHER ==> Errore nella items = upload.parseRequest(request);");
                e.printStackTrace();
                errorPage = true;
            }
            /*Questa istruzione è però distruttiva per la request in quanto la posso fare solo una volta e dopo di chè dentro items
             *ho sia i file che i parametri HTTP normali dunque è evidente che devo trattarli in modo diverso, in particolare devo
             *salvare nella request come attributi proprio questa lista di items che contiene sia i files che i parametri HTTP */

            request.setAttribute("attributesMultipart", items); /*inserisco come attributi della request gli items*/

            for (FileItem item : items) { /*per ciascuno degli items verifico se si tratta di un file o di un form field*/
                if (item.isFormField() && item.getFieldName().equals("controllerAction")) {
                    controllerAction = item.getString(); /*riempio controllerAction solo dopo aver trovato il formField il cui nome è controllerAction*/
                    System.err.println("DISPATCHER ==> controllerAction yes multipart : " + controllerAction);
                }
            }
        }//fine ELSE

        /* chiamate AJAX */
        if (controllerAction != null && controllerAction.equals("home.Cart.changeDesiredQuantity")) {

            forward(request, response, "jsp/customer/ajax-change-desired-qty.jsp");

        } else if (controllerAction != null && controllerAction.equals("home.Book.reservedSlot")) {

            forward(request, response, "jsp/customer/ajax-times-searcher.jsp");

        } else if (controllerAction != null && controllerAction.equals("home.Book.getBooking")) {

            forward(request, response, "jsp/customer/ajax-get-booking.jsp");

        }
        /* chiamate normali ( NON AJAX ) */
        else {

            if (controllerAction == null) {
                controllerAction = "home.Home.view";
            }
            /* CARICO LA CLASSE DA CUI RICHIAMARE IL METODO */
            String[] splittedAction = controllerAction.split("\\.");
            Class<?> controllerClass = null;
            try {
                controllerClass = Class.forName(splittedAction[0] + ".controller." + splittedAction[1]);
                System.err.println("DISPATCHER  ==> controllerClass :" + controllerClass.getName());
                //  aggiungo il nome del package ^^^^^^^^^^^^^^^^
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found Exception ==> CARICAMENTO DELLA CONTROLLER CLASS NEL DISPATCHER");
                e.printStackTrace();
                errorPage = true;
            }

            /* INVOCO IL METODO */

            try {
                controllerMethod = controllerClass.getMethod(splittedAction[2], HttpServletRequest.class, HttpServletResponse.class);
                System.err.println("DISPATCHER  ==> controllerMethod :" + controllerMethod.getName());
            } catch (NoSuchMethodException e) {
                System.err.println("No Such Method Exception ==> CARICAMENTO DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER");
                e.printStackTrace();
                errorPage = true;
            }

            try {
                controllerMethod.invoke(null, request, response);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                errorPage = true;
            } catch (InvocationTargetException e) {
                System.err.println("Invocation Target Exception ==> INVOCAZIONE DEL METODO DELLA CONTROLLER CLASS NEL DISPATCHER");
                e.printStackTrace();
                errorPage = true;
            }

            /*Dopo aver invocato il metodo corrispondente per ogni HTTP request e aver comunicato
             * al DispatcherAdmin quale JSP mostrare tramite l'attributo viewUrl, recepisco i dati settati*/

            String viewUrl = (String) request.getAttribute("viewUrl");
            System.out.println("viewUrl ==> " + viewUrl);

            if (errorPage) { /* deve essere mostrata la pagina di errore... è successo qualcosa di brutto sicuramente... */

                errorPage = false; /* altrimenti rimane sempre a true anche se il problema che ha scatenato l'errore viene fixato si continua a vedere la pagina sbagliata*/

                forward(request, response, "jsp/error/404.jsp");

            } else if (viewUrl != null) { /* passata con il controller */
                forward(request, response, "jsp/" + viewUrl + ".jsp");

            } else {
                System.err.println("viewUrl È null!!!");
                /* redirigo sulla home.jsp */
                forward(request, response, "jsp/home.jsp");
            }
        }
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String relativePathJSP) {
        RequestDispatcher jsp = request.getRequestDispatcher(relativePathJSP);
        System.out.println("jspToShow ==> " + jsp);
        try {
            jsp.forward(request, response);
        } catch (ServletException | IOException e) {
            System.err.println("Servlet Exception ==> FORWARD VERSO LA JSP NEL DISPATCHER");
            e.printStackTrace();
            errorPage = true;
        }
    }
}
