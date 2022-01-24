package myexeptions;

public class PageNumberException extends Exception {
    private final String msg;
    public PageNumberException(String s){this.msg = s;}

    @Override
    public String toString(){
        return msg;
    }
}
