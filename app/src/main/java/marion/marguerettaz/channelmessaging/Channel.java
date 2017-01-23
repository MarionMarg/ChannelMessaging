package marion.marguerettaz.channelmessaging;

/**
 * Created by marguerm on 23/01/2017.
 */
public class Channel {

    public final int channelID;
    public final String name;
    public final int connectedusers;


    public Channel(int channelID, String name, int connectedusers) {
        this.channelID = channelID;
        this.name = name;
        this.connectedusers = connectedusers;
    }
}
