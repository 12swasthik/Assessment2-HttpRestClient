package service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResultWriter {

    private static ResultWriter writer = null;
    private ResultWriter(){}

    public static ResultWriter getInstance(){
        if(writer == null) writer = new ResultWriter();
        return writer;
    }

    public synchronized void outputResult(PrintWriter out, HttpServletResponse resp, String response){
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(response);
        out.flush();
    }
}
