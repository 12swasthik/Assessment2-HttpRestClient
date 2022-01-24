package data;


import com.google.gson.Gson;
import org.json.JSONObject;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;

    private User(int id, String email, String firstName, String lastName, String avatar) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }
    public static User createUser(JSONObject obj){
        return new User(obj.getInt("id"),obj.getString("email"),obj.getString("first_name"),obj.getString("last_name"),obj.getString("avatar"));
    }


    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
