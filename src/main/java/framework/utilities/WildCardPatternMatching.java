package framework.utilities;

import java.util.List;

public class WildCardPatternMatching {

    private WildCardPatternMatching() {
        //empty constructor
    }

    /**
     * Test given string content for at least one occurrence against list of patterns.
     *
     * @param content a content in any format, for example 172.28.0.1
     * @param patterns list of patterns, a single a pattern to match against content, for example *172.*.0.*
     * @return true if any pattern matches
     */
    public static boolean traverseMatch(String content, List<String> patterns) {
        for (String pattern: patterns) {
            if (isMatch(content, pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Match given string by pattern.
     *
     * @param content a content in any format, for example 172.28.0.1
     * @param pattern a pattern to match against content, for example *172.*.0.*
     * @return return true if pattern matches
     */
    public static boolean isMatch(String content, String pattern) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iindex = -1;

        while (i < content.length()) {
            if (j < pattern.length() && (pattern.charAt(j) == '?' || pattern.charAt(j) == content.charAt(i))) {
                ++i;
                ++j;
            } else if (j < pattern.length() && pattern.charAt(j) == '*') {
                starIndex = j;
                iindex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iindex + 1;
                iindex++;
            } else {
                return false;
            }
        }
        while (j < pattern.length() && pattern.charAt(j) == '*') {
            ++j;
        }
        return j == pattern.length();
    }

}
