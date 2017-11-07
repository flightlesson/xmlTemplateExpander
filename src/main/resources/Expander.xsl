<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  version="2.0"
		xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias"
		>

  <xsl:output method="xml" indent="yes"/>
  
  <xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>

  <xsl:param name="debug-level">0</xsl:param>
  <xsl:param name="expand-using"></xsl:param>
  <xsl:param name="path-to-output-dir">./</xsl:param>

  <xsl:variable name="using" select="document($expand-using)"/>

  <xsl:template match="/">
    <xsl:apply-templates select="*"/>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:attribute name="{name()}">
        <xsl:call-template name="expand-values">
          <xsl:with-param name="string" select="."/>
	  <xsl:with-param name="expand-as">text</xsl:with-param>
        </xsl:call-template>
    </xsl:attribute>
  </xsl:template>

  <xsl:template match="text()">
      <xsl:call-template name="expand-values">
        <xsl:with-param name="string" select="."/>
	<xsl:with-param name="expand-as">node-set</xsl:with-param>
      </xsl:call-template>
  </xsl:template>

  <xsl:template name="expand-values">
    <xsl:param name="string"/>
    <xsl:param name="expand-as"/>
    <xsl:message>expand-values expand-as=|<xsl:value-of select="$expand-as"/>| string=|<xsl:value-of select="$string"/>|</xsl:message>
    <xsl:analyze-string select="$string" regex="([^%]*)%(\c+)%">
      <xsl:matching-substring>
	<!-- <xsl:copy-of select="$using//template-param[@name=regex-group(2)]"/> -->
	<xsl:value-of select="regex-group(1)"/>
        <xsl:variable name="replacement">
	  <xsl:choose>
	    <xsl:when test="$using//template-param[@name=regex-group(2)]">
	      <xsl:choose>
		<xsl:when test="$expand-as='text'">
                  <xsl:message>matching-substring[1]: [[<xsl:value-of select="regex-group(1)"/>][<xsl:value-of select="regex-group(2)"/>]]</xsl:message>
		  <xsl:value-of select="$using//template-param[@name=regex-group(2)]"/>
		</xsl:when>
		<xsl:otherwise>
                  <xsl:message>matching-substring[2]: [[<xsl:value-of select="regex-group(1)"/>][<xsl:value-of select="regex-group(2)"/>]]</xsl:message>
		  <xsl:copy-of select="$using//template-param[@name=regex-group(2)]"/>
		</xsl:otherwise>
	      </xsl:choose>
            </xsl:when>
	    <xsl:otherwise>
              <xsl:message>matching-substring[3]: [[<xsl:value-of select="regex-group(1)"/>][<xsl:value-of select="regex-group(2)"/>]]</xsl:message>
	      <xsl:value-of select="regex-group(2)"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
	<xsl:copy-of select="$replacement"/>
      </xsl:matching-substring>
      <xsl:non-matching-substring>
	<xsl:message>non-matching-substring: [<xsl:value-of select="."/>]</xsl:message>
	<xsl:copy-of select="."/>
      </xsl:non-matching-substring>
    </xsl:analyze-string>
  </xsl:template>

</xsl:stylesheet>
