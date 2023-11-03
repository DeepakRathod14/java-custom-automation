package framework.utilities.random;

import java.security.SecureRandom;

public class RandomEnum {
    
    private RandomEnum() {
        //Private constructor
    }
    
    private static final SecureRandom random = new SecureRandom();
    
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
