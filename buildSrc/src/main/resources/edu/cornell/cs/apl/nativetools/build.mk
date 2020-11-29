include lib.mk
include get.mk

$(DOWNLOAD_DIR)/patched-source: $(DOWNLOAD_DIR)/original-source lib.patch
	rsync -au --delete --exclude=.git $</ $@
	git apply lib.patch --directory $@
	touch $@

$(GENERATED_CPP_FILE) $(GENERATED_JAVA_DIR): $(DOWNLOAD_DIR)/patched-source lib.i
	rm -rf $(GENERATED_JAVA_DIR)
	mkdir -p $(GENERATED_JAVA_DIR)
	mkdir -p $(dir $(GENERATED_CPP_FILE))
	swig \
		-Wall -Werror -macroerrors \
		-c++ \
		-java -package $(LIB_PACKAGE) \
		$(addprefix -I$</,$(LIB_INCLUDE_DIRS)) \
		-o $(GENERATED_CPP_FILE) -outdir $(GENERATED_JAVA_DIR) \
		lib.i

.PHONY: swig
swig: $(GENERATED_CPP_FILE) $(GENERATED_JAVA_DIR)
