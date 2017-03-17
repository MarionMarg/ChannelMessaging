package marion.marguerettaz.channelmessaging;

/**
 * Created by marguerm on 27/01/2017.
 */
public class Message {
    public final int userID;
    public final String message;
    public final String username;
    public final String date;
    public final String imageUrl;
    public final String messageImageUrl;

    public Message(int userID, String message, String date, String imageUrl, String username, String messageImageUrl) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.username = username;
        this.messageImageUrl = messageImageUrl;
    }
}
