ABY_GROUP   := $(shell grep abyGroup   < gradle.properties | cut -d'=' -f2)

LIBRARY_NAME := abyjava
SOURCE_DIR   := src/main/java/$(subst .,/,$(ABY_GROUP))/aby
RESOURCE_DIR := src/main/resources/natives
WRAPPER_FILE := src/main/cpp/aby_wrap.cpp

all: swig linux_64
.PHONY: all

swig: $(SOURCE_DIR) $(WRAPPER_FILE)
.PHONY: swig

linux_64: $(RESOURCE_DIR)/linux_64/lib$(LIBRARY_NAME).so
.PHONY: linux_64

$(SOURCE_DIR) $(WRAPPER_FILE):
	rm -rf $@
	./scripts/docker_copy.sh "--target swig" /root/$@ $@
.PHONY: $(SOURCE_DIR) $(WRAPPER_FILE)

$(RESOURCE_DIR)/linux_64/lib$(LIBRARY_NAME).so:
	rm -f $@
	./scripts/docker_copy.sh "--target builder" /root/$@ $@
.PHONY: $(RESOURCE_DIR)/linux_64/lib$(LIBRARY_NAME).so

test:
	docker build --target tester .
.PHONY: test
