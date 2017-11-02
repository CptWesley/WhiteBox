package nl.cptwesley.whitebox;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WhiteBox annotation collection declaration.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface WhiteBoxes {
    /**
     * Gets a container of multiple @WhiteBox annotations.
     * @return Returns a container of multiple @WhiteBox annotations.
     */
    WhiteBox[] value();
}
