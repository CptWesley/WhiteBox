package nl.cptwesley.whitebox;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WhiteBox annotation declaration.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface WhiteBox {
    String value();
    String target();
    WhiteBoxDepth depth() default WhiteBoxDepth.Public;
}
