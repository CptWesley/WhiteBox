package nl.cptwesley.whitebox.processor;

/**
 * Dummy class used for testing.
 */
public class ExampleTargetClass {

    private String name;

    /**
     * Empty public constructor.
     */
    public ExampleTargetClass() {
        this.name = "default";
    }

    /**
     * Public constructor.
     * @param name Arg0.
     */
    public ExampleTargetClass(String name) {
        this.name = name;
    }

    /**
     * Protected constructor.
     * @param name Arg0.
     * @param a1 Arg1.
     */
    protected ExampleTargetClass(String name, int a1) {
        this.name = name;
    }

    /**
     * Package constructor.
     * @param name Arg0.
     * @param a1 Arg1.
     * @param a2 Arg2.
     */
    ExampleTargetClass(String name, int a1, int a2) {
        this.name = name;
    }

    /**
     * Private constructor.
     * @param name Arg0.
     * @param a1 Arg1.
     * @param a2 Arg2.
     * @param a3 Arg3.
     */
    private ExampleTargetClass(String name, int a1, int a2, int a3) {
        this.name = name;
    }

    /**
     * Public method.
     * @return value.
     */
    public int getPublicValue() {
        return -1;
    }

    /**
     * Protected method.
     * @return value.
     */
    protected int getProtectedValue() {
        return -1;
    }

    /**
     * Package method.
     * @return value.
     */
    int getPackageValue() {
        return -1;
    }

    /**
     * Private method.
     * @return value.
     */
    private int getPrivateValue() {
        return -1;
    }

    /**
     * Private return string method.
     * @return Returns the name of this class.
     */
    private String getName() {
        return this.name;
    }
}
