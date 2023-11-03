package framework.utilities.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import framework.utilities.BeanUtils;
import framework.utilities.MavenProperty;
import framework.utilities.ReflectionUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class ConfigLoader {

    private static final String ERROR_LOADING_CONFIGURATION = "Error loading configuration: ";

    //store loaded configs in map
    private static final ConcurrentHashMap<String, Properties> PROPS_MAP = new ConcurrentHashMap<>();

    private ConfigLoader() {
        throw new AssertionError();
    }

    /**
     * Override object by input system properties.
     * For example Object Foo has field BarBaz, then -DBarBaz=somevalue would override that.
     * @param object Object.
     */
    public static void overrideObjectBySystemProperties(Object object) {
        Properties systemProperties = System.getProperties();
        try {
            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, object.getClass().getDeclaredFields());
            Collections.addAll(fields, object.getClass().getSuperclass().getDeclaredFields());
            for (Field field : fields) {
                String propertyName;
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                if (jsonProperty != null) {
                    propertyName = jsonProperty.value();
                } else {
                    propertyName = field.getName();
                }
                if (systemProperties.getProperty(propertyName) != null) {
                    ReflectionUtil.callSetter(object, getValue(systemProperties, propertyName, field.getType()), field);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigLoadException("Error transforming pojo to properties: " + e);
        }
    }

    /**
     * Override input Object by maven&system properties.
     * @param object Input object, input object will be modified
     */
    public static void overrideObjectBySystemPropertyRecursively(Object object) {
        Properties properties = MavenProperty.getMavenProperties();
        Map<String, String> describedMap = BeanUtils.recursiveDescribe(object);
        properties
            .entrySet()
            .stream()
            .filter(property -> describedMap.containsKey(property.getKey().toString()))
            .forEach(
                property -> BeanUtils.setProperty(object, property.getKey().toString(), property.getValue()));
    }

    /**
     * Load config into class variables. Class variables should be static
     */
    public static void load(Class<?> configClass, String file) {
        try {
            Properties systemProperties = System.getProperties();
            Properties props = loadConfigProperties(configClass, file);

            for (Field field : configClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && (props.getProperty(field.getName()) != null
                    || systemProperties.getProperty(field.getName()) != null)) {
                    //override property if defined
                    if (systemProperties.getProperty(field.getName()) != null) {
                        props.setProperty(field.getName(), systemProperties.getProperty(field.getName()));
                    }
                    ReflectionUtil.setFieldValue(null, field, getValue(props, field.getName(), field.getType()));
                }
            }
        } catch (Exception e) {
            throw new ConfigLoadException(ERROR_LOADING_CONFIGURATION + e);
        }
    }

    /**
     * Load config into class public/private/protected/static/non-static/ variables.
     */
    public static void load(Object object, String file) {
        try {
            Properties systemProperties = System.getProperties();
            Properties props = loadConfigProperties(object.getClass(), file);

            List<Field> fields = new ArrayList<>();
            Collections.addAll(fields, object.getClass().getDeclaredFields());
            Collections.addAll(fields, object.getClass().getSuperclass().getDeclaredFields());

            for (Field field : fields) {
                if (props.getProperty(field.getName()) != null
                    || systemProperties.getProperty(field.getName()) != null) {
                    //override property if defined
                    if (systemProperties.getProperty(field.getName()) != null) {
                        props.setProperty(field.getName(), systemProperties.getProperty(field.getName()));
                    }
                    field.setAccessible(true);
                    field.set(object, getValue(props, field.getName(), field.getType()));
                }
            }
        } catch (Exception e) {
            throw new ConfigLoadException(ERROR_LOADING_CONFIGURATION + e);
        }
    }

    /**
     * The the value from properties and convert to its proper type.
     */
    private static Object getValue(Properties props, String name, Class<?> type) {
        String value = props.getProperty(name);
        if (value.equals("") && type != String.class) {
            return null;
        }
        if (type == String.class) {
            return value;
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == List.class || type == Arrays.class) {
            return Arrays.asList(value);
        }
        throw new IllegalArgumentException("Unknown configuration value type: " + type.getName());
    }


    /**
     * load config properties into class - usualy it should be some library that has lots of configurations and class
     *     that uses it does not need to store it.
     */
    public static Properties loadConfigProperties(Class<?> configClass, String file) {
        return PROPS_MAP.computeIfAbsent(file, key -> {
            Properties props = new Properties();
            try (InputStream inputStream = configClass.getResourceAsStream(file)) {
                props.load(inputStream);
            } catch (Exception e) {
                throw new ConfigLoadException(ERROR_LOADING_CONFIGURATION + e);
            }
            return props;
        });
    }

    /**
     * load properties from file. usualy this is maven.properties. return properties replaceSystemProperties = true will
     * replace maven.properties by system properties key-by-key
     *
     * @return Properties
     */
    public static Properties loadProps(String file, boolean replaceSystemProperties) {
        try {
            Properties props = loadConfigProperties(ConfigLoader.class, file);
            if (replaceSystemProperties) {
                ConfigLoader.replaceSystemProperties(props);
            }
            return props;
        } catch (Exception e) {
            throw new ConfigLoadException(ERROR_LOADING_CONFIGURATION + e);
        }
    }

    /**
     * merge system properties with maven properties.
     */
    public static Properties replaceSystemProperties(Properties properties) {
        Properties systemProperties = System.getProperties();
        properties.putAll(systemProperties);
        return properties;
    }

    /**
     * Retrieve property value from Properties.
     * @param props Properties that hold properties
     * @param key desired property to retrive
     * @return Object property value
     */
    public static Object getProperty(Properties props, String key) {
        List<String> vals = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            } else if (entry.getKey().toString().contains(key)) {
                vals.add((String) entry.getKey());
            }
        }
        return Collections.max(vals, Comparator.comparingInt(String::length));
    }

}
