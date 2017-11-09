TRUNK=..
TARGET=$(TRUNK)/maven-build-artifacts/xml-template-expander
SANDBOX=$(TRUNK)/xml-template-expander-sandbox

all: $(TARGET)/xml-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar

install: $(SANDBOX)/bin $(SANDBOX)/bin/xml-template-expander $(SANDBOX)/bin/xml-template-expander.jar

$(TARGET)/xml-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar: 
	mvn package

$(SANDBOX)/bin:
	mkdir -p $@

$(SANDBOX)/bin/xml-template-expander: xml-template-expander
	cp $< $@
	chmod a+rx $@

$(SANDBOX)/bin/xml-template-expander.jar: $(TARGET)/xml-template-expander-1.0-SNAPSHOT-jar-with-dependencies.jar
	cp $< $@
	chmod a+r $@

