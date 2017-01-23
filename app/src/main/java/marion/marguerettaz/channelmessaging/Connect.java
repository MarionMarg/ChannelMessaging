package marion.marguerettaz.channelmessaging;

/**
 * Created by marguerm on 20/01/2017.
 */
public class Connect {

    public final String response;
    public final Integer code;
    public final String accesstoken;


    public Connect(String response, Integer code, String accesstoken) {
        this.response = response;
        this.code = code;
        this.accesstoken = accesstoken;
    }
}
