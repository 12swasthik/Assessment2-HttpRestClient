package service;

import data.User;
import myexeptions.NetworkException;
import myexeptions.PageNumberException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class UserService {
    private static final int MAX_PER_PAGE = 3;
    private static final String TOTAL_PAGES_LITERAL = "total_pages";
    private static final String USERS_LITERAL = "users";
    private UserService() {
    }

    private static JSONObject getJsonResponse(String param,String method) throws IOException, NetworkException {
        BufferedReader br;
        String line;
        StringBuilder response = new StringBuilder();
        URL url = new URL(param);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (HTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.setUseCaches(false);
        connection.setRequestMethod(method);
        int status = connection.getResponseCode();
        if (status == 200) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        if (status != 200) {
            throw new NetworkException(response.toString());
        }
        return new JSONObject(response.toString());
    }


    public static JSONObject getUsers(String pageNumber) throws IOException, NetworkException, PageNumberException {
        JSONObject response = new JSONObject();
        JSONObject json = getJsonResponse("https://reqres.in/api/users?page=" + pageNumber,"GET");
        int totalPages = json.getInt(TOTAL_PAGES_LITERAL);
        if (Integer.parseInt(pageNumber) <= totalPages && Integer.parseInt(pageNumber) >= 1) {
            response.put("page", json.getInt("page"));
            response.put("total_users", json.getInt("total"));
            response.put(TOTAL_PAGES_LITERAL, totalPages);
            response.put("users_per_page", json.getInt("per_page"));
            response.put(USERS_LITERAL, json.getJSONArray("data"));
            return response;
        } else {
            throw new PageNumberException("page number is invalid, page number between 1 and "+totalPages);
        }
    }

    public static JSONObject search(String query,String pagequery) throws IOException, NetworkException, PageNumberException {
        if(query == null || query.equals(""))
            throw new IllegalArgumentException("No query found");
        if(pagequery == null || Objects.equals(pagequery, "") || " ".equals(pagequery)) pagequery = "1";
        query = query.toLowerCase();

        JSONObject response = new JSONObject();
        ArrayList<User> users = new ArrayList<>();

        int totalPages = getJsonResponse("https://reqres.in/api/users","GET").getInt(TOTAL_PAGES_LITERAL);

        createUserList(users,query,totalPages);

        response.put("total_users",users.size());
        if(users.size() > MAX_PER_PAGE){
            paginateResponse(response,pagequery,users);
        }
        else {
            for (User user : users) response.append(USERS_LITERAL, new JSONObject(user.toString()));
            response.put(TOTAL_PAGES_LITERAL,1);
            response.put("page",1);
        }
        return response;
    }

    private static void createUserList(ArrayList<User> users,String query,int totalPages) throws IOException, NetworkException {
        for (int page = 1; page <= totalPages; page++) {
            JSONObject pageResponse = getJsonResponse("https://reqres.in/api/users?page=" + page,"GET");
            JSONArray usersArray = pageResponse.getJSONArray("data");
            for (int index = 0; index < usersArray.length(); index++) {
                JSONObject user = usersArray.getJSONObject(index);
                String email = user.getString("email").toLowerCase();
                String name = user.getString("first_name") + ' ' + user.getString("last_name").toLowerCase();
                if (email.contains(query) || name.contains(query))
                    users.add(User.createUser(user));
            }
        }
    }
    private static void paginateResponse(JSONObject response,String pagequery,ArrayList<User> users) throws PageNumberException {
        int pageNumber = Integer.parseInt(pagequery);
        int startIndex = MAX_PER_PAGE*(pageNumber-1);
        int endIndex = Math.min(users.size(),startIndex+MAX_PER_PAGE);
        int allPages = users.size() / MAX_PER_PAGE;
        if(users.size()%MAX_PER_PAGE !=0) allPages+=1;
        if(pageNumber > allPages || pageNumber <=0) throw new PageNumberException("page number out of range, should be between 1 and "+allPages);
        for(int i=startIndex;i<endIndex;i++){
            response.append(USERS_LITERAL,new JSONObject(users.get(i).toString()));
        }
        response.put(TOTAL_PAGES_LITERAL,allPages);
        response.put("page",pageNumber);
    }
}

