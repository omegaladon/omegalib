package me.omega.omegalib.plugin.register;

import com.google.auto.service.AutoService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("me.omega.omegalib.plugin.register.Plugin")
public class PluginProcessor extends AbstractProcessor {

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Plugin processor initialized");
        this.processingEnv = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations.size() == 0) {
            return true;
        }

        if (annotations.size() > 1) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Found 2 or more classes annotated with " +
                                                                            "@Plugin.");
            return true;
        }

        Element element = roundEnv.getElementsAnnotatedWith(Plugin.class).iterator().next();
        Map<String, Object> data = new HashMap<>();
        Plugin plugin = element.getAnnotation(Plugin.class);

        data.put("name", plugin.name());
        data.put("version", plugin.version());
        data.put("main", plugin.main());
        data.put("description", plugin.description());
        data.put("authors", plugin.authors());
        data.put("website", plugin.website());
        data.put("api-version", plugin.apiVersion().toString());
        data.put("load", plugin.load().toString());
        data.put("prefix", plugin.prefix());
        data.put("depend", plugin.depend());
        data.put("softDepend", plugin.softDepend());
        data.put("loadBefore", plugin.loadBefore());

        try {
            Yaml yaml = new Yaml();
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin" +
                                                                                                             ".yml");

            try (Writer writer = resource.openWriter(); BufferedWriter bw = new BufferedWriter(writer)) {
                yaml.dump(data, bw);
                bw.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;

    }

}
