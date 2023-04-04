package App;

import java.util.Collections;

public class Util {

    public static String repeatString(String str, int n) {
        return String.join("", Collections.nCopies(n, str));
    }

}
