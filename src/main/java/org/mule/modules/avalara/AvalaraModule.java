/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.modules.avalara;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.avalara.api.AvalaraClient;
import org.mule.modules.avalara.api.DefaultAvalaraClient;
import org.mule.modules.avalara.api.MapBuilder;

import ar.com.zauber.commons.mom.CXFStyle;
import ar.com.zauber.commons.mom.MapObjectMapper;

import com.avalara.avatax.services.CancelTaxRequest;
import com.avalara.avatax.services.CancelTaxResult;
import com.avalara.avatax.services.CommitTaxRequest;
import com.avalara.avatax.services.CommitTaxResult;
import com.avalara.avatax.services.GetTaxHistoryRequest;
import com.avalara.avatax.services.GetTaxHistoryResult;
import com.avalara.avatax.services.GetTaxRequest;
import com.avalara.avatax.services.GetTaxResult;
import com.avalara.avatax.services.PingResult;

/**
 * Module
 *
 * @author MuleSoft, Inc.
 */
@Module(name = "avalara", schemaVersion = "1.0-SNAPSHOT")
public class AvalaraModule
{
    /**
     * Avalara's username
     */
    @Configurable
    private String username;

    /**
     * Avalara's password
     */
    @Configurable
    private String password;
    
    /**
     * Avalara client. By default uses DefaultAvalaraClient class.
     */
    @Configurable
    @Optional
    private AvalaraClient client;
    
    private MapObjectMapper mom = new MapObjectMapper("com.avalara.avatax.services");
    
