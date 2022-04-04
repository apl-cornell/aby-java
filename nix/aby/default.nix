{ pkgs }:

pkgs.stdenv.mkDerivation rec {
  pname = "ABY";
  version = "099e72559e9cf509cc046f64b42713bd53f45b43";

  # TODO: this downloads submodule ENCRYPTO_utils twice.
  src = pkgs.fetchFromGitHub {
    owner = "apl-cornell";
    repo = pname;
    rev = version;
    fetchSubmodules = true;
    sha256 = "nxxHuCbiB9A43UpE8VJ50IK4qqEW3rzH88Q7UfL6zjg=";
  };

  nativeBuildInputs = [
    pkgs.cmake
  ];

  propagatedBuildInputs = [
    pkgs.boost
    pkgs.gmp
    pkgs.openssl
  ];

  # CMake fails to install this header.
  postInstall = ''
    find . -name cmake_constants.h -exec cp '{}' $out/include ';'
  '';
}
