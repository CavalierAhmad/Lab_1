package algonquin.cst2335.alja0062;

public class ChatMessage {
    String message;
    String time;
    boolean isSent;

    ChatMessage(String m, String t, boolean s){
        message = m;
        time = t;
        isSent = s;
    }

    public String getMessage() {return message;}

    public String getTime() {return time;}

    public boolean isSent() {return isSent;}
}
