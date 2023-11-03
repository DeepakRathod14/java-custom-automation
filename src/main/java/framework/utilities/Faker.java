package framework.utilities;

import com.fasterxml.uuid.Generators;
import com.google.common.collect.Lists;

import framework.utilities.primitive.RandomValueGeneratorProvider;
import framework.utilities.primitive.generators.BooleanGenerator;
import framework.utilities.primitive.generators.DoubleGenerator;
import framework.utilities.primitive.generators.FloatGenerator;
import framework.utilities.primitive.generators.IntegerGenerator;
import framework.utilities.primitive.generators.LongGenerator;
import framework.utilities.primitive.generators.StringGenerator;
import framework.utilities.primitive.types.Types;
import framework.utilities.random.RandomHelper;
import framework.utilities.timestampbuilder.TimeConstants;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * The type Faker.
 */
public class Faker {

    private static final Logger LOGGER = LogManager.getLogger();

    private Faker() {
        //default constructor
    }

    private static final int NUMBERS_LENGTH = 10;

    /**
     * Generate date string.
     *
     * @return the string
     */
    public static String generateDate() {
        DateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
        Date date = new Date();
        return sdf.format(date);
    }


    /**
     * Generate date string.
     *
     * @param dateFormat the date format
     * @return the string
     */
    public static String generateDate(String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(calendar.getTime());
    }


    /**
     * Generate date minus seconds string.
     *
     * @param dateFormat the date format
     * @param seconds the seconds
     * @return the string
     */
    public static String generateDateMinusSeconds(String dateFormat, Integer seconds) {
        Date date = generateDateTimeNowMinusSeconds(seconds);
        DateFormat sdf = new SimpleDateFormat(dateFormat);

        return sdf.format(date);
    }

    /**
     * Generate date minus days string.
     *
     * @param dateFormat the date format
     * @param days the days
     * @return the string
     */
    public static String generateDateMinusDays(String dateFormat, Integer days) {
        Date date = generateTimeNowMinusDays(days);
        DateFormat sdf = new SimpleDateFormat(dateFormat);

        return sdf.format(date);
    }

