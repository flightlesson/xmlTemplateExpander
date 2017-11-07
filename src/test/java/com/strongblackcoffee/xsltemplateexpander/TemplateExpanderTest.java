/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.strongblackcoffee.xsltemplateexpander;

import javax.xml.transform.stream.StreamSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tsingle
 */
public class TemplateExpanderTest {
    
    public TemplateExpanderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of expand method, of class TemplateExpander.
     */
    @Test
    public void testExpand() throws Exception {
        System.out.println("testExpand");
        String inputURI = "/test01/values.xml";
        String pathToOutputDir = "./";
        int debugLevel = 0;
        TemplateExpander instance = new TemplateExpander();
        instance.expand(inputURI, new StreamSource(this.getClass().getResourceAsStream("/test01/template.xml"),"/test01/template.xml"), pathToOutputDir, debugLevel);
    }
    
}
