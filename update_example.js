const shell = require("shelljs");
const path = require("path");

const command = `docker run -m 4GB --cpus 4 -e USER="$(id -u)" \\
 -u="$(id -u)" \\
 -v ${__dirname}:/src/workspace \\
 -v ${path.join(__dirname, 'example', 'docker_cache')}:/tmp \\
 -w /src/workspace \\
 l.gcr.io/google/bazel:latest \\
 --output_user_root=/tmp/build_output \\
 --output_user_root=/tmp/execroot \\
 --output_user_root=/tmp/cache \\
 --output_user_root=/tmp/install \\
 --output_user_root=/tmp/external \\
 --output_user_root=/tmp/server \\
 --output_base=/tmp build //example/api/...`;

console.log(command);
shell.exec(command, {
    cwd: __dirname
});

const artifactsPath = path.join("example", "proto_swagger_gen_out");
shell.rm("-rf", artifactsPath);
shell.mkdir(artifactsPath);
shell.cp(
  "-rLf",
  path.join(
    "example",
    "docker_cache",
    "execroot",
    "proto_swagger_gen",
    "bazel-out",
    "k8-fastbuild",
    "bin",
    "example",
    "api",
    "*"
  ),
  artifactsPath
);
shell.chmod('-R', 700, artifactsPath);