package nl.cptwesley.whitebox.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Getter;
import nl.cptwesley.whitebox.WhiteBoxDepth;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Class which generates code for a whiteboxed version of a targeted class.
 */
class WhiteBoxClassGenerator {

    @Getter private Class target;
    @Getter private VisibilityFilter visibilityFilter;
    @Getter private String name;

    /**
     * Constructor for a white box class generator.
     * @param target Target class.
     * @param depth Depth to generate to.
     * @param name Name of the new class.
     */
    WhiteBoxClassGenerator(Class target, WhiteBoxDepth depth, String name) {
        this.target = target;
        this.visibilityFilter = new VisibilityFilter(target, depth);
        this.name = name;
    }

    /**
     * Generates the source code of the whiteboxed class.
     * @return Source code of the whiteboxed class.
     */
    TypeSpec generateSource() {
        List<Constructor> constructors = visibilityFilter.getConstructors();
        List<Method> methods = visibilityFilter.getMethods();

        TypeSpec.Builder whiteBoxedClass = TypeSpec
                .classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(Object.class, "object", Modifier.PRIVATE);

        for (Constructor constructor : constructors) {
            whiteBoxedClass.addMethod(getConstructorSpec(constructor));
        }

        for (Method method : methods) {
            whiteBoxedClass.addMethod(getMethodSpec(method));
        }

        return whiteBoxedClass.build();
    }

    /**
     * Gets a constructor method spec.
     * @param constructor Constructor to generate method spec for.
     * @return Returns MethodSpec of the provided constructor.
     */
    private MethodSpec getConstructorSpec(Constructor constructor) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; ++i) {
            builder.addParameter(parameterTypes[i], "arg" + i);
        }

        builder.addCode("try {\n")
                .addStatement(
                        "Class c = Class.forName(\"$L\")",
                        target.getName())
                .addStatement("java.lang.reflect.Constructor cons = c.getDeclaredConstructor($L)",
                        getConstructorParameterTypeString(constructor.getParameterTypes()))
                .addStatement("cons.setAccessible(true)")
                .addStatement("this.object = cons.newInstance($L)",
                        getConstructorCallingParameterString(constructor.getParameterTypes()))
                .addCode("} catch (Exception e) {\n")
                .addStatement("throw new RuntimeException()")
                .addCode("}\n");

        return builder.build();
    }

    /**
     * Generate constructor argument types.
     * @param parameterTypes Parameter types to generate for.
     * @return String of parameters types passed to a constructor.
     */
    private String getConstructorParameterTypeString(Class<?>[] parameterTypes) {
        String s = getParameterTypeString(parameterTypes);
        if (s.length() == 0) {
            return "";
        }

        return s.substring(2, s.length());
    }

    /**
     * Generate constructor arguments.
     * @param parameterTypes Parameter types to generate for.
     * @return String of parameters passed to a constructor.
     */
    private String getConstructorCallingParameterString(Class<?>[] parameterTypes) {
        String s = getCallingParameterString(parameterTypes);
        if (s.length() == 0) {
            return "";
        }

        return s.substring(2, s.length());
    }

    /**
     * Get method spec from javapoet.
     * @param method Method to create Method spec for.
     * @return Method spec of given method.
     */
    private MethodSpec getMethodSpec(Method method) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(method.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(method.getReturnType());

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; ++i) {
            builder.addParameter(parameterTypes[i], "arg" + i);
        }

        builder.addCode("try {\n")
                .addStatement(
                "java.lang.reflect.Method m = object.getClass().getDeclaredMethod(\"$L\"$L)",
                 method.getName(), getParameterTypeString(parameterTypes))
                .addStatement("m.setAccessible(true)")
                .addStatement("$Lm.invoke(object$L)",
                        getReturnString(method),
                        getCallingParameterString(parameterTypes))
                .addCode("} catch (Exception e) {\n")
                .addStatement("throw new RuntimeException()")
                .addCode("}\n");

        return builder.build();
    }

    /**
     * Generates a string containing all parameter types, used to target methods.
     * @param parameterTypes Array of parameter types.
     * @return Returns a string of the joined parameter types.
     */
    private String getParameterTypeString(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        int paramsAmount = parameterTypes.length;
        if (paramsAmount > 0) {
            sb.append(", ");
        }
        for (int i = 0; i < paramsAmount; ++i) {
            sb.append(parameterTypes[i].getName())
                    .append(".class");

            if (i != paramsAmount - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Generates calling parameter string.
     * @param parameterTypes Array of parameter types.
     * @return Returns a string of the joined parameter names.
     */
    private String getCallingParameterString(Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder();
        int paramsAmount = parameterTypes.length;
        if (paramsAmount > 0) {
            sb.append(", ");
        }
        for (int i = 0; i < paramsAmount; ++i) {
            sb.append("arg")
                .append(i);

            if (i != paramsAmount - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Generates a string return type.
     * @param method Method to create it for.
     * @return String containing the required head for correct functioning of the method.
     */
    private String getReturnString(Method method) {
        if (method.getReturnType().equals(Void.TYPE)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        sb.append("return (")
                .append(method.getReturnType().getName())
                .append(')');

        return sb.toString();
    }
}
