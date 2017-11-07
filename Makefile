TRUNK=..
TARGET=$(TRUNK)/maven-build-artifacts/xsl-template-expander
SANDBOX=$(TRUNK)/xsl-template-expander-sandbox

all: $(TARGET)/xsl-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar

install: $(SANDBOX)/bin $(SANDBOX)/bin/template-expander $(SANDBOX)/bin/xsl-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar

$(TARGET)/xsl-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar: 
	mvn package

$(SANDBOX)/bin:
	mkdir -p $@

$(SANDBOX)/bin/template-expander: template-expander
	cp $< $@
	chmod a+rx $@

$(SANDBOX)/bin/xsl-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar: $(TARGET)/xsl-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar
	cp $< $@
	chmod a+r $@

