/*
 * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

package com.tomtom.nk2.core.common.geography

import java.util.Locale

/**
 * This enum lists all countries as defined in ISO 3166.
 */
enum class CountryId(
    /**
     * ISO 3166-1 alpha-3 code
     */
    val isoCode: String,
    /**
     * ISO 3166-1 alpha-2 code
     */
    val iso2Code: String
) {
    ABW("ABW", "AW"), // Aruba
    AFG("AFG", "AF"), // Afghanistan
    AGO("AGO", "AO"), // Angola
    AIA("AIA", "AI"), // Anguilla
    ALA("ALA", "AX"), // Åland Islands
    ALB("ALB", "AL"), // Albania
    AND("AND", "AD"), // Andorra
    ARE("ARE", "AE"), // United Arab Emirates
    ARG("ARG", "AR"), // Argentina
    ARM("ARM", "AM"), // Armenia
    ASM("ASM", "AS"), // American Samoa
    ATA("ATA", "AQ"), // Antarctica
    ATF("ATF", "TF"), // French Southern Territories
    ATG("ATG", "AG"), // Antigua and Barbuda
    AUS("AUS", "AU"), // Australia
    AUT("AUT", "AT"), // Austria
    AZE("AZE", "AZ"), // Azerbaijan
    BDI("BDI", "BI"), // Burundi
    BEL("BEL", "BE"), // Belgium
    BEN("BEN", "BJ"), // Benin
    BES("BES", "BQ"), // Bonaire, Sint Eustatius and Saba
    BFA("BFA", "BF"), // Burkina Faso
    BGD("BGD", "BD"), // Bangladesh
    BGR("BGR", "BG"), // Bulgaria
    BHR("BHR", "BH"), // Bahrain
    BHS("BHS", "BS"), // Bahamas
    BIH("BIH", "BA"), // Bosnia and Herzegovina
    BLM("BLM", "BL"), // Saint Barthélemy
    BLR("BLR", "BY"), // Belarus
    BLZ("BLZ", "BZ"), // Belize
    BMU("BMU", "BM"), // Bermuda
    BOL("BOL", "BO"), // Bolivia, Plurinational State of
    BRA("BRA", "BR"), // Brazil
    BRB("BRB", "BB"), // Barbados
    BRN("BRN", "BN"), // Brunei Darussalam
    BTN("BTN", "BT"), // Bhutan
    BVT("BVT", "BV"), // Bouvet Island
    BWA("BWA", "BW"), // Botswana
    CAF("CAF", "CF"), // Central African Republic
    CAN("CAN", "CA"), // Canada
    CCK("CCK", "CC"), // Cocos (Keeling) Islands
    CHE("CHE", "CH"), // Switzerland
    CHL("CHL", "CL"), // Chile
    CHN("CHN", "CN"), // China
    CIV("CIV", "CI"), // Côte d'Ivoire
    CMR("CMR", "CM"), // Cameroon
    COD("COD", "CD"), // Congo, the Democratic Republic of the
    COG("COG", "CG"), // Congo
    COK("COK", "CK"), // Cook Islands
    COL("COL", "CO"), // Colombia
    COM("COM", "KM"), // Comoros
    CPV("CPV", "CV"), // Cape Verde
    CRI("CRI", "CR"), // Costa Rica
    CUB("CUB", "CU"), // Cuba
    CUW("CUW", "CW"), // Curaçao
    CXR("CXR", "CX"), // Christmas Island
    CYM("CYM", "KY"), // Cayman Islands
    CYP("CYP", "CY"), // Cyprus
    CZE("CZE", "CZ"), // Czech Republic
    DEU("DEU", "DE"), // Germany
    DJI("DJI", "DJ"), // Djibouti
    DMA("DMA", "DM"), // Dominica
    DNK("DNK", "DK"), // Denmark
    DOM("DOM", "DO"), // Dominican Republic
    DZA("DZA", "DZ"), // Algeria
    ECU("ECU", "EC"), // Ecuador
    EGY("EGY", "EG"), // Egypt
    ERI("ERI", "ER"), // Eritrea
    ESH("ESH", "EH"), // Western Sahara
    ESP("ESP", "ES"), // Spain
    EST("EST", "EE"), // Estonia
    ETH("ETH", "ET"), // Ethiopia
    FIN("FIN", "FI"), // Finland
    FJI("FJI", "FJ"), // Fiji
    FLK("FLK", "FK"), // Falkland Islands (Malvinas)
    FRA("FRA", "FR"), // France
    FRO("FRO", "FO"), // Faroe Islands
    FSM("FSM", "FM"), // Micronesia, Federated States of
    GAB("GAB", "GA"), // Gabon
    GBR("GBR", "GB"), // United Kingdom
    GEO("GEO", "GE"), // Georgia
    GGY("GGY", "GG"), // Guernsey
    GHA("GHA", "GH"), // Ghana
    GIB("GIB", "GI"), // Gibraltar
    GIN("GIN", "GN"), // Guinea
    GLP("GLP", "GP"), // Guadeloupe
    GMB("GMB", "GM"), // Gambia
    GNB("GNB", "GW"), // Guinea-Bissau
    GNQ("GNQ", "GQ"), // Equatorial Guinea
    GRC("GRC", "GR"), // Greece
    GRD("GRD", "GD"), // Grenada
    GRL("GRL", "GL"), // Greenland
    GTM("GTM", "GT"), // Guatemala
    GUF("GUF", "GF"), // French Guiana
    GUM("GUM", "GU"), // Guam
    GUY("GUY", "GY"), // Guyana
    HKG("HKG", "HK"), // Hong Kong
    HMD("HMD", "HM"), // Heard Island and McDonald Islands
    HND("HND", "HN"), // Honduras
    HRV("HRV", "HR"), // Croatia
    HTI("HTI", "HT"), // Haiti
    HUN("HUN", "HU"), // Hungary
    IDN("IDN", "ID"), // Indonesia
    IMN("IMN", "IM"), // Isle of Man
    IND("IND", "IN"), // India
    IOT("IOT", "IO"), // British Indian Ocean Territory
    IRL("IRL", "IE"), // Ireland
    IRN("IRN", "IR"), // Iran, Islamic Republic of
    IRQ("IRQ", "IQ"), // Iraq
    ISL("ISL", "IS"), // Iceland
    ISR("ISR", "IL"), // Israel
    ITA("ITA", "IT"), // Italy
    JAM("JAM", "JM"), // Jamaica
    JEY("JEY", "JE"), // Jersey
    JOR("JOR", "JO"), // Jordan
    JPN("JPN", "JP"), // Japan
    KAZ("KAZ", "KZ"), // Kazakhstan
    KEN("KEN", "KE"), // Kenya
    KGZ("KGZ", "KG"), // Kyrgyzstan
    KHM("KHM", "KH"), // Cambodia
    KIR("KIR", "KI"), // Kiribati
    KNA("KNA", "KN"), // Saint Kitts and Nevis
    KOR("KOR", "KR"), // Korea, Republic of
    KWT("KWT", "KW"), // Kuwait
    LAO("LAO", "LA"), // Lao People's Democratic Republic
    LBN("LBN", "LB"), // Lebanon
    LBR("LBR", "LR"), // Liberia
    LBY("LBY", "LY"), // Libya
    LCA("LCA", "LC"), // Saint Lucia
    LIE("LIE", "LI"), // Liechtenstein
    LKA("LKA", "LK"), // Sri Lanka
    LSO("LSO", "LS"), // Lesotho
    LTU("LTU", "LT"), // Lithuania
    LUX("LUX", "LU"), // Luxembourg
    LVA("LVA", "LV"), // Latvia
    MAC("MAC", "MO"), // Macao
    MAF("MAF", "MF"), // Saint Martin (French part)
    MAR("MAR", "MA"), // Morocco
    MCO("MCO", "MC"), // Monaco
    MDA("MDA", "MD"), // Moldova, Republic of
    MDG("MDG", "MG"), // Madagascar
    MDV("MDV", "MV"), // Maldives
    MEX("MEX", "MX"), // Mexico
    MHL("MHL", "MH"), // Marshall Islands
    MKD("MKD", "MK"), // Macedonia, the former Yugoslav Republic of
    MLI("MLI", "ML"), // Mali
    MLT("MLT", "MT"), // Malta
    MMR("MMR", "MM"), // Myanmar
    MNE("MNE", "ME"), // Montenegro
    MNG("MNG", "MN"), // Mongolia
    MNP("MNP", "MP"), // Northern Mariana Islands
    MOZ("MOZ", "MZ"), // Mozambique
    MRT("MRT", "MR"), // Mauritania
    MSR("MSR", "MS"), // Montserrat
    MTQ("MTQ", "MQ"), // Martinique
    MUS("MUS", "MU"), // Mauritius
    MWI("MWI", "MW"), // Malawi
    MYS("MYS", "MY"), // Malaysia
    MYT("MYT", "YT"), // Mayotte
    NAM("NAM", "NA"), // Namibia
    NCL("NCL", "NC"), // New Caledonia
    NER("NER", "NE"), // Niger
    NFK("NFK", "NF"), // Norfolk Island
    NGA("NGA", "NG"), // Nigeria
    NIC("NIC", "NI"), // Nicaragua
    NIU("NIU", "NU"), // Niue
    NLD("NLD", "NL"), // Netherlands
    NOR("NOR", "NO"), // Norway
    NPL("NPL", "NP"), // Nepal
    NRU("NRU", "NR"), // Nauru
    NZL("NZL", "NZ"), // New Zealand
    OMN("OMN", "OM"), // Oman
    PAK("PAK", "PK"), // Pakistan
    PAN("PAN", "PA"), // Panama
    PCN("PCN", "PN"), // Pitcairn
    PER("PER", "PE"), // Peru
    PHL("PHL", "PH"), // Philippines
    PLW("PLW", "PW"), // Palau
    PNG("PNG", "PG"), // Papua New Guinea
    POL("POL", "PL"), // Poland
    PRI("PRI", "PR"), // Puerto Rico
    PRK("PRK", "KP"), // Korea, Democratic People's Republic of
    PRT("PRT", "PT"), // Portugal
    PRY("PRY", "PY"), // Paraguay
    PSE("PSE", "PS"), // Palestinian Territory, Occupied
    PYF("PYF", "PF"), // French Polynesia
    QAT("QAT", "QA"), // Qatar
    REU("REU", "RE"), // Réunion
    ROU("ROU", "RO"), // Romania
    RUS("RUS", "RU"), // Russian Federation
    RWA("RWA", "RW"), // Rwanda
    SAU("SAU", "SA"), // Saudi Arabia
    SDN("SDN", "SD"), // Sudan
    SEN("SEN", "SN"), // Senegal
    SGP("SGP", "SG"), // Singapore
    SGS("SGS", "GS"), // South Georgia and the South Sandwich Islands
    SHN("SHN", "SH"), // Saint Helena, Ascension and Tristan da Cunha
    SJM("SJM", "SJ"), // Svalbard and Jan Mayen
    SLB("SLB", "SB"), // Solomon Islands
    SLE("SLE", "SL"), // Sierra Leone
    SLV("SLV", "SV"), // El Salvador
    SMR("SMR", "SM"), // San Marino
    SOM("SOM", "SO"), // Somalia
    SPM("SPM", "PM"), // Saint Pierre and Miquelon
    SRB("SRB", "RS"), // Serbia
    SSD("SSD", "SS"), // South Sudan
    STP("STP", "ST"), // Sao Tome and Principe
    SUR("SUR", "SR"), // Suriname
    SVK("SVK", "SK"), // Slovakia
    SVN("SVN", "SI"), // Slovenia
    SWE("SWE", "SE"), // Sweden
    SWZ("SWZ", "SZ"), // Swaziland
    SXM("SXM", "SX"), // Sint Maarten (Dutch part)
    SYC("SYC", "SC"), // Seychelles
    SYR("SYR", "SY"), // Syrian Arab Republic
    TCA("TCA", "TC"), // Turks and Caicos Islands
    TCD("TCD", "TD"), // Chad
    TGO("TGO", "TG"), // Togo
    THA("THA", "TH"), // Thailand
    TJK("TJK", "TJ"), // Tajikistan
    TKL("TKL", "TK"), // Tokelau
    TKM("TKM", "TM"), // Turkmenistan
    TLS("TLS", "TL"), // Timor-Leste
    TON("TON", "TO"), // Tonga
    TTO("TTO", "TT"), // Trinidad and Tobago
    TUN("TUN", "TN"), // Tunisia
    TUR("TUR", "TR"), // Turkey
    TUV("TUV", "TV"), // Tuvalu
    TWN("TWN", "TW"), // Taiwan, Province of China
    TZA("TZA", "TZ"), // Tanzania, United Republic of
    UGA("UGA", "UG"), // Uganda
    UKR("UKR", "UA"), // Ukraine
    UMI("UMI", "UM"), // United States Minor Outlying Islands
    URY("URY", "UY"), // Uruguay
    USA("USA", "US"), // United States
    UZB("UZB", "UZ"), // Uzbekistan
    VAT("VAT", "VA"), // Holy See (Vatican City State)
    VCT("VCT", "VC"), // Saint Vincent and the Grenadines
    VEN("VEN", "VE"), // Venezuela, Bolivarian Republic of
    VGB("VGB", "VG"), // Virgin Islands, British
    VIR("VIR", "VI"), // Virgin Islands, U.S.
    VNM("VNM", "VN"), // Viet Nam
    VUT("VUT", "VU"), // Vanuatu
    WLF("WLF", "WF"), // Wallis and Futuna
    WSM("WSM", "WS"), // Samoa
    XKS("XKS", "XK"), // Kosovo
    YEM("YEM", "YE"), // Yemen
    ZAF("ZAF", "ZA"), // South Africa
    ZMB("ZMB", "ZM"), // Zambia
    ZWE("ZWE", "ZW"); // Zimbabwe

    /**
     * Helper function to check if [CountryId] uses Miles and Feet.
     */
    fun usesMilesAndFeet(): Boolean =
        when (this) {
            USA,
            PRI,
            VIR -> true
            else -> false
        }

    /**
     * Helper function to check if [CountryId] uses Miles and Yards.
     */
    fun usesMilesAndYards(): Boolean =
        this == GBR

    companion object {
        private val isoCountryMap = mutableMapOf<String, CountryId>().apply {
            putAll(values().map { it.isoCode to it })
            putAll(values().map { it.iso2Code to it })
        }

        /**
         * Helper function to find [CountryId] based on its ISO 3166 string representation.
         */
        fun getCountryId(isoCode: String?): CountryId? {
            if (isoCode != null) {
                val thisIsoCode = isoCode.toUpperCase(Locale.ROOT)
                return isoCountryMap[thisIsoCode]
            }
            return null
        }
    }
}
