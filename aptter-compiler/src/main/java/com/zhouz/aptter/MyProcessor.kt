package com.zhouz.aptter

import com.google.auto.service.AutoService
import java.io.IOException
import java.io.Writer
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


/**
 * @author:zhouz
 * @date: 2024/2/29 18:53
 */
//@AutoService(Processor.class) 这个有木有？这是一个注解处理器，是Google开发的，
//用来生成META-INF/services/javax.annotation.processing.Processor文件的。
//引入方式     compile 'com.google.auto.service:auto-service:1.0-rc2'
@AutoService(Processor::class)
class MyProcessor : AbstractProcessor() {
    private var mTypeUtils: Types? = null
    private var mElementUtils: Elements? = null
    private var mFiler: Filer? = null
    private var mMessager: Messager? = null

    private var log: Logger? = null

    override fun getSupportedAnnotationTypes(): Set<String> {
        val annotations: MutableSet<String> = LinkedHashSet()
        annotations.add(MethodProcessor::class.java.canonicalName)
        return annotations
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        log = processingEnv.messager?.let { Logger(it) }
        mTypeUtils = processingEnv.typeUtils
        mElementUtils = processingEnv.elementUtils
        mFiler = processingEnv.filer
        mMessager = processingEnv.messager
    }

    override fun process(annotations: Set<TypeElement?>?, roundEnv: RoundEnvironment): Boolean {
        // // 遍历所有被注解了的元素
        log?.info("process")
        for (annotatedElement in roundEnv.getElementsAnnotatedWith(MethodProcessor::class.java)) {
            if (annotatedElement.kind !== ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s", MethodProcessor::class.java.getSimpleName())
                return true
            }
            // //解析，并生成代码
            analysisAnnotated(annotatedElement)
        }
        return false
    }

    private fun error(e: Element, msg: String, vararg args: Any) {
        mMessager?.printMessage(Diagnostic.Kind.ERROR, String.format(msg, *args), e)
    }

    private fun analysisAnnotated(classElement: Element) {
        val annotation: MethodProcessor = classElement.getAnnotation(MethodProcessor::class.java)
        val name: String = annotation.name
        log?.info("analysisAnnotated name:$name")
        val newClassName = name + SUFFIX
        val builder = StringBuilder()
            .append(
                """
                package $packageName;
                
                
                """.trimIndent()
            )
            .append("public class ")
            .append(newClassName)
            .append(" {\n\n") // open class
            .append("\tpublic String getMessage() {\n") // open method
            .append("\t\treturn \"")

        // this is appending to the return statement
        builder.append(retStr).append(" !\\n")
        builder.append("\";\n") // end return
            .append("\t}\n") // close method
            .append("}\n") // close class
        try { // write the file
            val source = mFiler!!.createSourceFile("$packageName.$newClassName")
            val writer: Writer = source.openWriter()
            writer.write(builder.toString())
            writer.flush()
            writer.close()
        } catch (e: IOException) {
        }
    }

    companion object {
        private const val SUFFIX = "Test"
        private const val packageName = "com.zhouz.aptdemo"
        private const val retStr = ""
    }
}