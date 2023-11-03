package framework.utilities.graphqlmapper.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation.
 * <p>Used to mark parametrized objects o</p>
 * <p>Value hold the arguments - set without braces e.g. clientid: $clientId, endpoint: $endpoint</p>
 * <p> replaceArgs if true the argumentsToString() method will be executed from your Dto in order to replace value</p>
 * <p>In your Dto implement argumentsToString() that returns string based value that should replace arguments</p>
 * <p>See IntegrationStatusDataListDto.class as example</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GraphQlArgs {

    /**
     * Get the name or GraphQL argument.
     * @return string
     */
    String argName() default "";

    /**
     * Replace arguments or not, default is false.
     * @return boolean
     */
    boolean replaceArgs() default false;
}
