docker run -m 8GB --cpus 8 -e USER="$(id -u)" \
 -u="$(id -u)" \
 -v ~/proto_swagger_gen:/src/workspace \
 -v ~/proto_swagger_gen/dist:/tmp/build_output \
 -w /src/workspace \
 l.gcr.io/google/bazel:latest \
 --output_user_root=/tmp/build_output \
 build //api/... --symlink_prefix=dist/
