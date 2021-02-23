import glob
import os.path

from conans import ConanFile, CMake, tools


class AbyConan(ConanFile):
    name = "aby"
    license = "LGPL-3.0"
    description = "A framework for efficient mixed-protocol secure two-party computation."
    topics = ("conan", "security")
    homepage = "https://github.com/encryptogroup/ABY"
    url = "https://github.com/conan-io/conan-center-index"
    settings = "os", "compiler", "build_type", "arch"
    options = {
        "shared": [True, False],
        "fPIC": [True, False]
    }
    default_options = {
        "shared": False,
        "fPIC": True
    }
    generators = "cmake"
    exports_sources = ["CMakeLists.txt", "patches/*"]

    @property
    def _source_subfolder(self):
        return "source_subfolder"

    @property
    def _build_subfolder(self):
        return "build_subfolder"

    def config_options(self):
        if self.settings.os == "Windows":
            del self.options.fPIC

    def configure(self):
        self.options["gmp"].enable_fat = True

    def requirements(self):
        self.requires("boost/1.72.0")
        self.requires("gmp/6.2.1")
        self.requires("openssl/1.1.1h")
        # TODO: enable fat gmp

    def source(self):
        commit = self.version
        git = tools.Git(folder=self._source_subfolder)
        git.clone(url="https://github.com/apl-cornell/ABY", branch=commit, shallow=True)

        # Clone only the submodules we need.
        submodules = [
            (".", "extern/ENCRYPTO_utils"),
            ("extern/ENCRYPTO_utils", "extern/relic"),
            (".", "extern/OTExtension"),
        ]
        for path, submodule in submodules:
            self.run(f"cd {self._source_subfolder}/{path} && git submodule update --init --depth 1 {submodule}")

        # Clone all submodules.
        # git.checkout(git.get_revision(), submodule="shallow")

    def _configure_cmake(self):
        if not hasattr(self, "_cmake"):
            cmake = CMake(self)
            cmake.configure(build_folder=self._build_subfolder)
            self._cmake = cmake
        return self._cmake

    def build(self):
        cmake = self._configure_cmake()
        cmake.build()

    def package(self):
        self.copy("LICENSE*", dst="licenses", src=self._source_subfolder)
        cmake = self._configure_cmake()
        cmake.install()

        # Install the header CMake fails to install.
        header_directory = os.path.join(self.build_folder, self._build_subfolder, self._source_subfolder)
        self.copy("**/cmake_constants.h", dst="include", src=header_directory, keep_path=False)

        # Remove CMake files.
        for cmakeDir in glob.iglob(f"{self.package_folder}/**/cmake", recursive=True):
            tools.rmdir(cmakeDir)

    def package_info(self):
        self.cpp_info.libs = tools.collect_libs(self)
