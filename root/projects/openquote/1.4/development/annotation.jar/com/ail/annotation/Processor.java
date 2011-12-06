/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.ail.annotation;

import static javax.lang.model.SourceVersion.RELEASE_6;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;

@SupportedSourceVersion(RELEASE_6)
@SupportedAnnotationTypes({ "com.ail.annotation.ArgumentDefinition", "com.ail.annotation.CommandDefinition", "com.ail.annotation.ServiceImplementation", "com.ail.annotation.TypeDefinition" })
public class Processor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;
	private Map<String, Element> arguments = new HashMap<String, Element>();
	private Map<String, Element> commands = new HashMap<String, Element>();
	private Map<String, Element> services = new HashMap<String, Element>();
	private Map<String, Element> types = new HashMap<String, Element>();

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		try {
			localProcess(elements, env);
		} catch (IOException e) {
			error("Annotation processor failure: " + e);
		}

		return true;
	}

	private void localProcess(Set<? extends TypeElement> elements, RoundEnvironment env) throws IOException {
		for (TypeElement te : elements) {
			for (Element e : env.getElementsAnnotatedWith(te)) {
				if ("CommandDefinition".equals(te.getSimpleName().toString())) {
					commands.put(e.toString(), e);
				} else if ("ArgumentDefinition".equals(te.getSimpleName().toString())) {
					arguments.put(e.toString(), e);
				} else if ("ServiceImplementation".equals(te.getSimpleName().toString())) {
					services.put(e.toString(), e);
				} else if ("TypeDefinition".equals(te.getSimpleName().toString())) {
					types.put(e.toString(), e);
				}
			}
		}

		if (arguments.size() != 0) {
			generateArgumentImpls();
		}

		if (commands.size() != 0) {
			generateCommandImpls();
		}

		if (arguments.size()!=0 || commands.size()!=0 || types.size()!=0) {
			generateCoreDefaultConfigTypes();
		}

		arguments.clear();
		commands.clear();
		services.clear();
		types.clear();
	}

	private void generateCoreDefaultConfigTypes() throws IOException {
		OutputStream os = filer.createResource(StandardLocation.CLASS_OUTPUT, "com.ail.core", "AnnotatedTypes.xml", (Element[]) null).openOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.printf("<!-- This is a generated file. The types defined here are automatically create -->\n");
		pw.printf("<!-- by the annotation processor. Edits to this file will be lost.             -->\n");
		pw.printf("<configuration xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://www.appliedindustriallogic.com/schemas/Configuration.xsd'>\n");
		pw.printf("  <types>");
		for (TypeElement te : ElementFilter.typesIn(services.values())) {
			pw.printf("<service name='%s' builder='ClassBuilder' key='com.ail.core.command.ClassAccessor'>\n", te);
			pw.printf("  <parameter name='ServiceClass'>%s</parameter>\n", te);
			pw.printf("</service>\n");
		}

		for (TypeElement te : ElementFilter.typesIn(commands.values())) {
			CommandDefinition anno = te.getAnnotation(CommandDefinition.class);
			TypeMirror value=null;
			try {
				anno.defaultServiceClass();
			} 
			catch(MirroredTypeException e) {
				value=e.getTypeMirror();
			}

			if (anno != null && value!=null) {
				pw.printf("<command name='%s' builder='ClassBuilder' key='%sImpl'>\n", te, te);
				pw.printf("  <parameter name='Service'>%s</parameter>\n", value);
				pw.printf("</command>\n");
			}
		}

		for (TypeElement te : ElementFilter.typesIn(arguments.values())) {
			pw.printf("<type name='%s' builder='ClassBuilder' key='%sImpl'/>\n", te, te);
		}
		
		for (TypeElement te : ElementFilter.typesIn(types.values())) {
			pw.printf("<type name='%s' builder='ClassBuilder' key='%s'/>\n", te, te);
		}
		
		pw.printf("  </types>");
		pw.printf("</configuration>");

		pw.close();
	}

	private void generateArgumentImpls() throws IOException {
		for (TypeElement te : ElementFilter.typesIn(arguments.values())) {
			if (!te.getQualifiedName().toString().endsWith("Argument")) {
				error("Argument class name '" + te.getQualifiedName() + "' does not end in \"Argument\"");
			}
			generateArgumentImpl(te);
		}
	}

	private void generateCommandImpls() throws IOException {
		for (TypeElement te : ElementFilter.typesIn(commands.values())) {
			if (!te.getQualifiedName().toString().endsWith("Command")) {
				error("Command class name '" + te.getQualifiedName() + "' does not end in \"Command\"");
			}
			generateCommandImpl(te);
		}
	}

	/**
	 * Generate the ArgImp class for the command.
	 * 
	 * @param te
	 * @throws IOException
	 */
	private void generateArgumentImpl(TypeElement te) throws IOException {
		// com.ail.core.thing.ThingCommand -> com.ail.core.thing.ThingArgImp
		String qualifiedClassName = te.getQualifiedName().toString().replaceFirst("Argument$", "ArgumentImpl");
		// ThingCommand -> ThingArgImp
		String simpleClassName = te.getSimpleName().toString().replaceFirst("Argument$", "ArgumentImpl");

		OutputStream os = filer.createSourceFile(qualifiedClassName, (Element[]) null).openOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.printf("// Generated by %s\n", Processor.class.getName());
		pw.printf("\n");
		pw.printf("package %s;\n", qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf('.')));
		pw.printf("\n");
		pw.printf("import com.ail.core.command.ArgumentImpl;\n");
		pw.printf("\n");
		pw.printf("public class %s extends ArgumentImpl implements %s {\n", simpleClassName, te);

		for (ExecutableElement ee : ElementFilter.methodsIn(te.getEnclosedElements())) {
			if ("get".equals(ee.getSimpleName().subSequence(0, 3))) {
				String type = ee.getReturnType().toString();
				String methodName = ee.getSimpleName().toString();
				String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				pw.printf("  private %s %s;\n", type, fieldName);
			}
		}

		pw.printf("\n");

		for (ExecutableElement ee : ElementFilter.methodsIn(te.getEnclosedElements())) {
			String methodName = ee.getSimpleName().toString();
			String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

			if (methodName.startsWith("get")) {
				String type = ee.getReturnType().toString();

				pw.printf("  public %s %s() {\n", type, methodName);
				pw.printf("    return %s;\n", fieldName);
				pw.printf("  }\n\n");
			} else {
				String type = ee.getParameters().get(0).asType().toString();

				pw.printf("  public void %s(%s %s) {\n", methodName, type, fieldName);
				pw.printf("    this.%1$s=%1$s;\n", fieldName);
				pw.printf("  }\n\n");
			}
		}

		pw.printf("}\n");

		pw.close();
	}

	/**
	 * Generate CommandImp for command
	 * 
	 * @param te
	 * @throws IOException
	 */
	private void generateCommandImpl(TypeElement te) throws IOException {
		// com.ail.core.thing.ThingCommand -> com.ail.core.thing.ThingCommandImp
		String qualifiedClassName = te.getQualifiedName() + "Impl";
		// ThingCommand -> ThingCommandImp
		String simpleClassName = te.getSimpleName() + "Impl";
		// com.ail.core.thing.ThingCommand ->
		// com.ail.core.thing.ThingArgumentImpl
		String qualifiedArgumentImpl = te.getQualifiedName().toString().replaceFirst("Command$", "ArgumentImpl");
		// com.ail.core.thing.ThingCommand -> com.ail.core.thing.ThingArgument
		String qualifiedArgument = te.getQualifiedName().toString().replaceFirst("Command$", "Argument");

		OutputStream os = filer.createSourceFile(qualifiedClassName, (Element[]) null).openOutputStream();
		PrintWriter pw = new PrintWriter(os);

		pw.printf("// Generated by %s\n", Processor.class.getName());
		pw.printf("\n");
		pw.printf("package %s;\n\n", qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf('.')));
		pw.printf("\n");
		pw.printf("import com.ail.core.command.Accessor;\n");
		pw.printf("import com.ail.core.command.Argument;\n");
		pw.printf("import com.ail.core.command.CommandImpl;\n");
		pw.printf("\n");
		pw.printf("public class %s extends CommandImpl implements %s {\n", simpleClassName, te);
		pw.printf("  private Accessor accessor;\n");
		pw.printf("  private %s args;\n", qualifiedArgument);
		pw.printf("\n");
		pw.printf("  public %s() {\n", simpleClassName);
		pw.printf("    super();\n");
		pw.printf("    args=new %s();\n", qualifiedArgumentImpl);
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public %s(Accessor accessor, %s args) {\n", simpleClassName, qualifiedArgument);
		pw.printf("    setAccessor(accessor);\n");
		pw.printf("    setArgs(args);\n");
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public Accessor getAccessor() {\n");
		pw.printf("    return accessor;\n");
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public void setAccessor(Accessor accessor) {\n");
		pw.printf("    this.accessor=accessor;\n");
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public void setService(Accessor accessor) {\n");
		pw.printf("    this.accessor=accessor;\n");
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public void setArgs(Argument arg) {\n");
		pw.printf("    this.args=(%s)arg;\n", qualifiedArgument);
		pw.printf("  }\n");
		pw.printf("\n");
		pw.printf("  public Argument getArgs() {\n");
		pw.printf("    return args;\n");
		pw.printf("  }\n");
		pw.printf("\n");

		Element interfaceElement = arguments.get(qualifiedArgument);

		for (ExecutableElement ee : ElementFilter.methodsIn(interfaceElement.getEnclosedElements())) {
			String methodName = ee.getSimpleName().toString();
			String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

			if (methodName.startsWith("get")) {
				String type = ee.getReturnType().toString();

				pw.printf("  public %s %s() {\n", type, methodName);
				pw.printf("    return args.%s();\n", methodName);
				pw.printf("  }\n\n");
			} else {
				String type = ee.getParameters().get(0).asType().toString();

				pw.printf("  public void %s(%s %s) {\n", methodName, type, fieldName);
				pw.printf("    args.%s(%s);\n", methodName, fieldName);
				pw.printf("  }\n\n");
			}
		}

		pw.printf("}");

		pw.close();
	}

	private void error(String message) {
		messager.printMessage(Kind.ERROR, message);
	}
}
