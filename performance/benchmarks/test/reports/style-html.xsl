<?xml version="1.0" encoding="windows-1250"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- expected structure looks like this
<testresults>
  <testcase name="IDE run" threshold="0" unit="ms" order="1" average="7312.57" stddev="2195.40" variance="4819759.84">
    <result value="7825"/>
    <difference value="18.30" origaverage="6181.35" origstddev="950.8491648050843"/>
    -->

<xsl:output method="html" encoding="UTF-8"/>

<xsl:template match="/">
  <html>
    <head>
      <title>Performance results</title>
    </head>
    <body>
      <xsl:apply-templates/>
    </body>
  </html>
</xsl:template>

<xsl:template match="testresults">
  <h2>Test results</h2>

  <table border="1"><thead>
      <td>Name</td><td>Run Order</td><td>Average</td><td>Stddev</td><td>Variance</td><td>Results</td>
      <td>Difference</td><td>Orig Average</td><td>Orig Stddev</td>
  </thead>
    <tbody>
      <xsl:apply-templates/>
    </tbody>
  </table>

  <p>Details:</p>
  <hr/>
</xsl:template>

<xsl:template match="testcase">
  <tr>
    <td><xsl:value-of select="@name"/></td>
    <td><xsl:value-of select="@order"/></td>
    <td><xsl:value-of select="@average"/></td>
    <td><xsl:value-of select="@stddev"/></td>
    <td><xsl:value-of select="@variance"/></td>
    <td><xsl:apply-templates/></td>
    <!--
  <xsl:if test="@value > @threshold">
    <td>Failed - out of limits</td>
  </xsl:if>
  -->
  </tr>
</xsl:template>

<xsl:template match="difference">
    <xsl:if test="@value > 0">
    <td bgcolor="red">
        <xsl:value-of select="@value"/>
    </td>
    </xsl:if>
    <xsl:if test="0 > @value">
    <td bgcolor="green">
        <xsl:value-of select="@value"/>
    </td>
    </xsl:if>
    <xsl:if test="@value = 0">
    <td>
        <xsl:value-of select="@value"/>
    </td>
    </xsl:if>
    <td><xsl:value-of select="@origaverage"/></td>
    <td><xsl:value-of select="@origstddev"/></td>
    <td><xsl:apply-templates/></td>
</xsl:template>

<xsl:template match="result">
  <xsl:value-of select="@value"/>,
</xsl:template>


</xsl:stylesheet>

