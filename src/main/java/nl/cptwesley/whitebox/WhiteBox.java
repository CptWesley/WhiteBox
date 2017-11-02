package nl.cptwesley.whitebox;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WhiteBox annotation declaration.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(WhiteBoxes.class)
public @interface WhiteBox {
    /**
     * Get the output name of the file.
     * @return Returns the output name of the file.
     */
    String value();

    /**
     * Gets the target class.
     * @return Returns the targeted class.
     */
    String target();

    /**
     * Gets the depth to which degree we want to whiteboxify.
     * @return Returns the depth to which degree we want to whiteboxify.
     */
    WhiteBoxDepth depth() default WhiteBoxDepth.Public;
}
