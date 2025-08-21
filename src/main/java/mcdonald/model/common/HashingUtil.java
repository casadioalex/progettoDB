package mcdonald.model.common;

import org.mindrot.jbcrypt.BCrypt;

public class HashingUtil {

    private HashingUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

}
