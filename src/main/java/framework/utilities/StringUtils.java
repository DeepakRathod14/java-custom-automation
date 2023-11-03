package framework.utilities;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("StringUtils class");
    }
    
    /**
     * Returns formatted string in camelCaseStyle (or CamelCase).
     *
     * @param string - input string
     * @param firstWordToLowerCase - is first char in string should be in lover case
     */
    public static String toCamelCase(String string, boolean firstWordToLowerCase) {
        char previousChar = '\u0000';
        StringBuilder result = new StringBuilder();
        
        boolean firstLetterArrived = !firstWordToLowerCase;
        boolean nextLetterInUpperCase = true;
        char currentChar;
        for (int i = 0; i < string.length(); i++) {
            currentChar = string.charAt(i);
            
            if (!Character.isLetterOrDigit(currentChar)
                || (((Character.isLetter(previousChar) && Character.isLowerCase(previousChar))
                    || Character.isDigit(previousChar))
                    && Character.isLetter(currentChar) && Character.isUpperCase(currentChar))) {
                nextLetterInUpperCase = true;
                if (!Character.isLetterOrDigit(currentChar)) {
                    previousChar = currentChar;
                    continue;
                }
            }
            
            if (nextLetterInUpperCase && firstLetterArrived) {
                result.append(Character.toUpperCase(currentChar));
            } else {
                result.append(Character.toLowerCase(currentChar));
            }
            
            firstLetterArrived = true;
            nextLetterInUpperCase = false;
            previousChar = currentChar;
        }
        
        return result.toString();
    }
}