    public PingResult ping()
    {
        return client.ping();
    }
    /**
     * Get Tax processor.
     * <p>
     * The Get Tax operation calculates tax for one or more invoiced items and 
     * displays details describing the calculation of tax for each line item.
     *
     * {@sample.xml doc/avalara-connector.xml.sample avalara:get-tax}
     * 
     * @param companyCode Client application company reference code
     * @param docType The document types supported include SalesOrder, SalesInvoice 
     *                and PurchaseOrder
     * @param docCode Client application identifier describing this tax transaction
     * @param docDate Date of invoice, purchase order, etc.
     * @param salespersonCode todo
     * @param customerCode Client application customer reference code 
     * @param customerUsageType Client application customer or usage type.
     *                          CustomerUsageType determines the exempt status of 
     *                          the transaction based on the exemption tax rules for 
     *                          the jurisdictions involved. CustomerUsageType may 
     *                          also be set at the line item level.
     * @param discount Discount amount applied to transaction
     * @param purchaseOrderNo Purchase order identifier. PurchaseOrderNo is required 
     *                        for single use exemption certificates to match the 
     *                        order and invoice with the certificate.
     * @param exemptionNo Exemption number used for this transaction
     * @param originCode todo
     * @param destinationCode todo
     * @param baseAddresses Collection of physical addresses that will be referred 
     *                      to as the destination or origination of 1 or more invoice 
     *                      line entries
     * @param listOfLines Collection of invoice lines requiring tax calculation
     * @param detailLevel Specifies the level of tax detail to return
     * @param referenceCode todo
     * @param hashCode todo
     * @param locationCode todo
     * @param commit Commit flag. If Commit is set to true, tax for the transaction 
     *               is saved and posted as tax document.
     * @param batchCode todo
     * @param taxOverride todo
     * @param currencyCode todo
     * @param serviceMode todo
     * @param paymentDate The date on which payment was made
     * @param exchangeRate todo
     * @param exchangeRateEffDate todo
     * @return The {@link GetTaxResult}
     */
    @Processor
    public GetTaxResult getTax(@Optional String companyCode,
                               AvalaraDocumentType docType,
                               @Optional String docCode,
                               Date docDate,
                               @Optional String salespersonCode,
                               @Optional String customerCode,
                               @Optional String customerUsageType,
                               String discount,
                               @Optional String purchaseOrderNo,
                               @Optional String exemptionNo,
                               @Optional String originCode,
                               @Optional String destinationCode,
                               @Optional List<Map<String, Object>> baseAddresses,
                               @Optional List<Map<String, Object>> listOfLines,
                               DetailLevelType detailLevel,
                               @Optional String referenceCode,
                               @Optional int hashCode,
                               @Optional String locationCode,
                               @Optional @Default("false") boolean commit,
                               @Optional String batchCode,
                               @Optional Map<String, Object> taxOverride,
                               @Optional String currencyCode,
                               ServiceModeType serviceMode,
                               Date paymentDate,
                               String exchangeRate,
                               Date exchangeRateEffDate)
    {
        BigDecimal discountDecimal = discount == null ? null :  new BigDecimal(discount);
        BigDecimal exchangeRatetDecimal = exchangeRate == null ? null :  new BigDecimal(exchangeRate);
        
        Map<String, Object> addresses = null;
        if (baseAddresses != null && !baseAddresses.isEmpty())
        {
            addresses = new HashMap<String, Object>();
            addresses.put("baseAddress", baseAddresses);
        }
        
        Map<String, Object> lines = null;
        if (listOfLines != null && !listOfLines.isEmpty())
        {
            addresses = new HashMap<String, Object>();
            addresses.put("baseAddress", listOfLines);
        }
        
        return client.sendToAvalara(EntityType.GetTax, mom.toObject(GetTaxRequest.class,            
                new MapBuilder()
                .with("companyCode", companyCode)
                .with("docType", docType.toDocumentType())
                .with("docCode", docCode)
                .with("docDate", docDate)
                .with("salespersonCode", salespersonCode)
                .with("currencyCode", customerCode)
                .with("customerUsageType", customerUsageType)
                .with("discount", discountDecimal)
                .with("purchaseOrderNo", purchaseOrderNo)
                .with("exemptionNo", exemptionNo)
                .with("originCode", originCode)
                .with("destinationCode", destinationCode)
                .with("addresses", addresses)
                .with("lines", lines)
                .with("detailLevel", detailLevel.toAvalaraDetailLevel())
                .with("referenceCode", referenceCode)
                .with("hashCode", hashCode)
                .with("locationCode", locationCode)
                .with("commit", commit) 
                .with("batchCode", batchCode)
                .with("taxOverride", taxOverride)
                .with("currencyCode", currencyCode)
                .with("serviceMode", serviceMode.toAvalaraServiceMode())
                .with("paymentDate", paymentDate)
                .with("exchangeRate", exchangeRatetDecimal)
                .with("exchangeRateEffDate", exchangeRateEffDate)
                .build()
            )
        );
    }

    /**
     * Commit Tax processor
     *
     * {@sample.xml doc/avalara-connector.xml.sample avalara:commit-tax}
     *
     * @param docId todo
     * @param companyCode todo
     * @param docType todo
     * @param docCode todo
     * @param newDocCode todo
     * @return The {@link CommitTaxRequest}
     */
    @Processor
    public CommitTaxResult commitTax(@Optional String docId,
                                     @Optional String companyCode,
                                     AvalaraDocumentType docType,
                                     @Optional String docCode,
                                     @Optional String newDocCode)
    {
        return (CommitTaxResult) client.sendToAvalara(EntityType.CommitTax,
            mom.toObject(CommitTaxRequest.class,            
                new MapBuilder()
                .with("docId", docId)
                .with("companyCode", companyCode)
                .with("docType", docType.toDocumentType())
                .with("docCode", docCode)
                .with("newDocCode", newDocCode)
                .build()
            )
        );
    }
    
