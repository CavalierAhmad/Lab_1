package algonquin.cst2335.alja0062;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "message")
    String message;
    @ColumnInfo(name = "Time")
    String time;
    @ColumnInfo(name = "IsSent")
    boolean isSent;

    public ChatMessage(String m, String t, boolean s){
        message = m;
        time = t;
        isSent = s;
    }

    public ChatMessage(int id, String message, String time, boolean sent) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.isSent = sent;
    }

    public String getMessage() {return message;}

    public String getTime() {return time;}

    public boolean isSent() {return isSent;}
}

//done