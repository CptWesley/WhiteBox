package nl.cptwesley.whitebox.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import nl.cptwesley.whitebox.WhiteBox;
import nl.cptwesley.whitebox.WhiteBoxes;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

/**
 * Annotation processor for the @WhiteBox annotation.
 */
@SupportedAnnotationTypes({
        "nl.cptwesley.whitebox.WhiteBox",
        "nl.cptwesley.whitebox.WhiteBoxes"
        })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class WhiteBoxProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        this.filer = environment.getFiler();
        this.messager = environment.getMessager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment environment) {
        for (Element element : environment.getElementsAnnotatedWith(WhiteBox.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Can only be applied to a class.");
                return false;
            }
            TypeElement typeElement = (TypeElement) element;
            WhiteBox annotation = element.getAnnotation(WhiteBox.class);
            if (!generateClass(typeElement, annotation)) {
                return true;
            }
        }
        for (Element element : environment.getElementsAnnotatedWith(WhiteBoxes.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Can only be applied to a class.");
                return false;
            }
            TypeElement typeElement = (TypeElement) element;
            for (WhiteBox annotation : element.getAnnotationsByType(WhiteBox.class)) {
                if (!generateClass(typeElement, annotation)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generates class source file.
     * @param element Element that was annotated.
     * @param annotation Annotation on the method.
     */
    private boolean generateClass(TypeElement element, WhiteBox annotation) {
        TypeSpec newClass = generateTypeSpec(element, annotation);
        try {
            JavaFile.builder(element.getEnclosingElement().toString(), newClass)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error operating on file.");
            return false;
        }
        return true;
    }

    /**
     * Generates a Java Poet TypeSpec.
     * @param element Element that was annotated.
     * @param annotation Annotation on the class.
     * @return
     */
    private static TypeSpec generateTypeSpec(TypeElement element, WhiteBox annotation) {
        try {
            Class targetClass = Class.forName(annotation.target());
            WhiteBoxClassGenerator generator = new WhiteBoxClassGenerator(
                    targetClass,
                    annotation.depth(),
                    annotation.value());
            return generator.generateSource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TypeSpec.classBuilder(element.getSimpleName().toString()).build();
    }
}
