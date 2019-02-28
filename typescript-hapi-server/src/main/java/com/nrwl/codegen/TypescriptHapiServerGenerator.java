package io.swagger.codegen.languages;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.utils.SemVer;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;

public class TypescriptHapiServerGenerator extends AbstractTypeScriptClientCodegen {
    public TypescriptHapiServerGenerator() {
        super();
        this.outputFolder = "generated-code/typescript-hapi-server";

        embeddedTemplateDir = templateDir = "typescript-hapi-server";
        modelTemplateFiles.put("model.mustache", ".ts");
        apiTemplateFiles.put("api.plugin.mustache", ".ts");
        languageSpecificPrimitives.add("Blob");
        typeMapping.put("file", "Blob");
        apiPackage = "api";
        modelPackage = "model";
    }

    @Override
    protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, ModelImpl swaggerModel) {
        codegenModel.additionalPropertiesType = getTypeDeclaration(swaggerModel.getAdditionalProperties());
        addImport(codegenModel, codegenModel.additionalPropertiesType);
    }

    @Override
    public String getName() {
        return "typescript-hapi-server";
    }

    @Override
    public String getHelp() {
        return "Generates a TypeScript Hapi Plugin.";
    }

    @Override
    public void processOpts() {
        super.processOpts();
        supportingFiles.add(
                new SupportingFile("models.mustache", modelPackage().replace('.', File.separatorChar), "models.ts"));
        supportingFiles
                .add(new SupportingFile("apis.mustache", apiPackage().replace('.', File.separatorChar), "api.ts"));
        supportingFiles.add(new SupportingFile("index.mustache", getIndexDirectory(), "index.ts"));
    }

    private String getIndexDirectory() {
        String indexPackage = modelPackage.substring(0, Math.max(0, modelPackage.lastIndexOf('.')));
        return indexPackage.replace('.', File.separatorChar);
    }

    @Override
    public boolean isDataTypeFile(final String dataType) {
        return dataType != null && dataType.equals("Blob");
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof FileProperty) {
            return "Blob";
        } else if (p instanceof ObjectProperty) {
            return "any";
        } else {
            return super.getTypeDeclaration(p);
        }
    }


    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        if (isLanguagePrimitive(swaggerType) || isLanguageGenericType(swaggerType)) {
            return swaggerType;
        }
        applyLocalTypeMapping(swaggerType);
        return swaggerType;
    }

    private String applyLocalTypeMapping(String type) {
        if (typeMapping.containsKey(type)) {
            type = typeMapping.get(type);
        }
        return type;
    }

    private boolean isLanguagePrimitive(String type) {
        return languageSpecificPrimitives.contains(type);
    }

    private boolean isLanguageGenericType(String type) {
        for (String genericType : languageGenericTypes) {
            if (type.startsWith(genericType + "<")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        parameter.dataType = applyLocalTypeMapping(parameter.dataType);
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> operations) {
        Map<String, Object> objs = (Map<String, Object>) operations.get("operations");

        // Add filename information for api imports
        objs.put("apiFilename", getApiFilenameFromClassname(objs.get("classname").toString()));
        List<CodegenOperation> ops = (List<CodegenOperation>) objs.get("operation");
        for (CodegenOperation op : ops) {
                op.httpMethod = op.httpMethod.toLowerCase(Locale.ENGLISH);
                objs.put("httpMethodUpperCase", op.httpMethod.toUpperCase(Locale.ENGLISH));
         // Prep a string buffer where we're going to set up our new version of the string.
            StringBuilder pathBuffer = new StringBuilder();
            StringBuilder parameterName = new StringBuilder();
            int insideCurly = 0;

            // Iterate through existing string, one character at a time.
            for (int i = 0; i < op.path.length(); i++) {
                switch (op.path.charAt(i)) {
                case '{':
                    // We entered curly braces, so track that.
                    insideCurly++;

                    // Add the more complicated component instead of just the brace.
                    pathBuffer.append("${encodeURIComponent(String(");
                    break;
                case '}':
                    // We exited curly braces, so track that.
                    insideCurly--;

                    // Add the more complicated component instead of just the brace.
                    CodegenParameter parameter = findPathParameterByName(op, parameterName.toString());
                    pathBuffer.append(toVarName(parameterName.toString()));
                    if (parameter != null && parameter.isDateTime) {
                        pathBuffer.append(".toISOString()");
                    }
                    pathBuffer.append("))}");
                    parameterName.setLength(0);
                    break;
                default:
                    char nextChar = op.path.charAt(i);
                    if (insideCurly > 0) {
                        parameterName.append(nextChar);
                    } else {
                        pathBuffer.append(nextChar);
                    }
                    break;
                }
            }

            // Overwrite path to TypeScript template string, after applying everything we just did.
            op.path = pathBuffer.toString();
        }

        // Add additional filename information for model imports in the services
        List<Map<String, Object>> imports = (List<Map<String, Object>>) operations.get("imports");
        for (Map<String, Object> im : imports) {
            im.put("filename", im.get("import"));
            im.put("classname", getModelnameFromModelFilename(im.get("filename").toString()));
        }

        return operations;
    }

    /**
     * Finds and returns a path parameter of an operation by its name
     * @param operation
     * @param parameterName
     * @return
     */
    private CodegenParameter findPathParameterByName(CodegenOperation operation, String parameterName) {
        for(CodegenParameter param : operation.pathParams) {
            if (param.baseName.equals(parameterName)) {
                return param;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> postProcessModels(Map<String, Object> objs) {
        Map<String, Object> result = super.postProcessModels(objs);

        return postProcessModelsEnum(result);
    }

    @Override
    public Map<String, Object> postProcessAllModels(Map<String, Object> objs) {
        Map<String, Object> result = super.postProcessAllModels(objs);

        for (Map.Entry<String, Object> entry : result.entrySet()) {
            Map<String, Object> inner = (Map<String, Object>) entry.getValue();
            List<Map<String, Object>> models = (List<Map<String, Object>>) inner.get("models");
            for (Map<String, Object> mo : models) {
                CodegenModel cm = (CodegenModel) mo.get("model");
                
                // Add additional filename information for imports
                mo.put("tsImports", toTsImports(cm, cm.imports));
            }
        }
        return result;
    }

    private List<Map<String, String>> toTsImports(CodegenModel cm, Set<String> imports) {
        List<Map<String, String>> tsImports = new ArrayList<>();
        for (String im : imports) {
            if (!im.equals(cm.classname)) {
                HashMap<String, String> tsImport = new HashMap<>();
                tsImport.put("classname", im);
                tsImport.put("filename", toModelFilename(im));
                tsImports.add(tsImport);
            }
        }
        return tsImports;
    }

    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "DefaultPlugin";
        }
        return initialCaps(name) + "Plugin";
    }

    @Override
    public String toApiFilename(String name) {
        if (name.length() == 0) {
            return "default.plugin";
        }
        return camelize(name, true) + ".plugin";
    }

    @Override
    public String toApiImport(String name) {
        return apiPackage() + "/" + toApiFilename(name);
    }

    @Override
    public String toModelFilename(String name) {
        return camelize(toModelName(name), true);
    }

    @Override
    public String toModelImport(String name) {
        return modelPackage() + "/" + toModelFilename(name);
    }

    private String getApiFilenameFromClassname(String classname) {
        String name = classname.substring(0, classname.length() - "Plugin".length());
        return toApiFilename(name);
    }

    private String getModelnameFromModelFilename(String filename) {
        String name = filename.substring((modelPackage() + "/").length());
        return camelize(name);
    }

}
