package com.strongblackcoffee.xsltemplateexpander;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.stream.Stream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 */
public class TemplateExpander {

    static final String USAGE = "TemplateExpander [{options}] {args}";
    static final String HEADER = "For each {arg} file generates a corresponding .xsl file by expanding a template using data from the {arg} file.";
    static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption("h","help",false,"show help");
        OPTIONS.addOption("D","output-dir",true,"specify the directory that generated files get written to; defaults to './'.");
        OPTIONS.addOption("v","verbose",false,"verbose output");
        OPTIONS.addOption("d","debug",false,"more verbose output");
        OPTIONS.addOption("t","template",true,"path to the template file; defaults to 'template.xsl'");
    };
    static final String FOOTER = 
            "Processes each {arg} file through the {template} to create {output-dir}/{arg}.xsl."
            + "\n\n"
            + "{arg} files contain XML like:\n"
            + "    <unexpanded-step step=\"{this-step}\" next-step=\"{mext-step}\">"
            + "       {xsl element list}\n"
            + "    </unexpanded-xsl>\n"
            ;
    
    /**
     * Starts the application.
     */
    public static void main( String[] args ) {
        try {
            CommandLine cmdline = (new DefaultParser()).parse(OPTIONS,args);
            if (cmdline.hasOption("help")) {
                (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
                System.exit(1);
            }
            int verbosity = cmdline.hasOption("debug") ? 2 : cmdline.hasOption("verbose") ? 1 : 0;
            TemplateExpander expander = new TemplateExpander();
            expander.expand(cmdline.getArgs()[0],cmdline.getOptionValue("template","template.xml"),cmdline.getOptionValue("output-dir"),verbosity);
        } catch (ParseException | ArrayIndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
            (new HelpFormatter()).printHelp(USAGE,HEADER,OPTIONS,FOOTER,false);
        } catch (TemplateExpanderException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    protected static final URIResolver uriResolver= new URIResolver() {
            @Override 
            public Source resolve(String href, String base) throws TransformerException {
                System.out.println("resolve(\""+href+"\",\""+base+"\")");
                //if (href.endsWith(".xsl")) {
                InputStream inputStream = this.getClass().getResourceAsStream(href);
                if (inputStream != null) {
                    System.out.println("resolve found " + href + " as resource");
                } else {
                    File file = new File(href);
                    if (file.canRead()) {
                        System.out.println("resolve found " + href + " as file");
                        try {
                            inputStream = new FileInputStream(file); 
                        } catch (FileNotFoundException shouldNeverOccur) {
                            inputStream = null;
                        }
                    }
                }
                StreamSource stream= new StreamSource(inputStream);
                return stream;
            }
        };
    
    public TemplateExpander() {
        transformerFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
        transformerFactory.setURIResolver(uriResolver);
    }
    
    protected final TransformerFactory transformerFactory;
    
    public void expand(String inputURI, String templateURI, String pathToOutputDir, int debugLevel) throws TemplateExpanderException {
        expand(inputURI, new StreamSource(new File(templateURI)), pathToOutputDir, debugLevel);
    }
        
        
    public void expand(String inputURI, Source template, String pathToOutputDir, int debugLevel) throws TemplateExpanderException {
        System.out.println("expand(\""+inputURI+"\",\""+template.getSystemId()+"\",\""+pathToOutputDir+"\","+debugLevel+")");
        try {
            StreamSource xsltScript = new StreamSource( getClass().getResourceAsStream("/Expander.xsl") );
            xsltScript.setPublicId("Expander.xsl");
            Transformer expander = transformerFactory.newTransformer(xsltScript);
            //expander.setURIResolver(uriResolver);
            expander.setParameter("debug-level",debugLevel);
            expander.setParameter("expand-using", inputURI);
            expander.setParameter("path-to-output-dir", pathToOutputDir);
            
            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            Result result = new StreamResult(resultStream);
            expander.transform(template, result);
            
            System.out.println(resultStream.toString());
            
        } catch (TransformerException ex) {
            throw new TemplateExpanderException("Simplifier constructor: ", ex);
        }
    }
}
