package suman.dev.strocks.Constant;

/**
 * Created by Node1 on 7/20/2017.
 */

public class Token {
    public static String access_token;
    public static long expires_in;
    public static String token_type;
    public static String scope;
    public static String refresh_token;

    public static String AuthenticationCode()
    {
        return "Bearer "+access_token;
    }

    public static boolean isLoggedIn= false;

    public static void destroy()
    {
        access_token= null;
        expires_in=0;
        token_type = null;
        scope = null;
        refresh_token = null;
        isLoggedIn = false;
    }
}
