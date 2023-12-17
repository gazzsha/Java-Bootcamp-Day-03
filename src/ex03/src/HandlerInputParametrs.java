import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class HandlerInputParametrs {

    final static String KEY_WORD = "^--threadsCount=\\d+$";

    static String Handler(String argc) {
        Pattern pattern = Pattern.compile(KEY_WORD);
        Matcher matcher = pattern.matcher(argc);
        if (matcher.matches()) {
            return matcher.group(0).substring("--threadsCount=".length());
        } else throw new RuntimeException("Wrong input!");
    }
}
