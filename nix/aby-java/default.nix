{ pkgs, aby, jni }:

pkgs.stdenv.mkDerivation rec {
  pname = "aby-java";
  version = aby.version;

  src = ./src;

  postUnpack = ''
    mkdir -p src/java
    swig -Wall -Werror -macroerrors \
      -c++ -java -package ABY_PACKAGE_NAME \
      -I${aby.out}/include \
      -o src/wrapper.cpp -outdir src/java \
      $src/ABY.i
  '';

  nativeBuildInputs = [
    pkgs.cmake
    pkgs.swig
  ];

  buildInputs = [
    aby
    jni
  ];

  postInstall = ''
    cp -r ../java $out
  '';
}
