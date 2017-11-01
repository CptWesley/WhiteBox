package nl.cptwesley.whitebox.processor;

import com.google.auto.service.AutoService;
import nl.cptwesley.whitebox.WhiteBox;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Annotation processor for the @WhiteBox annotation.
 */
@SupportedAnnotationTypes({"WhiteBox"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class WhiteBoxProcessor extends AbstractProcessor {

    private Types types;
    private Elements elements;
    private Filer filer;
    private Messager messager;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);
        this.types = environment.getTypeUtils();
        this.elements = environment.getElementUtils();
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
                return true;
            }

            TypeElement typeElement = (TypeElement) element;
        }
    }
}
