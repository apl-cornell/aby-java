{ pkgs ? import <nixpkgs> {}
}:

let
  aby = import ./aby { inherit pkgs; };

  jni = import ./jni { inherit pkgs; };

  aby-java = import ./aby-java { inherit pkgs aby jni; };
in aby-java
