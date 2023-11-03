package framework.utilities.graphqlmapper.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines object in graphQL query. Field or class, annotated with the @GraphQLObject. will be surrounded in curly
 * braces: { ... }.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GraphQlObject {

    /**
     * The name of graphql object.
     *
     * @return string
     */
    String name() default "";

    /**
     * treat as empty or not.
     *
     * @return boolean
     */
    boolean empty() default false;
}
