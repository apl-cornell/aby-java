ROOT_DIR    := ${CURDIR}
SOURCE_DIR  := $(ROOT_DIR)/source
BUILD_DIR   := $(ROOT_DIR)/build
INSTALL_DIR := $(ROOT_DIR)/install

BOOST_VERSION   := 1.74.0
GMP_VERSION     := 6.2.0
OPENSSL_VERSION := 1.1.1g
OPENJDK_VERSION := jdk-11+28

BOOST_FOLDER    := boost_$(subst .,_,$(BOOST_VERSION))
BOOST_ARCHIVE   := $(BOOST_FOLDER).tar.bz2
GMP_FOLDER      := gmp-$(GMP_VERSION)
GMP_ARCHIVE     := $(GMP_FOLDER).tar.bz2
OPENSSL_FOLDER  := openssl-$(OPENSSL_VERSION)
OPENSSL_ARCHIVE := $(OPENSSL_FOLDER).tar.gz
OPENJDK_FOLDER  := openjdk-$(OPENJDK_VERSION)

export CFLAGS   = $(STATICLIB_CFLAGS)
export CXXFLAGS = $(STATICLIB_CFLAGS)

all: build-all
.PHONY:all

$(SOURCE_DIR)/$(BOOST_ARCHIVE):
	mkdir -p $(dir $@)
	wget --quiet https://dl.bintray.com/boostorg/release/$(BOOST_VERSION)/source/$(BOOST_ARCHIVE) -O $@

$(SOURCE_DIR)/$(GMP_ARCHIVE):
	mkdir -p $(dir $@)
	wget --quiet https://gmplib.org/download/gmp/$(GMP_ARCHIVE) -O $@

$(SOURCE_DIR)/$(OPENSSL_ARCHIVE):
	mkdir -p $(dir $@)
	wget --quiet https://www.openssl.org/source/$(OPENSSL_ARCHIVE) -O $@

$(SOURCE_DIR)/$(BOOST_FOLDER): $(SOURCE_DIR)/$(BOOST_ARCHIVE)
	tar --extract --file $< --directory $(dir $@)
	touch $@

$(SOURCE_DIR)/$(GMP_FOLDER): $(SOURCE_DIR)/$(GMP_ARCHIVE)
	tar --extract --file $< --directory $(dir $@)
	touch $@

$(SOURCE_DIR)/$(OPENSSL_FOLDER): $(SOURCE_DIR)/$(OPENSSL_ARCHIVE)
	tar --extract --file $< --directory $(dir $@)
	touch $@

OPENJDK_URL := https://raw.githubusercontent.com/openjdk/jdk/$(OPENJDK_VERSION)
$(SOURCE_DIR)/$(OPENJDK_FOLDER): \
	$(SOURCE_DIR)/$(OPENJDK_FOLDER)/jni.h \
	$(SOURCE_DIR)/$(OPENJDK_FOLDER)/unix/jni_md.h \
	$(SOURCE_DIR)/$(OPENJDK_FOLDER)/windows/jni_md.h
	touch $@
$(SOURCE_DIR)/$(OPENJDK_FOLDER)/jni.h:
	mkdir -p $(dir $@)
	wget --quiet $(OPENJDK_URL)/src/java.base/share/native/include/jni.h -O $@
$(SOURCE_DIR)/$(OPENJDK_FOLDER)/unix/jni_md.h:
	mkdir -p $(dir $@)
	wget --quiet $(OPENJDK_URL)/src/java.base/unix/native/include/jni_md.h -O $@
$(SOURCE_DIR)/$(OPENJDK_FOLDER)/windows/jni_md.h:
	mkdir -p $(dir $@)
	wget --quiet $(OPENJDK_URL)/src/java.base/windows/native/include/jni_md.h -O $@

get-boost: $(SOURCE_DIR)/$(BOOST_FOLDER)
get-gmp: $(SOURCE_DIR)/$(GMP_FOLDER)
get-openssl: $(SOURCE_DIR)/$(OPENSSL_FOLDER)
get-openjdk: $(SOURCE_DIR)/$(OPENJDK_FOLDER)
get-all: get-boost get-gmp get-openssl
.PHONY: get-all get-boost get-gmp get-openssl get-openjdk

build-boost: $(SOURCE_DIR)/$(BOOST_FOLDER)
	mkdir -p $(BUILD_DIR)/$(BOOST_FOLDER)
	cd $< \
		&& ./bootstrap.sh --with-libraries=filesystem,system,thread --prefix=$(INSTALL_DIR) \
		&& ./b2 --build-dir=$(BUILD_DIR)/$(BOOST_FOLDER) link=static cxxflags="$(CXXFLAGS)" install

build-gmp: $(SOURCE_DIR)/$(GMP_FOLDER)
	mkdir -p $(BUILD_DIR)/$(GMP_FOLDER)
	cd $(BUILD_DIR)/$(GMP_FOLDER) \
		&& $</configure --prefix=$(INSTALL_DIR) --enable-cxx --with-pic --enable-shared=no \
		&& $(MAKE) install \
		# && $(MAKE) check

build-openssl: $(SOURCE_DIR)/$(OPENSSL_FOLDER)
	mkdir -p $(BUILD_DIR)/$(OPENSSL_FOLDER)
	cd $(BUILD_DIR)/$(OPENSSL_FOLDER) \
		&& $</config --prefix=$(INSTALL_DIR) --openssldir=$(INSTALL_DIR)/ssl no-shared \
		&& $(MAKE) install_dev

build-openjdk: $(SOURCE_DIR)/$(OPENJDK_FOLDER)
	mkdir -p $(INSTALL_DIR)/include
	cp -r $</* $(INSTALL_DIR)/include/

build-all: build-boost build-gmp build-openssl build-openjdk
.PHONY: build-all build-boost build-gmp build-openssl build-openjdk

clean: clean-build clean-install
clean-all: clean-source clean-build clean-install

clean-source:
	rm -rf $(SOURCE_DIR)

clean-build:
	rm -rf $(BUILD_DIR)

clean-install:
	rm -rf $(INSTALL_DIR)

.PHONY: clean clean-all clean-source clean-build clean-install
