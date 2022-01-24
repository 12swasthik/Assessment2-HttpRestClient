package myexeptions;

public class NetworkException extends Exception{
    private final String msg;
    public NetworkException(String s){this.msg = s;}

    @Override
    public String toString(){
        return msg;
    }
}
