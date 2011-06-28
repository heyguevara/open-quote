<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <!--    Standard documents-->
    <!--    UK  -->
    <xsl:attribute-set name="UkPageSize" use-attribute-sets="A4 Helvetica Non-print-margins"/>
    <xsl:variable name="ail-regions-first">
        <fo:region-body region-name="body"/>
        <fo:region-before region-name="first-before" extent="0mm"/>
        <fo:region-after region-name="first-after" extent="0mm"/>
        <fo:region-start region-name="first-start" extent="80mm"/>
        <fo:region-end region-name="first-end" extent="10mm"/>
    </xsl:variable>
    <xsl:variable name="ail-regions-rest">
        <fo:region-body region-name="body"/>
        <fo:region-before region-name="rest-before" extent="0mm"/>
        <fo:region-after region-name="rest-after" extent="0mm"/>
        <fo:region-start region-name="rest-start" extent="40mm"/>
        <fo:region-end region-name="rest-end" extent="10mm"/>
    </xsl:variable>
    <xsl:variable name="UkLayout">
        <!-- page sequences -->
        <fo:page-sequence-master master-name="all-pages">
            <fo:single-page-master-reference master-reference="first-page"/>
            <fo:repeatable-page-master-reference master-reference="other-pages"/>
        </fo:page-sequence-master>

        <fo:simple-page-master xsl:use-attribute-sets="UkPageSize" master-name="first-page">
            <xsl:copy-of select="$ail-regions-first"/>
        </fo:simple-page-master>
        <fo:simple-page-master xsl:use-attribute-sets="UkPageSize" master-name="other-pages">
            <xsl:copy-of select="$ail-regions-rest"/>
        </fo:simple-page-master>
    </xsl:variable>
    <!-- Page sizes -->
    <xsl:attribute-set name="A4">
        <xsl:attribute name="page-height">297mm</xsl:attribute>
        <xsl:attribute name="page-width">210mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Portrait">
        <xsl:attribute name="size">portrait</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Landscape">
        <xsl:attribute name="size">portrait</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Legal">
        <xsl:attribute name="page-height">279mm</xsl:attribute>
        <xsl:attribute name="page-width">216mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="LegalA4">
        <xsl:attribute name="page-height">279mm</xsl:attribute>
        <xsl:attribute name="page-width">210mm</xsl:attribute>
    </xsl:attribute-set>
    <!-- Non printable margins-->
    <xsl:attribute-set name="Non-print-margins">
        <xsl:attribute name="margin-bottom">10mm</xsl:attribute>
        <xsl:attribute name="margin-left">10mm</xsl:attribute>
        <xsl:attribute name="margin-right">10mm</xsl:attribute>
        <xsl:attribute name="margin-top">10mm</xsl:attribute>
    </xsl:attribute-set>
    <!--    Fonts-->
    <xsl:attribute-set name="Helvetica">
        <xsl:attribute name="font-family">Helvetica</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Times">
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Courier">
        <xsl:attribute name="font-family">Courier</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="BaseFont" use-attribute-sets="Helvetica"/>
    <xsl:attribute-set name="B">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="U">
        <xsl:attribute name="text-decoration">underline</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="I">
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>
    <!--    XHTML Styles-->
    <xsl:attribute-set name="H1" use-attribute-sets="BaseFont">
        <xsl:attribute name="font-size">16pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="H2" use-attribute-sets="BaseFont">
        <xsl:attribute name="font-size">14pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="H3" use-attribute-sets="BaseFont B">
        <xsl:attribute name="font-size">12pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="H4" use-attribute-sets="BaseFont">
        <xsl:attribute name="font-size">12pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="normal" use-attribute-sets="BaseFont">
        <xsl:attribute name="font-size">10pt</xsl:attribute>
    </xsl:attribute-set>
    <!--    Standard FO utilities-->
    <!-- Page Count -->
    <xsl:variable name="fo-page-count">
        <fo:block>
            <fo:block id="TVLP"/>
        </fo:block>
    </xsl:variable>
    <xsl:variable name="fo-pagebreak">
        <fo:block break-after="page" keep-with-next="auto">
            <fo:leader leader-pattern="space"/>
        </fo:block>
    </xsl:variable>
    <xsl:variable name="fo-blankline">
        <fo:block>
            <fo:leader leader-pattern="space"/>
        </fo:block>
    </xsl:variable>
    <xsl:template match="u">
        <fo:inline text-decoration="underline">
            <xsl:apply-templates select="node()"/>
        </fo:inline>
    </xsl:template>

    <xsl:template match="i">
        <fo:inline font-style="italic">
            <xsl:apply-templates select="node()"/>
        </fo:inline>
    </xsl:template>

    <xsl:template match="b">
        <fo:inline font-weight="bold">
            <xsl:apply-templates select="node()"/>
        </fo:inline>
    </xsl:template>

    <xsl:template match="c">
        <fo:block text-align="center">
            <xsl:apply-templates select="node()"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="br">
        <xsl:text>&#x00A0;&#x2028;</xsl:text>
    </xsl:template>


    <xsl:attribute-set name="before-border">
        <xsl:attribute name="border-before-style">solid</xsl:attribute>
        <xsl:attribute name="border-before-width">0.1mm</xsl:attribute>
        <xsl:attribute name="border-before-color">
            <xsl:value-of select="$light-colour"/>
        </xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="start-border">
        <xsl:attribute name="border-start-style">solid</xsl:attribute>
        <xsl:attribute name="border-start-width">0.1mm</xsl:attribute>
        <xsl:attribute name="border-start-color">
            <xsl:value-of select="$light-colour"/>
        </xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="full-border-no-colour">
        <xsl:attribute name="border-style">solid</xsl:attribute>
        <xsl:attribute name="border-width">0.1mm</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="header-block">
        <xsl:attribute name="color">white</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>


    <xsl:variable name="empty-cell">
        <fo:table-cell>
            <fo:block>&#x00A0;</fo:block>
        </fo:table-cell>
    </xsl:variable>
    <xsl:variable name="light-colour">silver</xsl:variable>
    <xsl:variable name="dark-colour">grey</xsl:variable>
</xsl:stylesheet>
