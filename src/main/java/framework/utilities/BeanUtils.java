package framework.utilities;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.FluentPropertyBeanIntrospector;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BeanUtils {

    private static final Logger LOGGER = LogManager.getLogger();
    private static ConvertUtilsBean converter = BeanUtilsBean.getInstance().getConvertUtils();

    static {
        PropertyUtils.addBeanIntrospector(new FluentPropertyBeanIntrospector());
    }

    private BeanUtils() {
        //default constructor
    }

    /**
     * Get property from object.
     *
     * @param bean any object
     * @param name the name of the property or path, for example nameA.nameB or with arrays nameA.1.nameB
     * @return the value of located name from input Object bean
     */
    public static Object getProperty(Object bean, String name) {
        Object result = null;
        try {
            result = PropertyUtils.getProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(e);
        }
        return result;
    }

    /**
     * Set the value of property.
     *
     * @param bean  any object
     * @param name  the name of the property or path, for example nameA.nameB or with arrays nameA.1.nameB
     * @param value the value; value type should match
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Recursively describe input object.
     *
     * @param object input Object to describe
     * @return Flat map, key is path to property and value is a String value of that property
     */
    public static Map<String, String> recursiveDescribe(Object object) {
        Set cache = new HashSet();
        return recursiveDescribe(object, null, cache);
    }

    private static Map<String, String> recursiveDescribe(Object object, String prefix, Set cache) {
        if (object == null /*|| cache.contains(object)*/) {
            return Collections.emptyMap();
        }
        cache.add(object);
        prefix = (prefix != null) ? prefix + "." : "";

        Map<String, String> beanMap = new TreeMap<>();

        Map<String, Object> properties = getProperties(object);

        for (Map.Entry<String, Object> propertyEntry : properties.entrySet()) {
            String property = propertyEntry.getKey();
            Object value = propertyEntry.getValue();
            try {
                if (value == null) {
                    //ignore nulls
                } else if (Collection.class.isAssignableFrom(value.getClass())) {
                    beanMap.putAll(convertAll((Collection) value, prefix + property, cache));
                } else if (value.getClass().isArray()) {
                    beanMap.putAll(convertAll(Arrays.asList((Object[]) value), prefix + property, cache));
                } else if (Map.class.isAssignableFrom(value.getClass())) {
                    beanMap.putAll(convertMap((Map) value, prefix + property, cache));
                } else {
                    beanMap.putAll(convertObject(value, prefix + property, cache));
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        return beanMap;
    }

    private static Map<String, Object> getProperties(Object object) {
        Map<String, Object> propertyMap;
        if (object instanceof Map) {
            propertyMap = (Map) object;
        } else if (object instanceof List) {
            propertyMap = convertArray(((List) object));
        } else {
            propertyMap = getFields(object);
            //getters take precedence in case of any name collisions
            propertyMap.putAll(getGetterMethods(object));
        }

        return propertyMap;
    }

    private static Map<String, Object> convertArray(List<Object> valArray) {
        Map<String, Object> valuesMap = new HashMap<>();
        for (int currentElm = 0; currentElm < valArray.size(); currentElm++) {
            Object value = valArray.get(currentElm);
            if (value != null) {
                valuesMap.put("[" + currentElm + "]", value);
            }
        }
        return valuesMap;
    }

    private static Map<String, Object> getGetterMethods(Object object) {
        Map<String, Object> result = new HashMap<>();
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(object.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    String name = pd.getName();
                    if (!"class".equals(name)) {
                        result.put(name, invokeReaderMethod(reader, object));
                    }
                }
            }
        } catch (IntrospectionException e) {
            LOGGER.error(e);
        }
        return result;
    }

    private static Object invokeReaderMethod(Method reader, Object parameter) {
        try {
            return reader.invoke(parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.debug(e);
        }
        return null;
    }

    private static Map<String, Object> getFields(Object object) {
        return getFields(object, object.getClass());
    }

    private static Map<String, Object> getFields(Object object, Class<?> classType) {
        Map<String, Object> result = new HashMap<>();

        Class superClass = classType.getSuperclass();
        if (superClass != null) {
            result.putAll(getFields(object, superClass));
        }

        //get public fields only
        Field[] fields = classType.getFields();
        for (Field field : fields) {
            try {
                result.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                LOGGER.error(e);
                //you can choose to do something here
            }
        }
        return result;
    }

    private static Map<String, String> convertAll(Collection<Object> values, String key, Set cache) {
        Map<String, String> valuesMap = new HashMap<>();
        Object[] valArray = values.toArray();
        for (int i = 0; i < valArray.length; i++) {
            Object value = valArray[i];
            if (value != null) {
                valuesMap.putAll(convertObject(value, key + ".[" + i + "]", cache));
            }
        }
        return valuesMap;
    }

    private static Map<String, String> convertMap(Map<Object, Object> values, String key, Set cache) {
        Map<String, String> valuesMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : values.entrySet()) {
            Object thisKey = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                valuesMap.putAll(convertObject(value, key + "." + thisKey, cache));
            }
        }
        return valuesMap;
    }

    private static Map<String, String> convertObject(Object value, String key, Set cache) {
        //if this type has a registered converted, then get the string and return
        if (converter.lookup(value.getClass()) != null) {
            String stringValue = converter.convert(value);
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put(key, stringValue);
            return valueMap;
        } else {
            //otherwise, treat it as a nested bean that needs to be described itself
            return recursiveDescribe(value, key, cache);
        }
    }

}
