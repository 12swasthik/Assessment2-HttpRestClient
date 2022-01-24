package servlet;

import org.json.JSONObject;
import service.ResultWriter;
import service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/search")
public class SearchServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String param = req.getParameter("q");
        String page = req.getParameter("page");
        try {
            JSONObject users = UserService.search(param, page);
            ResultWriter.getInstance().outputResult(resp.getWriter(), resp, users.toString(3));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ResultWriter.getInstance().outputResult(resp.getWriter(), resp, "error : " + e);
            }
            catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }
}
