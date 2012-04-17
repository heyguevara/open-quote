<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:view="http://www.alfresco.org/view/repository/1.0">
    <xsl:output indent="yes"/>
    <xsl:template match="*">
        <!--        Create Element-->
        <xsl:copy>
            <!--            Attributes -->
            <xsl:copy-of select="@*"/>
            <!--            Element data -->
            <xsl:value-of select="text()"/>
            <!--            Post reorented Elements-->
            <xsl:apply-templates select="*">
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>

