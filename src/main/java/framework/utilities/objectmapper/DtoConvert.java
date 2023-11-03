package framework.utilities.objectmapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import framework.utilities.ReflectionUtil;
import framework.utilities.files.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import static framework.utilities.files.FileUtil.*;

public class DtoConvert {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String UTF_8 = "UTF-8";

    private DtoConvert() {
        throw new AssertionError();
    }

    /**
     * Convert dto to json string.
     *
     * @param dtoClass input object
     * @return String json
     */
    public static String dtoToJsonString(Object dtoClass) {
        ObjectMapper mapper = new ObjectMapper();
        return writeAsString(dtoClass, mapper);
    }

    /**
     * Convert dto to pretty json output.
     *
     * @param dtoClass - source
     * @return string json
     */
    public static String dtoToPrettyJsonString(Object dtoClass) {
        String result = Strings.EMPTY;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoClass);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * Convert object to yaml.
     *
     * @param dtoClass - source
     * @return String YAML
     */
    public static String dtoToYaml(Object dtoClass) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return writeAsString(dtoClass, mapper);
    }

    private static String writeAsString(Object dtoClass, ObjectMapper mapper) {
        String result = Strings.EMPTY;
        try {
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            result = mapper.writeValueAsString(dtoClass);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }


    protected static <T> T readStream(Class<T> configClass, String filePath, ObjectMapper dtoObjectMapper) {
        try (InputStream inputStream = FileUtil.getFileInputStream(filePath, configClass)) {
            return dtoObjectMapper.readValue(inputStream, configClass);
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        return null;
    }

    /**
     * Convert json string into dto list.
     *
     * @param configClass Config class - target
     * @param json        - input json string - source
     * @param <T>         any type
     * @return List of T of defined target Class
     */
    public static <T> List<T> jsonStringToDtoList(Class<T> configClass, String json) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        try {
            return dtoObjectMapper.readValue(json,
                dtoObjectMapper.getTypeFactory().constructCollectionType(List.class, configClass));
        } catch (JsonProcessingException e) {
            LOGGER.catching(e);
        }
        return new ArrayList<>();
    }

    protected static <T> List<T> readListStream(Class<T> configClass, String filePath, ObjectMapper dtoObjectMapper) {
        try (InputStream inputStream = FileUtil.getFileInputStream(filePath, configClass)) {
            return dtoObjectMapper.readValue(inputStream,
                dtoObjectMapper.getTypeFactory().constructCollectionType(List.class, configClass));
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        return new ArrayList<>();
    }

    /**
     * Convert xml to dto.
     *
     * @param configClass - target
     * @param content     - source
     * @param <T>         T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T xmlToDto(Class<T> configClass, String content) {
        try {
            JAXBContext context = JAXBContext.newInstance(configClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setEventHandler(
                event -> {
                    throw new XmlParserException(event.getMessage(),
                        event.getLinkedException());
                });
            return (T) unmarshaller.unmarshal(new StringReader(content));
        } catch (JAXBException e) {
            LOGGER.catching(e);
            return null;
        }
    }

    /**
     * Convert string to dto.
     *
     * @param configClass   - target
     * @param filePath      - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T yamlFileToDto(Class<T> configClass, String filePath, boolean failOnUnknown) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        return readStream(configClass, filePath, mapper);
    }

    /**
     * Convert string to dto.
     *
     * @param dtoClass      - target
     * @param content       - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T stringToDto(Class<T> dtoClass, String content, boolean failOnUnknown) {
        return stringToDto(dtoClass, content, failOnUnknown, new ObjectMapper());
    }

    /**
     * Convert string to dto.
     *
     * @param dtoClass        - source
     * @param content         - target
     * @param failOnUnknown   true if should fail if unknown property
     * @param dtoObjectMapper object mapper
     * @param <T>             T any type suitable for deserialization, list, map, dto
     * @return T
     */
    @Nullable
    public static <T> T stringToDto(Class<T> dtoClass, String content, boolean failOnUnknown,
        ObjectMapper dtoObjectMapper) {
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try {
            return dtoObjectMapper.readValue(content, dtoClass);
        } catch (IOException e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Check if json is valid.
     *
     * @param jsonInString input string
     * @return true if content is valid as json
     */
    public static boolean isJsonValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Convert yaml to dto.
     *
     * @param dtoClass      - target
     * @param content       - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T yamlToDto(Class<T> dtoClass, String content, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper(new YAMLFactory());
        return stringToDto(dtoClass, content, failOnUnknown, dtoObjectMapper);
    }

    /**
     * Convert dto to XFormUrlEncoded.
     *
     * @param object input object
     * @return String
     */
    public static String dtoToXFormUrlEncoded(Object object) {
        Map<String, Object> map = DtoConvert.dtoToMap(object);
        return map.entrySet().stream()
            .map(DtoConvert::encodeField)
            .collect(Collectors.joining("&"));
    }

    /**
     * Convert object To Map.
     *
     * @param dtoClass Class
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> dtoToMap(Object dtoClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.convertValue(dtoClass, Map.class);
    }

    private static String encodeField(Map.Entry<String, Object> x) {
        String res = "";
        try {
            res = x.getKey() + "=" + URLEncoder.encode(x.getValue().toString(), UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }
        return res;
    }


    /**
     * Convert xFormUrlEncoded to Dto.
     *
     * @param dtoClass class to convert - target
     * @param content  input - source
     * @param <T>      T any type suitable for deserialization
     * @return T
     */
    public static <T> T xformUrlEncodedToDto(Class<T> dtoClass, String content) {
        T obj;
        try {
            obj = dtoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e);
            return null;
        }
        return xformUrlEncodedToDto(obj, content);
    }

    /**
     * Convert xFormUrlEncoded to Dto.
     *
     * @param obj     obj to convert to
     * @param content source content
     * @param <T>     T any type suitable for deserialization
     * @return T
     */
    public static <T> T xformUrlEncodedToDto(T obj, String content) {
        String[] pairs = content.split("\\&");

        for (int i = 0; i < pairs.length; i++) {
            String[] fields = pairs[i].split("=");
            String name = "";
            String value = "";
            try {
                name = URLDecoder.decode(fields[0], UTF_8);
                value = URLDecoder.decode(fields[1], UTF_8);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e);
            }
            ReflectionUtil.setFieldByJsonPropertyName(obj, name, value);
        }
        return obj;
    }

    /**
     * Convert bytes to dto.
     *
     * @param dtoClass      Class - target
     * @param content       byte[] - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T bytesToDto(Class<T> dtoClass, byte[] content, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try {
            return dtoObjectMapper.readValue(content, dtoClass);
        } catch (IOException e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Convert stream to dto.
     *
     * @param dtoClass      Class - target
     * @param content       Stream - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T inputStreamToDto(Class<T> dtoClass, InputStream content, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try {
            return dtoObjectMapper.readValue(content, dtoClass);
        } catch (IOException e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Convert map to dto.
     *
     * @param dtoClass      Class - target
     * @param content       a Map - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T mapToDto(Class<T> dtoClass, Map<String, Object> content, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try {
            return dtoObjectMapper.convertValue(content, dtoClass);
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Convert map to dto.
     *
     * @param object        Object to use to convert - target
     * @param content       map - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */

    public static <T> T mapToDto(Object object, Map<String, Object> content, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try {
            return (T) dtoObjectMapper.convertValue(content, object.getClass());
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Convert string content to TypeReference deserialize it using TypeReference.
     *
     * @param content       path to json file
     * @param typeReference TypeReference describing an object
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T contentToDto(String content, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.readValue(content, typeReference);
        } catch (IOException e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Read a json file and deserialize it using Class.
     *
     * @param filePath      path to file
     * @param clazz         Class
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization
     * @return T
     */
    public static <T> T jsonFileToMap(String filePath, Class<?> clazz, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try (InputStream inputStream = getFileInputStream(filePath, clazz)) {
            return (T) dtoObjectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    /**
     * Convert json file to dto using TypeReference.
     *
     * @param typeReference TypeReference - target
     * @param filePath      - source
     * @param failOnUnknown true if should fail if unknown property
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T jsonFileToDto(TypeReference<T> typeReference, String filePath, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        try (InputStream inputStream = getFileInputStream(filePath, DtoConvert.class)) {
            return dtoObjectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            LOGGER.catching(e);
        }
        return (T) new Object();
    }

    /**
     * Read json file as dro using Class.
     *
     * @param configClass Class - target
     * @param filePath    - file source
     * @param <T>         T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T jsonFileToDto(Class<T> configClass, String filePath, boolean failOnUnknown) {
        ObjectMapper dtoObjectMapper = new ObjectMapper();
        dtoObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
        return readStream(configClass, filePath, dtoObjectMapper);
    }

    /**
     * Read a json file and deserialize it using TypeReference.
     *
     * @param filePath      path to json file
     * @param typeReference TypeReference describing an object
     * @param <T>           T any type suitable for deserialization, list, map, dto
     * @return T
     */
    public static <T> T jsonFileToDto(String filePath, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try (InputStream inputStream = getFileInputStream(filePath, DtoConvert.class)) {
            return mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    /**
     * Read json file as dto list using Class.
     *
     * @param configClass Class - target
     * @param filePath    - file source
     * @param <T>         any type
     * @return List of T of defined target Class
     */
    public static <T> List<T> jsonFileToDtoList(Class<T> configClass, String filePath) {
        return jsonStringToDtoList(configClass, loadFileAsString(filePath));
    }
}