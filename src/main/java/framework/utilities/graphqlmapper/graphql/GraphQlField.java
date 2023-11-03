package framework.utilities.graphqlmapper.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines field in graphQL query. Field or class, annotated with the @GraphQLObject, will be separated by comma: ,
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GraphQlField {

    /**
     * The name of graphql field.
     * @return string
     */
    String name() default "";
}
