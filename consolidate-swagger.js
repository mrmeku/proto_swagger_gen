const { writeFileSync, readFileSync } = require("fs");

[outputSwaggerPath, baseJson, ...inputSwaggerPaths] = process.argv.slice(2);

if (!baseJson) {
  throw new Error("At least one swagger file is required");
}

const consolidatedJson = JSON.parse(readFileSync(baseJson).toString());

inputSwaggerPaths.forEach(path => {
  const json = JSON.parse(readFileSync(path).toString());
  consolidatedJson.paths = { ...consolidatedJson.paths, ...json.paths };
  consolidatedJson.definitions = {
    ...consolidatedJson.definitions,
    ...json.definitions
  };
});

writeFileSync(outputSwaggerPath, JSON.stringify(consolidatedJson, null, 2));
