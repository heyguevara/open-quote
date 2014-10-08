/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.financial;

import com.ail.core.Functions;
import com.ail.core.TypeEnum;

/** @stereotype enumeration */
public enum Currency implements TypeEnum {
    AED("UAE Dirham"),
	AFA("Afghani"),
	ALL("Lek"),
	ANG("Antillian Guilder"),
	AOK("Kwanza"),
	ARP("Argentine Peso"),
	ATS("Schilling"),
	AUD("Australian Dollar"),
	BBD("Barbados Dollar"),
	BDT("Taka"),
	BEC("Belgian Franc (conv.)"),
	BEF("Belgian Franc"),
	BEL("Belgian Franc (fin.)"),
	BGL("Lev"),
	BHD("Bahraini Dinar"),
	BIF("Burundi Franc"),
	BMD("Bermudan Dollar"),
	BND("Brunei Dollar"),
	BOP("Bolivian Peso"),
	BRC("Cruzeiro"),
	BSD("Bahamian Dollar"),
	BUK("Kyat"),
	BWP("Pula"),
	BZD("Belize Dollar"),
	CAD("Canadian Dollar"),
	CHF("Swiss Franc"),
	CLP("Chilean Peso"),
	CND("Chinese Dollar"),
	CNY("Yuan Renminbi"),
	COP("Colombian Peso"),
	CRC("Costa Rican Colon"),
	CSK("Koruna"),
	CUP("Cuban Peso"),
	CVE("Cape Verde Escudo"),
	CYP("Cyprus Pound"),
	DDM("Mark der DDR"),
	DEM("Deutsche Mark"),
	DJF("Djibouti Franc"),
	DKK("Danish Krone"),
	DOP("Dominican Peso"),
	DZD("Algerian Dinar"),
	ECS("Sucre"),
	EGP("Egyptian Pound"),
	ESA("Span. Peseta (acct. A)"),
	ESB("Span. Peseta (acct. B)"),
	ESP("Spanish Peseta"),
	ETB("Ethiopian Birr"),
	EUR("Euro"),
	FIM("Markka"),
	FJD("Fiji Dollar"),
	FKP("Falkland Islands Pound"),
	FRF("French Franc"),
	GBP("Pound Sterling"),
	GHC("Cedi"),
	GIP("Gibraltar Pound"),
	GMD("Dalasi"),
	GNS("Syli"),
	GQE("Ekwele"),
	GRD("Drachma"),
	GTQ("Quetzal"),
	GWP("Guinea-Bissau Peso"),
	GYD("Guyana Dollar"),
	HKD("Hong Kong Dollar"),
	HNL("Lempira"),
	HTG("Gourde"),
	HUF("Forint"),
	IDR("Rupiah"),
	IEP("Irish Pound"),
	ILS("Shekel"),
	INR("Indian Rupee"),
	IQD("Iraqi Dinar"),
	IRR("Iranian Rial"),
	ISK("Iceland Krona"),
	ITL("Lira"),
	JMD("Jamaican Dollar"),
	JOD("Jordanian dinar"),
	JPY("Yen"),
	KES("Kenyan Shilling"),
	KHR("Riel"),
	KMF("Comoros Franc"),
	KPW("North Korean Won"),
	KRW("Won"),
	KWD("Kuwaiti Dinar"),
	KYD("Cayman Islands Dollar"),
	LAK("Kip"),
	LBP("Lebanese Pound"),
	LKR("Sri Lanka Rupee"),
	LRD("Liberian Dollar"),
	LSM("Maloti"),
	LUF("Luxembourg Franc"),
	LYD("Libyan Dinar"),
	MAD("Moroccan Dirham"),
	MGF("Malagasy Franc"),
	MLF("Mali Franc"),
	MNT("Tugrik"),
	MOP("Pataca"),
	MRO("Ouguiya"),
	MTP("Maltese Pound"),
	MUR("Mauritius Rupee"),
	MVR("Maldive Rupee"),
	MWK("Kwacha"),
	MXP("Mexican Peso"),
	MYR("Malaysian Ringgit"),
	MZM("Metical"),
	NGN("Naira"),
	NIC("Cordoba"),
	NLG("Netherlands Guilder"),
	NOK("Norwegian Krone"),
	NPR("Nepalese Rupee"),
	NZD("New Zealand Dollar"),
	OMR("Rial Omani"),
	PAB("Balboa"),
	PES("Sol"),
	PGK("Kina"),
	PHP("Philippine Peso"),
	PKR("Pakistan Rupee"),
	PLZ("Zloty"),
	PTE("Portugese Escudo"),
	PYG("Guarani"),
	QAR("Qatari Rial"),
	ROL("Leu"),
	RWF("Rwanda Franc"),
	SAR("Saudi Riyal"),
	SBD("Solomon Islands Dollar"),
	SCR("Seychelles Rupee"),
	SDP("Sudanese Pound"),
	SEK("Swedish Krona"),
	SGD("Singapore Dollar"),
	SHP("St. Helena Pound"),
	SLL("Leone"),
	SOS("Somali Shilling"),
	SRG("Suriname Guilder"),
	STD("Dobra"),
	SUR("Rouble"),
	SVC("El Salvador Colon"),
	SYP("Syrian Pound"),
	SZL("Lilangeni"),
	THB("Baht"),
	TND("Tunisian Dinar"),
	TOP("Pa'anga"),
	TPE("Timor Escudo"),
	TRL("Turkish Lira"),
	TTD("Trinidad&Tobago Dollar"),
	TWD("New Taiwan Dollar"),
	TZS("Tanzanian Shilling"),
	UGS("Uganda Shilling"),
	USD("US Dollar"),
	USN("US Dollar (Next day)"),
	USS("US Dollar (same day)"),
	UYP("Uruguayan Peso"),
	VND("Dong"),
	VEB("Bolivar"),
	VUV("Vatu"),
	WST("Tala"),
	XAF("CFA Franc BEAC"),
	XAU("Gold"),
	XBA("European Composite Unit"),
	XBC("Euro. Unit of Acct. 9"),
	XBD("Euro. Unit of Acct. 17"),
	XCD("East Caribbean Dollar"),
	XDR("Special Drawing Rights"),
	XEU("European Currency Unit"),
	XOF("CFA Franc BCEAO"),
	XPF("CFP Franc"),
	YDD("Yemeni Dinar"),
	YER("Yemeni Rial"),
	YUD("New Yugoslavian Dinar"),
	ZAL("Rand (financial)"),
	ZAR("Rand"),
	RMB("Renminbi"),
	ZMK("Kwacha"),
	ZRZ("Zaire"),
	ZWD("Zimbabwe Dollar");
    
    private final String longName;

    Currency() {
        this.longName=name();
    }
    
    Currency(String longName) {
        this.longName=longName;
    }
    
    public String getLongName() {
        return longName;
    }
    
    public String getName() {
        return name();
    }
    
    public String valuesAsCsv() {
        return Functions.valuesAsCsv(values());
    }

    public String longName() {
        return longName;
    }
    
    /**
     * This method is similar to the valueOf() method offered by Java's Enum type, but
     * in this case it will match either the Enum's name or the longName.
     * @param name The name to lookup
     * @return The matching Enum, or IllegalArgumentException if there isn't a match.
     */
    public static Currency forName(String name) {
        return (Currency)Functions.enumForName(name, values());
    }

    public int getOrdinal() {
        return ordinal();
    }
}
