package nl.cptwesley.whitebox.processor;


import lombok.Getter;
import nl.cptwesley.whitebox.WhiteBoxDepth;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter that only gets the types with right visibility.
 */
class VisibilityFilter {

    @Getter private Class target;
    @Getter private WhiteBoxDepth depth;

    /**
     * Constructor for a visibility filter.
     * @param target Class to filter.
     * @param depth Depth to filter to.
     */
    VisibilityFilter(Class target, WhiteBoxDepth depth) {
        this.target = target;
        this.depth = depth;
    }

    /**
     * Gets a list of visible constructors.
     * @return List of visible constructors.
     */
    List<Constructor> getConstructors() {
        List<Constructor> constructors = new ArrayList<>();

        for (Constructor constructor : target.getDeclaredConstructors()) {
            int modifier = constructor.getModifiers();
            if (isVisible(modifier)) {
                constructors.add(constructor);
            }
        }
        return constructors;
    }

    /**
     * Checks if a certain type is visible given the white box depth.
     * @param modifier Given white box depth.
     * @return True if visible, false otherwise.
     */
    @SuppressWarnings({"magicnumber", "fallthrough"})
    private boolean isVisible(int modifier) {
        switch (depth.ordinal()) {
            case 3:
                if (Modifier.isPrivate(modifier)) {
                    return true;
                }
            case 2:
                if (Modifier.isProtected(modifier)) {
                    return true;
                }
            case 1:
                if (!Modifier.isPrivate(modifier)
                        && !Modifier.isProtected(modifier)
                        && !Modifier.isPublic(modifier)) {
                    return true;
                }
            case 0:
                if (Modifier.isPublic(modifier)) {
                    return true;
                }
            default:
                return false;
        }
    }

}
