<?xml version="1.0"?>
<xsl:stylesheet id="MultiplicityFormat" version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:UML="omg.org/UML/1.4">
   <xsl:import href="MultiplicityRangeFormat.xsl"/>
   <xsl:output method="text" />

	<!--
		Handles the formatting of the Multiplity ranges
	-->
	
   <xsl:template match="UML:Multiplicity" >
      <xsl:variable name="ranges" select=".//UML:MultiplicityRange"/>
      <xsl:variable name="left">[</xsl:variable>
      <xsl:variable name="right">]</xsl:variable>
      <xsl:call-template name="UML:Multiplicity">
                <xsl:with-param name="ranges" select="$ranges"/>
                <xsl:with-param name="left" select="$left"/>
                <xsl:with-param name="right" select="$right"/>
       </xsl:call-template>
   </xsl:template>
	
   <xsl:template name="UML:Multiplicity" >
      <xsl:param name="ranges" />
      <xsl:param name="left" />
      <xsl:param name="right" />
      
      <xsl:variable name="numRanges" select="count($ranges)"/>
      <xsl:if test="$numRanges">
      		<xsl:value-of select="$left"/>
	      <xsl:for-each select="$ranges">
	         <xsl:variable name="curNum" select="position()"/>
	
	  	  <xsl:call-template name="UML:MultiplicityRange"/>
	      
		  <xsl:if test="$curNum &lt; $numRanges">
	            <xsl:text>, </xsl:text>
	         </xsl:if>
	      </xsl:for-each>
      		<xsl:value-of select="$right"/>
	</xsl:if>		
   </xsl:template>

</xsl:stylesheet>

  