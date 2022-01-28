import pathlib

from conans import ConanFile, tools


class JniConan(ConanFile):
    name = "jni"
    license = "GPL"
    url = "https://github.com/conan-io/conan-center-index"
    description = "Header files for the Java Native Interface."
    topics = ("java", "jdk")
    settings = "os"
    no_copy_source = True

    def source(self):
        self._download_jni("src/java.base/share/native/include/jni.h")
        tools.mkdir("windows")
        with tools.chdir("windows"):
            self._download_jni("src/java.base/windows/native/include/jni_md.h")
        tools.mkdir("unix")
        with tools.chdir("unix"):
            self._download_jni("src/java.base/unix/native/include/jni_md.h")

    def _download_jni(self, filename):
        jdk_url = f"https://raw.githubusercontent.com/openjdk/jdk/{self.version}"
        tools.download(f"{jdk_url}/{filename}", filename=pathlib.PurePath(filename).name)

    def package(self):
        self.copy("jni.h", dst="include")
        md_dir = "windows" if self.settings.os == "Windows" else "unix"
        self.copy("*.h", dst="include", src=md_dir)

    def package_id(self):
        self.info.header_only()
