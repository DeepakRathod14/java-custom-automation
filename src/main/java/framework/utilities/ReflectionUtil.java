package framework.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ReflectionUtil {

    public static final Logger LOGGER = LogManager.getLogger();

    private ReflectionUtil() {
        //default constructor
    }

    /**
     * Set a value of an object by field name.
     *
     * @param obj Object that has fieldName
     * @param fieldName Field name in Object
     * @param value the value to be set
     */
    public static void setFieldByJsonPropertyName(Object obj, String fieldName, Object value) {
        List<Field> fields = new ArrayList<>();
        Collections.addAll(fields, obj.getClass().getDeclaredFields());
        Collections.addAll(fields, obj.getClass().getSuperclass().getDeclaredFields());
        try {
            for (Field field : fields) {
                if (isEquals(fieldName, field)) {
                    callSetter(obj, value, field);
                    break;
                }
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Call setter for underlying object and field.
     *
     * @param obj the object that holds field
     * @param value the value that will be set into that field
     * @param field the field in that class
     * @throws IllegalAccessException - in case if access issues
     * @throws InvocationTargetException - in case if target method cannot be called
     * @throws NoSuchMethodException - in case if getter method not found
     */
    public static void callSetter(Object obj, Object value, Field field)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String setterName = "set" + StringUtils.capitalize(field.getName());
        obj.getClass().getMethod(setterName, field.getType()).invoke(obj, value);
    }

    /**
     * Call getter for underlying object.
     *
     * @param obj object that holds field
     * @param field field in that class
     * @throws IllegalAccessException - in case if access issues
     * @throws InvocationTargetException - in case if target method cannot be called
     * @throws NoSuchMethodException - in case if getter method not found
     */
    public static Object callGetter(Object obj, Field field)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String setterName = "get" + StringUtils.capitalize(field.getName());
        return obj.getClass().getMethod(setterName).invoke(obj);
    }

    /**
     * Get field value from object.
     *
     * @param obj input object
     * @param field Field of that object
     * @return Object value of that field
     * @throws IllegalAccessException if field is not accessible
     */
    public static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
        boolean revert = false;
        if (!field.isAccessible()) {
            field.setAccessible(true);
            revert = true;
        }
        Object val = field.get(obj);
        if (revert) {
            field.setAccessible(false);
        }
        return val;
    }

    /**
     * Set field value of defined class.
     *
     * @param clz object
     * @param field field that is in that object
     * @param value desired value
     * @throws IllegalAccessException if field cannot be modified
     */
    public static void setFieldValue(Object clz, Field field, Object value) throws IllegalAccessException {
        boolean revert = false;
        if (!field.isAccessible()) {
            field.setAccessible(true);
            revert = true;
        }
        field.setAccessible(true);
        field.set(clz, value);
        if (revert) {
            field.setAccessible(false);
        }
    }


    /**
     * Get Field value by fieldName.
     *
     * @param obj Object that stores fieldName
     * @param fieldName - fieldName that will be used to get info
     * @return Object value
     */
    public static Object getFieldByJsonPropertyName(Object obj, String fieldName) {
        return getFieldByJsonPropertyName(obj, fieldName, false);
    }

    /**
     * Get Field value by fieldName.
     *
     * @param obj Object that stores fieldName
     * @param fieldName - fieldName that will be used to get info
     * @param ignoreCase - true | false - ignore case or not
     * @return Object value
     */
    public static Object getFieldByJsonPropertyName(Object obj, String fieldName,
        Boolean ignoreCase) {
        List<Field> fields = new ArrayList<>();
        Collections.addAll(fields, obj.getClass().getDeclaredFields());
        Collections.addAll(fields, obj.getClass().getSuperclass().getDeclaredFields());
        for (Field field : fields) {
            if (isEquals(fieldName, field, ignoreCase)) {
                return getValueOfField(field, obj);
            }
        }
        return null;
    }

    /**
     * Get Field value by fieldName ignoring case.
     *
     * @param obj Object that stores fieldName
     * @param fieldName - fieldName that will be used to get info
     * @return Object value
     */
    public static Object getFieldByJsonPropertyNameIgnoreCase(Object obj, String fieldName) {
        return getFieldByJsonPropertyName(obj, fieldName, true);
    }

    private static boolean isEquals(String fieldName, Field field, Boolean ignoreCase) {
        if (Boolean.TRUE.equals(ignoreCase)) {
            return isEqualsIgnoreCase(fieldName, field);
        }
        return isEquals(fieldName, field);
    }

    private static boolean isEquals(String fieldName, Field field) {
        return field.getAnnotation(JsonProperty.class).value().equals(fieldName) || field.getName()
            .equals(fieldName);
    }

    private static boolean isEqualsIgnoreCase(String fieldName, Field field) {
        return field.getAnnotation(JsonProperty.class).value().equalsIgnoreCase(fieldName) || field
            .getName().equalsIgnoreCase(fieldName);
    }

    /**
     * Gets the value from annotated field @JsonProperty.
     *
     * @param field the Field
     * @return String value of annotated field
     */
    public static String getValueOfJsonProperty(Field field) {
        return field.isAnnotationPresent(JsonProperty.class) ? field.getAnnotation(JsonProperty.class)
            .value() : null;
    }

    /**
     * Get field value from corresponding object.
     *
     * @param field Field
     * @param obj Input Object
     * @return value of the field from that object
     */
    public static Object getValueOfField(Field field, Object obj) {
        try {
            return callGetter(obj, field);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(e);
        }
        return null;
    }


    /**
     * Get method from class ignoring case. Use class.getMethod instead if case needs to be preserved.
     *
     * @param clazz Class where search should be performed
     * @param methodName desired method name
     * @param paramType parameter type
     */
    public static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?> paramType)
        throws NoSuchMethodException {
        return Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equalsIgnoreCase(methodName))
            .filter(m -> m.getParameterTypes().length == 1)
            .filter(m -> m.getParameterTypes()[0].equals(paramType)).findFirst().orElseThrow(
                () -> new NoSuchMethodException(String
                    .format("Class %s, Method %s with params " + "type %s not found", clazz.getName(),
                        methodName, paramType.getName())));
    }

    /**
     * Get all declared Fields in provided class. Do not use supper class.
     *
     * @param dto class
     * @return List of fields
     */
    public static List<String> getFieldNames(Class<?> dto) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : dto.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    /**
     * Get all declared Fields in provided class and by Annotation. Do not use supper class.
     *
     * @param clazz class
     * @param annotation annotation to get
     * @return list of fields with specific annotation
     */
    public static List<String> getFieldNamesWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }


}
