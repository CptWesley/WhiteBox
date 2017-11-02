package nl.cptwesley.whitebox.processor;

import com.squareup.javapoet.TypeSpec;
import nl.cptwesley.whitebox.WhiteBoxDepth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the WhiteBoxClassGenerator class.
 */
public class WhiteBoxClassGeneratorTest {
    // TODO: More/better testing.

    private static final String CLASS_NAME = "GeneratedClass";

    private WhiteBoxClassGenerator generator;

    /**
     * Sets up testing environment before each test.
     */
    @BeforeEach
    void setup() {
        generator = new WhiteBoxClassGenerator(
                TestDummy.class,
                WhiteBoxDepth.Public,
                CLASS_NAME);
    }

    /**
     * Checks if getters are working correctly.
     */
    @Test
    void getterTest() {
        assertThat(generator.getName()).isEqualTo(CLASS_NAME);
        assertThat(generator.getTarget()).isSameAs(TestDummy.class);
    }

    /**
     * Checks if visibility filter is set up properly.
     */
    @Test
    void filterTest() {
        VisibilityFilter filter = generator.getVisibilityFilter();
        assertThat(filter.getTarget()).isEqualTo(TestDummy.class);
        assertThat(filter.getDepth()).isEqualTo(WhiteBoxDepth.Public);
    }

    /**
     * Checks that the generated type spec contains the necessary data.
     */
    @Test
    void generationTest() {
        TypeSpec result = generator.generateSource();

        assertThat(result.name).isEqualTo(CLASS_NAME);
        assertThat(result.modifiers).contains(Modifier.PUBLIC)
                                    .contains(Modifier.FINAL);
        assertThat(result.methodSpecs).matches(t ->
                t.stream().anyMatch(o -> o.name.equals("getPublicValue")));
        assertThat(result.methodSpecs).matches(t ->
                t.stream().allMatch(o -> o.modifiers.contains(Modifier.PUBLIC)));
    }

}
