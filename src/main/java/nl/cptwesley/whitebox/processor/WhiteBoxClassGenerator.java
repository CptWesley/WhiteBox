package nl.cptwesley.whitebox.processor;

import lombok.Getter;
import nl.cptwesley.whitebox.WhiteBoxDepth;

/**
 * Class which generates code for a whiteboxed version of a targeted class.
 */
class WhiteBoxClassGenerator {

    @Getter private Class target;
    @Getter private VisibilityFilter visibilityFilter;

    /**
     * Constructor for a white box class generator.
     * @param target Target class.
     * @param depth Depth to generate to.
     */
    WhiteBoxClassGenerator(Class target, WhiteBoxDepth depth) {
        this.target = target;
        this.visibilityFilter = new VisibilityFilter(target, depth);
    }

    /**
     * Generates the source code of the whiteboxed class.
     * @return Source code of the whiteboxed class.
     */
    String generateSource() {

    }
}
