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
package java2typescript.jackson.module.grammar;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class FunctionType extends AbstractType {

    private LinkedHashMap<String, AbstractType> parameters = new LinkedHashMap<String, AbstractType>();

    private AbstractType resultType;

    private String uri;

    /** By default, printed as lambda function type (with =>) */
    @Override
    public void write(final Writer writer) throws IOException {

        write(writer, true);
    }

    /** Write as non lambda : func(a:string) : string */
    public void writeNonLambda(final Writer writer) throws IOException {

        write(writer, false);
    }

    private void write(final Writer writer, final boolean lambdaSyntax) throws IOException {

        writer.write("(");
        int i = 1;
        for (final Entry<String, AbstractType> entry : parameters.entrySet()) {
            writer.write(entry.getKey());
            writer.write(": ");
            entry.getValue().write(writer);
            if (i < parameters.size()) {
                writer.write(", ");
            }
            i++;
        }
        writer.write(")" + (lambdaSyntax ? "=> " : ": "));
        resultType.write(writer);
    }

    public LinkedHashMap<String, AbstractType> getParameters() {

        return parameters;
    }

    public AbstractType getResultType() {

        return resultType;
    }

    public void setResultType(final AbstractType resultType) {

        this.resultType = resultType;
    }

    /**
     * @return uri
     */
    public String getUri() {

        return uri;
    }

    /**
     * @param uri
     *            uri
     */
    public void setUri(final String uri) {

        this.uri = uri;
    }

    /**
     * @param parameters
     *            parameters
     */
    public void setParameters(final LinkedHashMap<String, AbstractType> parameters) {

        this.parameters = parameters;
    }

}
