<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:template match='/'>
        <listProductsArgImp xsi:type="java:com.ail.core.product.listproducts.ListProductsArgImp" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <productsRet>hello</productsRet>
            <productsRet>hello2</productsRet>
        </listProductsArgImp>
    </xsl:template>
</xsl:stylesheet>