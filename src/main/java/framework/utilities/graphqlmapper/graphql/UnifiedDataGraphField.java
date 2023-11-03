package framework.utilities.graphqlmapper.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import framework.utilities.graphqlmapper.graphql.DataGraphSerializer.None;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnifiedDataGraphField {

    /**
     * The value for UnifiedDataGraphField.
     *
     * @return string
     */
    String value() default "";

    /**
     * Specify custom serializer.
     *
     * @return DataGraphSerializer
     */
    Class<? extends DataGraphSerializer> using() default None.class;
}
