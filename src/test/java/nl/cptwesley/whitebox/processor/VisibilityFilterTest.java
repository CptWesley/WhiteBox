package nl.cptwesley.whitebox.processor;

import nl.cptwesley.whitebox.WhiteBoxDepth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the VisibilityFilter class.
 */
class VisibilityFilterTest {

    private VisibilityFilter filter;

    /**
     * Checks if target and depth are correctly set and returned.
     */
    @Test
    void targetDepthTest() {
        filter = new VisibilityFilter(TestDummy.class, WhiteBoxDepth.Public);
        assertThat(filter.getDepth()).isSameAs(WhiteBoxDepth.Public);
        assertThat(filter.getTarget()).isSameAs(TestDummy.class);
    }

    /**
     * Checks if the correct amount of public construcotrs are returned.
     */
    @ParameterizedTest
    @CsvSource({
            "Public, 2",
            "Protected, 3",
            "Package, 4",
            "Private, 5"
    })
    void visibleConstructorsTest(WhiteBoxDepth depth, int amount) {
        filter = new VisibilityFilter(TestDummy.class, depth);
        assertThat(filter.getConstructors().size()).isEqualTo(amount);
    }

    /**
     * Checks if the correct amount of public methods are returned.
     */
    @ParameterizedTest
    @CsvSource({
            "Public, 2",
            "Protected, 3",
            "Package, 4"
    })
    void visibleMethodsTest(WhiteBoxDepth depth, int amount) {
        filter = new VisibilityFilter(TestDummy.class, depth);
        assertThat(filter.getMethods().size()).isEqualTo(amount);
    }
}
