package framework.bean.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SortBy {

    /**
     * Defines a name of the field that will be used to sort collection.
     * @return String value, the field name
     */
    String value() default "";
}
