{ pkgs }:

pkgs.stdenv.mkDerivation rec {
  pname = "jni";
  version = "jdk-11+28";

  # JNI Headers
  share = "src/java.base/share/native/include/jni.h";
  windows = "src/java.base/windows/native/include/jni_md.h";
  unix = "src/java.base/unix/native/include/jni_md.h";
  md = if pkgs.stdenv.hostPlatform.isWindows then windows else unix;

  src = pkgs.fetchFromGitHub {
    owner = "openjdk";
    repo = "jdk";
    rev = version;
    sparseCheckout = ''
      ${share}
      ${windows}
      ${unix}
    '';
    sha256 = "RZUqCsej2prd4EQOVehYtJdek1K4tsKDv5jJGmUq1J8=";
  };

  installPhase = ''
    mkdir -p $out/include
    cp ${share} $out/include
    cp ${md} $out/include
  '';
}