    /**
     * Get Tax History processor
     *
     * {@sample.xml doc/avalara-connector.xml.sample avalara:get-tax-history}
     *
     * @param docId todo
     * @param companyCode todo
     * @param docType todo
     * @param docCode todo
     * @param detailLevel todo
     * @return The {@link GetTaxHistoryResult}
     */
    @Processor
    public GetTaxHistoryResult getTaxHistory(@Optional String docId,
                                             @Optional String companyCode,
                                             AvalaraDocumentType docType,
                                             @Optional String docCode,
                                             DetailLevelType detailLevel)
    {
        return (GetTaxHistoryResult) client.sendToAvalara(EntityType.GetTaxHistory,
            mom.toObject(GetTaxHistoryRequest.class,            
                new MapBuilder()
                .with("docId", docId)
                .with("companyCode", companyCode)
                .with("docType", docType.toDocumentType())
                .with("docCode", docCode)
                .with("detailLevel", detailLevel.toAvalaraDetailLevel())
                .build()
            )
        );
    }
    
    /**
     * Cancel Tax processor
     *
     * {@sample.xml doc/avalara-connector.xml.sample avalara:cancel-tax}
     * 
     * @param docId todo
     * @param companyCode todo
     * @param docType todo
     * @param docCode todo
     * @param cancelCode todo
     * @return The {@link CancelTaxResult}
     */
    @Processor
    public CancelTaxResult cancelTax(@Optional String docId,
                                     @Optional String companyCode,
                                     AvalaraDocumentType docType,
                                     @Optional String docCode,
                                     CancelCodeType cancelCode)
    {
        return (CancelTaxResult) client.sendToAvalara(EntityType.CancelTax,
            mom.toObject(CancelTaxRequest.class,            
                new MapBuilder()
                .with("docId", docId)
                .with("companyCode", companyCode)
                .with("docType", docType.toDocumentType())
                .with("docCode", docCode)
                .with("cancelCode", cancelCode.toAvalaraCancelCode())
                .build()
            )
        );
    }
    
    /**
     * Validate Address processor.
     * <p>
     * This operation validates the supplied address, returning canonical form and 
     * additional delivery details if successfully validated.
     *
     * {@sample.xml doc/avalara-connector.xml.sample avalara:validate-address}
     * @param line1 Address line 1
     * @param line2 Address line 2
     * @param line3 Address line 3
     * @param city City name. Required, when PostalCode is not specified.
     * @param region State or region name. Requirad, when PostalCode is not specified.
     * @param country Country code
     * @param postalCode Postal or ZIP code. Required, when City and Region are not 
     *                   specified
     */
    @Processor
    public void validateAddress(String line1,
                                @Optional String line2,
                                @Optional String line3, 
                                @Optional String city,
                                @Optional String region,
                                @Optional String country,
                                @Optional String postalCode)
    {
        throw new NotImplementedException();
    }
    /**
     * 
     */
    @PostConstruct
    public void init()
    {
        if (client == null )
        {
            client = new DefaultAvalaraClient(username, password);
        }
        mom.setPropertyStyle(CXFStyle.STYLE);
    }
    
    /**
     * Returns the username.
     * 
     * @return  with the username.
     */
    
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username. 
     *
     * @param username  with the username.
     */
    
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Returns the password.
     * 
     * @return  with the password.
     */
    
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password. 
     *
     * @param password  with the password.
     */
    
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Returns the client.
     * 
     * @return  with the client.
     */
    
    public AvalaraClient getClient()
    {
        return client;
    }

    /**
     * Sets the client. 
     *
     * @param client  with the client.
     */
    
    public void setClient(AvalaraClient client)
    {
        this.client = client;
    }
    
    private final DatatypeFactory datatypeFactory;
    {
        mom.registerConverter(new Converter()
        {
            
            @SuppressWarnings("rawtypes")
            @Override
            public Object convert(Class arg0, Object arg1)
            {
                Validate.isTrue(arg0 == XMLGregorianCalendar.class);
                
                return toGregorianCalendar((Date) arg1);
            }
        }, XMLGregorianCalendar.class);
        try
        {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch (DatatypeConfigurationException e)
        {
            throw new AssertionError(e);
        }
    }
    
    private XMLGregorianCalendar toGregorianCalendar(Date openingBalanceDate)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(openingBalanceDate);
        return datatypeFactory.newXMLGregorianCalendar(cal);
    }
}
