package botrestapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sinakashipazha on 2/27/2017 AD.
 */

public class Token {
    static {
        String tempToken = null;
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/token"))) {
            tempToken = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        token = tempToken;
    }

    private final static String token ;

    public static String getToken(){
        return token;
    }
}