    /**
     * Generate date plus days string.
     *
     * @param dateFormat the date format
     * @param days the days
     * @return the string
     */
    public static String generateDatePlusDays(String dateFormat, Integer days) {
        Date date = generateTimeNowPlusDays(days);
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * Random location string.
     *
     * @return the string
     */
    public static String randomLocation() {
        return randomObjectFromArray(TimeZone.getAvailableIDs());
    }

    /**
     * Random time zone string.
     *
     * @return the string
     */
    public static String randomTimeZone() {
        return randomObjectFromList(ZoneId.getAvailableZoneIds()
            .stream()
            .sorted()
            .collect(Collectors.toList()));
    }

    /**
     * Generate time zone utc string.
     *
     * @return the string
     */
    public static String generateTimeZoneUtc() {
        return ZoneId.of("Etc/UTC").toString();
    }

    /**
     * Return current date-time in UTC format.
     *
     * @return String value
     */
    public static String generateCurrentTimeInUtc() {
        return LocalDateTime.now(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern(TimeConstants.RUN_TIME_TIMESTAMP_PATTERN));
    }

    /**
     * Return current date-time in UTC format in supplied pattern.
     *
     * @return String value
     */
    public static String generateCurrentTimeInUtc(String pattern) {
        return LocalDateTime.now(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Time stamp in seconds long.
     *
     * @return the long
     */
    public static Long timeStampInSeconds() {
        return timeStampInMilliSeconds() / 1000;
    }

    /**
     * Time stamp in milli seconds long.
     *
     * @return the long
     */
    public static Long timeStampInMilliSeconds() {
        return System.currentTimeMillis();
    }

    /**
     * Generate date time now date.
     *
     * @return the date
     */
    public static Date generateDateTimeNow() {
        return Date.from(Instant.now());
    }

    /**
     * Generate time now minus days date.
     *
     * @param days the days
     * @return the date
     */
    public static Date generateTimeNowMinusDays(Integer days) {
        long seconds = days * 86400L;
        return Date.from(Instant.now().minusSeconds(seconds));
    }

    /**
     * Generate time now plus days date.
     *
     * @param days the days
     * @return the date
     */
    public static Date generateTimeNowPlusDays(Integer days) {
        long seconds = days * 86400L;
        return Date.from(Instant.now().plusSeconds(seconds));
    }

    /**
     * Generate date time now minus seconds date.
     *
     * @param seconds the seconds
     * @return the date
     */
    public static Date generateDateTimeNowMinusSeconds(Integer seconds) {
        return Date.from(Instant.now().minusSeconds(seconds));
    }

    /**
     * Generate date time now plus seconds date.
     *
     * @param seconds the seconds
     * @return the date
     */
    public static Date generateDateTimeNowPlusSeconds(Integer seconds) {
        return Date.from(Instant.now().plusSeconds(seconds));
    }


    /**
     * Generate date time now utc string.
     *
     * @return the string
     */
    public static String generateDateTimeNowUtc() {
        return Instant.now().toString();
    }

    /**
     * Generate date time now truncated seconds string.
     *
     * @return the string
     */
    public static String generateDateTimeNowTruncatedSeconds() {
        return Instant.now().truncatedTo(ChronoUnit.MINUTES).toString();
    }

    /**
     * Generate date time now plus seconds utc string.
     *
     * @param seconds the seconds
     * @return the string
     */
    public static String generateDateTimeNowPlusSecondsUtc(long seconds) {
        return Instant.now().plusSeconds(seconds).toString();
    }

    /**
     * Generate date time now minus seconds utc string.
     *
     * @param seconds the seconds
     * @return the string
     */
    public static String generateDateTimeNowMinusSecondsUtc(long seconds) {
        return Instant.now().minusSeconds(seconds).toString();
    }

    /**
     * Generate epoch now minus seconds utc long.
     *
     * @param seconds the seconds
     * @return the long
     */
    public static Long generateEpochNowMinusSecondsUtc(long seconds) {
        return Instant.now().minusSeconds(seconds).toEpochMilli() / 1000;
    }

    /**
     * Generate date time now plus minutes utc string.
     *
     * @param minutes the minutes
     * @return the string
     */
    public static String generateDateTimeNowPlusMinutesUtc(Integer minutes) {
        long seconds = minutes * 60L;
        return generateDateTimeNowPlusSecondsUtc(seconds);
    }

    /**
     * Generate date time now plus days utc string.
     *
     * @param days the days
     * @return the string
     */
    public static String generateDateTimeNowPlusDaysUtc(Integer days) {
        long seconds = days * 86400L;
        return generateDateTimeNowPlusSecondsUtc(seconds);
    }

    /**
     * Generate date time now minus days utc string.
     *
     * @param days the days
     * @return the string
     */
    public static String generateDateTimeNowMinusDaysUtc(Integer days) {
        long seconds = days * 86400L;
        return generateDateTimeNowMinusSecondsUtc(seconds);
    }

    /**
     * Generate epoch now minus days utc long.
     *
     * @param days the days
     * @return the long
     */
    public static Long generateEpochNowMinusDaysUtc(Integer days) {
        long seconds = days * 86400L;
        return generateEpochNowMinusSecondsUtc(seconds);
    }

    /**
     * Generate date time now plus hours utc string.
     *
     * @param hours the hours
     * @return the string
     */
    public static String generateDateTimeNowPlusHoursUtc(Integer hours) {
        long seconds = hours * 3600L;
        return generateDateTimeNowPlusSecondsUtc(seconds);
    }

    /**
     * Generate date time now minus hours utc string.
     *
     * @param hours the hours
     * @return the string
     */
    public static String generateDateTimeNowMinusHoursUtc(Integer hours) {
        long seconds = hours * 3600L;
        return generateDateTimeNowMinusSecondsUtc(seconds);
    }

    /**
     * Generate date time now minus hours string.
     *
     * @param hours the hours
     * @return the string
     */
    public static String generateDateTimeNowMinusHours(Integer hours) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        long seconds = hours * 3600L;
        return formatter.format(Date.from(Instant.now().minusSeconds(seconds)));
    }

    /**
     * Random string from list string.
     *
     * @param list the list
     * @return the string
     */
    public static String randomStringFromList(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * Random number of strings from list list.
     *
     * @param list the list
     * @return the list
     */
    public static List<String> randomNumberOfStringsFromList(List<String> list) {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(list.size()); i++) {
            resultList.add(list.get(new Random().nextInt(list.size())));
        }
        return resultList;
    }

    /**
     * Generate random string with 10 characters length.
     *
     * @return the string
     */
    public static String randomString() {
        return randomString(NUMBERS_LENGTH);
    }

    /**
     * Random string string.
     *
     * @param len the len
     * @return the string
     */
    public static String randomString(int len) {
        return StringGenerator.randomAlpha(len);
    }

    /**
     * Random string string.
     *
     * @param len the len
     * @param constantPart the constant part
     * @return the string
     */
    public static String randomString(int len, String constantPart) {
        return StringGenerator.randomAlpha(len) + constantPart;
    }

    /**
     * Random string string.
     *
     * @param words the words
     * @param wordLength the word length
     * @return the string
     */
    public static String randomString(int words, int wordLength) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < words; i++) {
            stringBuilder.append(StringGenerator.randomAlpha(wordLength));
            if (i < words - 1) {
                stringBuilder.append(' ');
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Random ip address string.
     *
     * @return the string
     */
    public static String randomIpAddress() {
        Random r = RandomHelper.random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }

    /**
     * Random mac address string.
     *
     * @return the string
     */
    public static String randomMacAddress() {

        byte[] macAddr = new byte[6];
        RandomHelper.random().nextBytes(macAddr);

        //zeroing last 2 bytes to make it unicast and locally adminstrated
        macAddr[0] = (byte) (macAddr[0] & (byte) 254);

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {

            if (sb.length() > 0) {
                sb.append(":");
            }

            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    /**
     * Random application version string.
     *
     * @return the string
     */
    public static String randomApplicationVersion() {
        return String.format("%d.%d",
            new IntegerGenerator().generate(1, 100),
            new IntegerGenerator().generate(0, 1000));
    }

    /**
     * Random release version string.
     *
     * @return the string
     */
    public static String randomReleaseVersion() {
        return String.format("%d.%d.%d",
            new IntegerGenerator().generate(1, 10),
            new IntegerGenerator().generate(1, 10),
            new IntegerGenerator().generate(1, 10000));
    }

    /**
     * Random os serial number string.
     *
     * @return the string
     */
    public static String randomOsSerialNumber() {
        return String.format("%s-%s-%s-%s",
            randomNumeric(5), randomNumeric(5),
            randomNumeric(5), randomAlphaNumeric(5).toUpperCase());
    }

    /**
     * Random machine identity string.
     *
     * @return the string
     */
    public static String randomMachineIdentity() {
        return String.format("%s-%s-%s-%s-%s",
            randomAlphaNumeric(8).toUpperCase(), randomAlphaNumeric(4).toUpperCase(),
            randomAlphaNumeric(4).toUpperCase(), randomAlphaNumeric(4).toUpperCase(),
            randomAlphaNumeric(12).toUpperCase());
    }

    /**
     * Random bios serial string.
     *
     * @return the string
     */
    public static String randomBiosSerial() {
        return StringGenerator.randomUuid(14, 2, ' ').toLowerCase()
            + "-" + StringGenerator.randomUuid(16, 2, ' ').toLowerCase();
    }

    /**
     * Random alpha numeric string.
     *
     * @param len the len
     * @return the string
     */
    public static String randomAlphaNumeric(int len) {
        return StringGenerator.randomAlphaNumeric(len);
    }

    /**
     * Random alpha numeric string with spaces.
     *
     * @param len the len
     * @return the string
     */
    public static String randomAlphaNumericWithSpaces(int len) {
        return StringGenerator.randomAlphaNumericWithSpaces(len);
    }

    /**
     * Random alpha numeric string with special chars.
     *
     * @param len the len
     * @return the string
     */
    public static String randomAlphaNumericWithSpecialChars(int len) {
        return StringGenerator.randomAlphaNumericWithSpecialChars(len);
    }

    /**
     * Random numeric string.
     *
     * @param len the len
     * @return the string
     */
    public static String randomNumeric(int len) {
        return StringGenerator.randomNumeric(len);
    }

    /**
     * Random object from list t.
     *
     * @param <T> the type parameter
     * @param list the list
     * @return the t
     */
    public static <T extends Object> T randomObjectFromList(List<T> list) {
        int index = RandomHelper.random().nextInt(list.size());
        return list.get(index);
    }

    /**
     * Random object from array t.
     *
     * @param <T> the type parameter
     * @param array the array
     * @return the t
     */
    public static <T extends Object> T randomObjectFromArray(T[] array) {
        return randomObjectFromList(Arrays.asList(array));
    }

    /**
     * Random objects from list list.
     *
     * @param <T> the type parameter
     * @param list the list
     * @param count the count
     * @return the list
     */
    public static <T> List<T> randomObjectsFromList(List<T> list, int count) {
        int listSize = list.size();
        if (count > listSize) {
            LOGGER.error("Please provide valid numbers of Items, max size is: {}", listSize);
            count = 0;
        }
        Collections.shuffle(list);
        List<T> newList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    /**
     * Get random entry from map. map can be a complex structure.
     *
     * @param obj - object representing map or list
     * @return any random entry. if a nested map, then it could return an entry from nested map
     */
    public static Map.Entry<String, Object> randomEntryFromComplexMap(Map<String, Object> obj) {
        Map<String, Object> flat = FlatMap.asFlattendMap(obj);
        List<Map.Entry<String, Object>> entries = new ArrayList<>(flat.entrySet());
        return entries.get(RandomHelper.random().nextInt(entries.size()));
    }

    /**
     * Get random entry from Map(String, String).
     *
     * @param map - map
     * @return any random entry. if a nested map, then it could return an entry from nested map
     */
    public static Map.Entry<String, String> randomEntryFromMap(Map<String, String> map) {
        List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
        return entries.get(RandomHelper.random().nextInt(entries.size()));
    }

    /**
     * Random key from map object.
     *
     * @param map input map
     * @return Object : random key from map
     */
    public static Object randomKeyFromMap(Map<?, ?> map) {
        List<?> keys = new ArrayList<>(map.keySet());
        return keys.get(RandomHelper.random().nextInt(keys.size()));
    }

    /**
     * Random object from map v.
     *
     * @param <K> the type parameter
     * @param <V> the type parameter
     * @param map input map
     * @return random value from map
     */
    public static <K, V> V randomObjectFromMap(Map<K, V> map) {
        List<K> keys = new ArrayList<>(map.keySet());
        return map.get(Faker.randomObjectFromList(keys));
    }

    /**
     * Random integer from list integer.
     *
     * @param list the list
     * @return the integer
     */
    public static Integer randomIntegerFromList(List<Integer> list) {
        int index = RandomHelper.random().nextInt(list.size());
        return list.get(index);
    }

    /**
     * Random object object.
     *
     * @param type the type
     * @return the object
     */
    public static Object randomObject(String type) {
        return new RandomValueGeneratorProvider()
            .fromString(type).provideGenerator().generate();
    }

    /**
     * Random object object.
     *
     * @param type the type
     * @param min the min
     * @param max the max
     * @return the object
     */
    public static Object randomObject(String type, Object min, Object max) {
        return new RandomValueGeneratorProvider()
            .fromString(type).provideGenerator().generate(min, max);
    }

    /**
     * Random int integer.
     *
     * @return the integer
     */
    public static Integer randomInt() {
        return new IntegerGenerator().generate();
    }

    /**
     * Random int int.
     *
     * @param max the max
     * @return the int
     */
    public static int randomInt(int max) {
        return new IntegerGenerator().generate(0, max);
    }

    /**
     * Random int int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomInt(int min, int max) {
        return new IntegerGenerator().generate(min, max);
    }

    /**
     * Random bool boolean.
     *
     * @return the boolean
     */
    public static boolean randomBool() {
        return new BooleanGenerator().generate();
    }

    /**
     * Random long long.
     *
     * @return the long
     */
    public static long randomLong() {
        return new LongGenerator().generate();
    }

    /**
     * Random long long.
     *
     * @param min the min
     * @param max the max
     * @return the long
     */
    public static long randomLong(Long min, Long max) {
        return new LongGenerator().generate(min, max);
    }

    /**
     * Random double double.
     *
     * @return the double
     */
    public static double randomDouble() {
        return new DoubleGenerator().generate();
    }

    /**
     * Random double double.
     *
     * @param minX the min x
     * @param maxX the max x
     * @return the double
     */
    public static double randomDouble(Double minX, Double maxX) {
        return new DoubleGenerator().generate(minX, maxX);
    }

    /**
     * Random float float.
     *
     * @return the float
     */
    public static float randomFloat() {
        return new FloatGenerator().generate();
    }

    /**
     * Random float float.
     *
     * @param minX the min x
     * @param maxX the max x
     * @return the float
     */
    public static float randomFloat(Float minX, Float maxX) {
        return new FloatGenerator().generate(minX, maxX);
    }

    /**
     * Generate uuid as string string.
     *
     * @return the string
     */
    public static String generateUuidAsString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate uuid uuid.
     *
     * @return the uuid
     */
    public static UUID generateUiid() {
        return UUID.randomUUID();
    }

    /**
     * Generate uuid time based as string string.
     *
     * @return the string
     */
    public static String generateUuidTimeBasedAsString() {
        return generateUuidTimeBased().toString();
    }

    /**
     * Generate UUID type v1 based on Date Time.
     *
     * @return the uuid
     */
    public static UUID generateUuidTimeBased() {
        return Generators.timeBasedGenerator().generate();
    }

    /**
     * Generate random list list.
     *
     * @param preset the preset
     * @return the list
     */
    public static List<String> generateRandomList(List<String> preset) {
        int numberOfListItems = Faker.randomInt(0, preset.size() + 1);
        return generateRandomList(preset, numberOfListItems);
    }

    /**
     * Generate random list of numbers without duplication.
     *
     * @param minNumberInList - is min value number in list
     * @param maxNumberInList - is max size of list and max integer value in list (includes maxNumberInList)
     * @return the list
     */
    public static List<Integer> generateRandomList(int minNumberInList, int maxNumberInList) {
        Integer randomSize = Faker.randomInt(1, maxNumberInList - minNumberInList + 1);
        return new IntegerGenerator().generateUniqueList(randomSize, minNumberInList, maxNumberInList + 1);
    }

    /**
     * Generate random list of numbers without duplication.
     *
     * @param maxNumberInList - is max size of list and max integer value in list (includes maxNumberInList)
     * @return the list
     */
    public static List<Integer> generateRandomList(int maxNumberInList) {
        return generateRandomList(0, maxNumberInList);
    }

    /**
     * Generate random list.
     *
     * @param preset the preset
     * @param atLeast the at least
     * @return the list
     */
    public static <T> List<T> generateRandomList(List<T> preset, int atLeast) {
        return generateRandomListWithLimit(new ArrayList<>(preset), Faker.randomInt(atLeast, preset.size() + 1));
    }

    /**
     * Generate random list.
     *
     * @param preset the preset
     * @param limit limit number of output size
     * @return the list
     */
    public static <T> List<T> generateRandomListWithLimit(List<T> preset, int limit) {

        List<T> copyListPreset = new ArrayList<>(preset);
        if (limit > preset.size()) {
            throw new InvalidParameterException("Please provide valid numbers of Items, max size is: "
                + preset.size());
        }
        return buildRandomListFromExisting(copyListPreset, limit);
    }

    private static <T> List<T> buildRandomListFromExisting(List<T> inputList, int limit) {
        int currentItem = 1;
        List<T> randomList = new ArrayList<>();
        while (currentItem <= limit) {
            T randomItem = Faker.randomObjectFromList(inputList);
            if (!randomList.contains(randomItem)) {
                randomList.add(randomItem);
                inputList.remove(randomItem);
                currentItem++;
            }
        }
        return randomList;
    }

    /**
     * Random email address string.
     *
     * @return the string
     */
    public static String randomEmailAddress() {
        return Faker.randomString(8) + "@" + Faker.randomString(16) + ".net";
    }

    /**
     * Random map map.
     *
     * @param numberOfElements the number of elements
     * @return the map
     */
    public static Map<String, Object> randomMap(int numberOfElements) {
        Map<String, Object> randomMap = new HashMap<>();
        int i = 0;
        while (i++ < numberOfElements) {
            randomMap.put(Faker.randomString(8), new RandomValueGeneratorProvider()
                .fromCls(Types.random().getType()).provideGenerator().generate());
        }
        return randomMap;
    }

    /**
     * Random windows path to folder string.
     *
     * @return the string
     */
    public static String randomWindowsPathToFolder() {
        return randomWindowsPath(3);
    }

    /**
     * Random windows path to file string.
     *
     * @return the string
     */
    public static String randomWindowsPathToFile() {
        return randomWindowsPath(3) + Faker.randomString() + ".txt";
    }

    /**
     * Random windows path string.
     *
     * @param folderCount the folder count
     * @return the string
     */
    public static String randomWindowsPath(int folderCount) {
        StringBuilder windowsPath = new StringBuilder();
        windowsPath.append(Faker.randomString(1).toUpperCase());
        windowsPath.append(":");
        for (int i = 0; i < folderCount; i++) {
            windowsPath.append("\\").append(Faker.randomString());
        }
        return windowsPath.toString();
    }

    /**
     * Random registry path string.
     *
     * @param folderCount the folder count
     * @return the string
     */
    public static String randomRegistryPath(int folderCount) {
        StringBuilder windowsPath = new StringBuilder();
        windowsPath.append(Faker.randomString(12).toUpperCase());
        for (int i = 0; i < folderCount; i++) {
            windowsPath.append("\\").append(Faker.randomString(8).toUpperCase());
        }
        return windowsPath.toString();
    }

    /**
     * Random linux path to folder string.
     *
     * @return the string
     */
    public static String randomLinuxPathToFolder() {
        return randomLinuxPath(3);
    }

    /**
     * Random linux path to file string.
     *
     * @return the string
     */
    public static String randomLinuxPathToFile() {
        return randomLinuxPath(3) + Faker.randomString() + ".txt";
    }

    /**
     * Random linux path string.
     *
     * @param folderCount the folder count
     * @return the string
     */
    public static String randomLinuxPath(int folderCount) {
        StringBuilder linuxPath = new StringBuilder();
        linuxPath.append("/tmp");
        for (int i = 0; i < folderCount; i++) {
            linuxPath.append("/").append(Faker.randomString());
        }
        return linuxPath.toString();
    }

    /**
     * Invert char case string.
     *
     * @param text the text
     * @return the string
     */
    /* Reverse case of random char in a string
     * @param text
     * @return new String
     */
    public static String invertCharCase(final String text) {
        return charInvert(text, 0);

    }

    private static String charInvert(final String text, int retry) {
        char[] charArray = text.toCharArray();
        int index = Faker.randomInt(0, charArray.length - 1);
        char newChar = ' ';

        for (; index < charArray.length; index++) {
            char charAt = charArray[index];
            if (Character.isLetter(charArray[index])) {
                if (Character.isUpperCase(charAt)) {
                    newChar = Character.toLowerCase(charAt);
                } else if (Character.isTitleCase(charAt)) {
                    newChar = Character.toLowerCase(charAt);
                } else if (Character.isLowerCase(charAt)) {
                    newChar = Character.toUpperCase(charAt);
                }
            }
            if (newChar != ' ') {
                break;
            }
        }
        if (retry > 3) {
            return text;
        }
        if (newChar == ' ') {
            return charInvert(text, retry);
        }
        return text.replace(text.charAt(index), newChar);
    }

    /**
     * Random unique list list.
     *
     * @param numberOfElements the number of elements
     * @param stringSize the string size
     * @return the list
     */
    public static List<String> randomUniqueList(int numberOfElements, int stringSize) {
        Set<String> unique = new HashSet<>();
        while (unique.size() < numberOfElements) {
            unique.add(Faker.randomString(stringSize).toUpperCase());
        }
        return Lists.newArrayList(unique);
    }

    /**
     * Is uuid boolean.
     *
     * @param string the string
     * @return the boolean
     */
    public static boolean isUuid(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Random partner id string.
     *
     * @return the string
     */
    public static String randomPartnerId() {
        return randomNumeric(8);
    }

    /**
     * Random increment int.
     *
     * @param number the number
     * @return the int
     */
    public static int randomIncrement(int number) {
        List<Integer> intList = new ArrayList<>();
        intList.add(number);
        intList.add(-1 * number);
        return randomObjectFromList(intList);
    }

}
