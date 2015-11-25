/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module.visitors;

import static com.fasterxml.jackson.databind.PropertyName.NO_NAME;
import static java.lang.reflect.Modifier.isPublic;
import static java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Method;
import java2typescript.jackson.module.grammar.AnyType;
import java2typescript.jackson.module.grammar.ClassType;
import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.VoidType;
import java2typescript.jackson.module.grammar.base.AbstractType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TSJsonObjectFormatVisitor extends ABaseTSJsonFormatVisitor<ClassType> implements
            JsonObjectFormatVisitor {

    private final Class clazz;

    public TSJsonObjectFormatVisitor(
                final ABaseTSJsonFormatVisitor<?> parentHolder, final String className, final Class clazz) {

        super(parentHolder);
        type = new ClassType(className);
        this.clazz = clazz;
        addPublicMethods();
    }

    private void addField(final String name, final AbstractType fieldType) {

        type.getFields().put(name, fieldType);
    }

    private boolean isAccessorMethod(final Method method, final BeanInfo beanInfo) {

        for (final PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
            if (method.equals(property.getReadMethod())) {
                return true;
            }
            if (method.equals(property.getWriteMethod())) {
                return true;
            }
        }
        return false;
    }

    private void addPublicMethods() {

        for (final Method method : this.clazz.getDeclaredMethods()) {

            // Only public
            if (!isPublic(method.getModifiers())) {
                continue;
            }

            // Exclude accessors
            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                if (isAccessorMethod(method, beanInfo)) {
                    continue;
                }
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }

            // Ignore @Transient methods
            if (method.getAnnotation(Transient.class) != null) {
                continue;
            }

            addMethod(method);
        }
    }

    private AbstractType getTSTypeForClass(final AnnotatedMember member) {

        final TypeBindings bindings =
                    new TypeBindings(TypeFactory.defaultInstance(), member.getDeclaringClass());
        final BeanProperty prop =
                    new BeanProperty.Std(member.getName(), member.getType(bindings), NO_NAME,
                                new AnnotationMap(), member, false);

        try {
            return getTSTypeForProperty(prop);
        } catch (final JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMethod(final Method method) {

        final FunctionType function = new FunctionType();

        final AnnotatedMethod annotMethod = new AnnotatedMethod(method, new AnnotationMap(), null);

        function.setResultType(getTSTypeForClass(annotMethod));

        // Récupère l'uri à partir de l'annotation de la méthode et du controller
        function.setUri(recupererUri(method));

        for (int i = 0; i < annotMethod.getParameterCount(); i++) {
            final AnnotatedParameter param = annotMethod.getParameter(i);
            String name;
            if (method.getParameters()[i] != null && method.getParameters()[i].isNamePresent()) {
                name = method.getParameters()[i].getName();
            } else {
                name = "param" + i;
            }

            function.getParameters().put(name, getTSTypeForClass(param));
        }
        this.type.getMethods().put(method.getName(), function);
    }

    /**
     * @param method
     */
    // Construit une info du type "[GET] /chemin/methode"
    private String recupererUri(final Method method) {

        final StringBuilder uri = new StringBuilder();
        if (method.isAnnotationPresent(RequestMapping.class)) {
            String methodUri = "";
            String classUri = "";

            final RequestMethod[] httpMethodes = method.getAnnotation(RequestMapping.class).method();

            for (int i = 0; i < httpMethodes.length; i++) {
                if (i == 0) {
                    uri.append("[");
                }
                uri.append(httpMethodes[i].name());
                if (i == httpMethodes.length - 1) {
                    uri.append("] ");
                } else {
                    uri.append("|");
                }
            }

            final String[] requestMappingValues = method.getAnnotation(RequestMapping.class).value();
            if (requestMappingValues.length > 0) {
                methodUri = requestMappingValues[0];
            }

            if (method.getDeclaringClass().isAnnotationPresent(RequestMapping.class)) {
                final String[] classRequestMappingValues =
                            method.getDeclaringClass().getAnnotation(RequestMapping.class).value();
                if (classRequestMappingValues.length > 0) {
                    classUri = classRequestMappingValues[0];
                }
            }

            uri.append(classUri);
            uri.append(methodUri);
        }
        return uri.length() > 0 ? uri.toString() : null;
    }

    @Override
    public void property(final BeanProperty writer) throws JsonMappingException {

        addField(writer.getName(), getTSTypeForProperty(writer));
    }

    @Override
    public void property(final String name, final JsonFormatVisitable handler, final JavaType propertyTypeHint)
        throws JsonMappingException {

        addField(name, getTSTypeForHandler(this, handler, propertyTypeHint));
    }

    @Override
    public void property(final String name) throws JsonMappingException {

        addField(name, AnyType.getInstance());
    }

    @Override
    public void optionalProperty(final BeanProperty writer) throws JsonMappingException {

        addField(writer.getName(), getTSTypeForProperty(writer));
    }

    @Override
    public void optionalProperty(final String name, final JsonFormatVisitable handler,
                final JavaType propertyTypeHint) throws JsonMappingException {

        addField(name, getTSTypeForHandler(this, handler, propertyTypeHint));
    }

    @Override
    public void optionalProperty(final String name) throws JsonMappingException {

        addField(name, AnyType.getInstance());
    }

    protected AbstractType getTSTypeForProperty(final BeanProperty writer) throws JsonMappingException {

        if (writer == null) {
            throw new IllegalArgumentException("Null writer");
        }
        final JavaType type = writer.getType();
        if (type.getRawClass().equals(Void.TYPE)) {
            return VoidType.getInstance();
        }
        try {
            final JsonSerializer<Object> ser = getSer(writer);

            if (ser != null) {
                if (type == null) {
                    throw new IllegalStateException("Missing type for property '" + writer.getName() + "'");
                }
                return getTSTypeForHandler(this, ser, type);
            } else {
                return AnyType.getInstance();
            }

        } catch (final Exception e) {
            throw new RuntimeException(String.format(//
                "Error when serializing %s, you should add a custom mapping for it", type.getRawClass()), e);
        }

    }

    protected JsonSerializer<java.lang.Object> getSer(final BeanProperty writer) throws JsonMappingException {

        JsonSerializer<Object> ser = null;
        if (writer instanceof BeanPropertyWriter) {
            ser = ((BeanPropertyWriter) writer).getSerializer();
        }
        if (ser == null) {
            ser = getProvider().findValueSerializer(writer.getType(), writer);
        }
        return ser;
    }

}
