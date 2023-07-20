package algonquin.cst2335.alja0062;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    String message;
    @ColumnInfo(name="time")
    String time;
    @ColumnInfo(name="IsSent")
    boolean isSent;
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    ChatMessage(String m, String t, boolean s){
        message = m;
        time = t;
        isSent = s;
    }

    public String getMessage() {return message;}

    public String getTime() {return time;}

    public boolean isSent() {return isSent;}
}
