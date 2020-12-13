package es.cifpcm.galeriaimagenesdeniz;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author elper
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, //2MB
        maxFileSize = 1024 * 1024 * 10, //10MB
        maxRequestSize = 1024 * 1024 * 50)    //50MB
public class ImageManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = request.getServletContext();
        String fullPath = context.getRealPath("/uploadsdeniz");

        // creates the save directory if it does not exists
        File fileSaveDir = new File(fullPath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            part.write(fullPath + File.separator + fileName);
            System.out.println(fullPath + File.separator + fileName);
        }
        request.setAttribute("mensaje", "El archivo se ha subido correctamente");
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = request.getServletContext();
        String fullPath = context.getRealPath("/uploadsdeniz");

        File folder = new File(fullPath);
        File[] listOfFiles = folder.listFiles();
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Galería de imágenes</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Lista de imágenes</h1>");

        for (File file : listOfFiles) {          
            if (file.isFile()) {
                String direccion = "/uploadsdeniz" + File.separator + file.getName();
                out.println("<a href='." + direccion + "'" + ">" + file.getName() + "</a>" + "<br>");
            }

        }
        out.println("<br><a href=\"./index.html\">Volver</a>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImageManagerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ImageManagerServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }

    }
}
