package framework.utilities.graphqlmapper.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GraphQLConvert parser will check: Provided query Dto should be marked with @GraphQLQuery annotation. Needs to
 * identify root query class in packages. Should be only for the first class in a whole query. Same behavior as for a
 * GraphQLObject, expect first class verification by parser.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GraphQlQuery {

    /**
     * Get the name of graphql query.
     *
     * @return string
     */
    public String name() default "";
}
