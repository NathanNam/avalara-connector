/**
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.modules.avalara;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.mule.api.ConnectionException;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.avalara.api.AvalaraClient;
import org.mule.modules.avalara.api.DefaultAvalaraClient;
import org.mule.modules.avalara.api.MapBuilder;
import org.mule.modules.avalara.exception.AvalaraRuntimeException;
import org.mule.modules.utils.mom.JaxbMapObjectMappers;

import com.avalara.avatax.services.AdjustTaxRequest;
import com.avalara.avatax.services.AdjustTaxResult;
import com.avalara.avatax.services.ArrayOfBatchFile;
import com.avalara.avatax.services.BaseAddress;
import com.avalara.avatax.services.Batch;
import com.avalara.avatax.services.BatchFetchResult;
import com.avalara.avatax.services.BatchFile;
import com.avalara.avatax.services.BatchFileFetchResult;
import com.avalara.avatax.services.BatchSaveResult;
import com.avalara.avatax.services.CancelTaxRequest;
import com.avalara.avatax.services.CancelTaxResult;
import com.avalara.avatax.services.CommitTaxRequest;
import com.avalara.avatax.services.CommitTaxResult;
import com.avalara.avatax.services.FetchRequest;
import com.avalara.avatax.services.GetTaxHistoryRequest;
import com.avalara.avatax.services.GetTaxHistoryResult;
import com.avalara.avatax.services.GetTaxRequest;
import com.avalara.avatax.services.GetTaxResult;
import com.avalara.avatax.services.PingResult;
import com.avalara.avatax.services.PostTaxRequest;
import com.avalara.avatax.services.PostTaxResult;
import com.avalara.avatax.services.ValidateRequest;
import com.avalara.avatax.services.ValidateResult;
import com.zauberlabs.commons.mom.MapObjectMapper;

/**
 * Avalara provides automated sales tax solutions to streamline cumbersome, 
 * error-prone tax compliance processes and reduce the risk of loss or penalty 
 * in case of an audit. Their automated solutions automatically perform address 
 * validation, jurisdiction research and rate calculation and allow you to 
 * manage even the most complicated tax issues, such as situs, nexus, tax tiers, 
 * tax holidays, exemptions, certificate management and product taxability rules.
 *
 * @author Gaston Ponti
 */
@Connector(name = "avalara", schemaVersion = "2.0", friendlyName = "Avalara")
public class AvalaraModule
{
    /**
     * Tax Webservice endpoint
     */
    @Configurable
    @Optional
    @Default("https://development.avalara.net/Tax/TaxSvc.asmx")
    @Placement(group = "Connection")
    private String taxServiceEndpoint;
    
    /**
     * Address Webservice endpoint
     */
    @Configurable
    @Optional
    @Default("https://development.avalara.net/Address/AddressSvc.asmx")
    @Placement(group = "Connection")
    private String addressServiceEndpoint;


    /**
     * Batch Webservice endpoint
     */
    @Configurable
    @Optional
    @Default("https://development.avalara.net/Batch/BatchSvc.asmx")
    @Placement(group = "Connection")
    private String batchServiceEndpoint;

    /**
     * Avalara's application client. By default uses DefaultAvalaraClient class.
     */
    private AvalaraClient apiClient;
    
    private final MapObjectMapper mom = JaxbMapObjectMappers.defaultWithPackage("com.avalara.avatax.services").build();

    /**
     * Ping Avalara to test connectivity and version of the service.
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:ping}
     *
     * @param message Ping Message
     * @return The {@link PingResult}
     *
     * @throws AvalaraRuntimeException
     */
    @Processor
    public PingResult ping(@Optional String message) {
        return apiClient.ping(message);
    }

    /**
     * Get Tax processor.
     * <p>
     * The Get Tax operation calculates tax for one or more invoiced items and 
     * displays details describing the calculation of tax for each line item.
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:get-tax}
     * 
     * @param getTaxRequest a {@link PostTaxRequest} to post. Its fields represent:
     * <ul>
     *  <li>companyCode Client application company reference code</li>
     *  <li>docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see
     *                {@link AvalaraDocumentType} for more information about the specific
     *                document types.</li>
     *  <li>docCode The internal reference code used by the client application.</li>
     *  <li>docDate Date of invoice, purchase order, etc.</li>
     *  <li>salespersonCode The client application salesperson reference code.</li>
     *  <li>customerCode Client application customer reference code.</li>
     *  <li>customerUsageType Client application customer or usage type.
     *                          CustomerUsageType determines the exempt status of
     *                          the transaction based on the exemption tax rules for
     *                          the jurisdictions involved. CustomerUsageType may
     *                          also be set at the line item level.
     *                          <p>The standard values for the CustomerUsageType (A-L).<br/>
        A Federal Government<br/>
        B State/Local Govt.<br/>
        C Tribal Government<br/>
        D Foreign Diplomat<br/>
        E Charitable Organization<br/>
        F Religious/Education<br/>
        G Resale<br/>
        H Agricultural Production<br/>
        I Industrial Prod/Mfg.<br/>
        J Direct Pay Permit<br/>
        K Direct Mail<br/>
        L - Other</li>
     *  <li>discount The discount amount to apply to the document.</li>
     *  <li>purchaseOrderNo Purchase order identifier. PurchaseOrderNo is required
     *                        for single use exemption certificates to match the
     *                        order and invoice with the certificate.</li>
     *  <li>exemptionNo Exemption number used for this transaction</li>
     *  <li>originCode Code that refers one of the address of the baseAddress collection.
     *                   It has to be the same code of one of the address's addressCode.
     *                   It represents the origin address.</li>
     *  <li>destinationCode Code that refers one of the address of the baseAddress collection.
     *                        It has to be the same code of one of the address's addressCode.
     *                        It represents the destination address.</li>
     *  <li>baseAddresses Collection of physical addresses that will be referred
     *                      to as the destination or origination of 1 or more invoice
     *                      line entries.</li>
     *  <li>lines Collection of invoice lines requiring tax calculation.</li>
     *  <li>detailLevel Specifies the level of tax detail to return</li>
     *  <li>referenceCode For returns (see {@link AvalaraDocumentType}), refers to the
     *                      {@link GetTaxRequest#getDocCode} of the original invoice.</li>
     *  <li>locationCode Location Code value. It is Also referred to as a Store
     *                     Location, Outlet Id, or Outlet code is a number assigned by
     *                     the State which identifies a Store location. Some state returns
     *                     require taxes are broken out separately for Store Locations.</li>
     *  <li>commit Commit flag. If Commit is set to true, tax for the transaction
     *               is saved, posted and committed as tax document.</li>
     *  <li>batchCode The batchCode value.</li>
     *  <li>taxOverride Indicates to apply tax override to the document.</li>
     *  <li>currencyCode It is 3 character ISO 4217 currency code.</li>
     *  <li>serviceMode This is only supported by AvaLocal servers. It provides the
     *                    ability to controls whether tax is calculated locally or remotely
     *                    when using an AvaLocal server. The default is Automatic which
     *                    calculates locally unless remote is necessary for non-local
     *                    addresses.</li>
     *  <li>paymentDate The date on which payment was made.</li>
     *  <li>exchangeRate The exchange rate value.</li>
     *  <li>exchangeRateEffDate The exchange rate effective date value.</li>
     * </ul>
     *
     * @return The {@link GetTaxResult}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public GetTaxResult getTax(final @Optional @Default("#[payload]") GetTaxRequest getTaxRequest) {
        return apiClient.sendToAvalara(TaxRequestType.GetTax, getTaxRequest);
    }

    /**
     * Adjust Tax processor.
     * <p>
     * The Get Tax operation calculates tax for one or more invoiced items and
     * displays details describing the calculation of tax for each line item.
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:adjust-tax}
     *
     * @param companyCode Client application company reference code
     * @param adjustmentReason The reason for this tax adjustment.
     * @param adjustmentDescription A description of a tax adjustment.
     * @param docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see
     *                {@link AvalaraDocumentType} for more information about the specific
     *                document types.
     * @param docCode The internal reference code used by the client application.
     * @param docDate Date of invoice, purchase order, etc.
     * @param salespersonCode The client application salesperson reference code.
     * @param customerCode Client application customer reference code
     * @param customerUsageType Client application customer or usage type.
     *                          CustomerUsageType determines the exempt status of
     *                          the transaction based on the exemption tax rules for
     *                          the jurisdictions involved. CustomerUsageType may
     *                          also be set at the line item level. <p>
     *                          The standard values for the CustomerUsageType (A-L).<br/>
    A Federal Government<br/>
    B State/Local Govt.<br/>
    C Tribal Government<br/>
    D Foreign Diplomat<br/>
    E Charitable Organization<br/>
    F Religious/Education<br/>
    G Resale<br/>
    H Agricultural Production<br/>
    I Industrial Prod/Mfg.<br/>
    J Direct Pay Permit<br/>
    K Direct Mail<br/>
    L - Other
     * @param discount The discount amount to apply to the document. The string
     *                 represents a {@link BigDecimal}.
     * @param purchaseOrderNo Purchase order identifier. PurchaseOrderNo is required
     *                        for single use exemption certificates to match the
     *                        order and invoice with the certificate.
     * @param exemptionNo Exemption number used for this transaction
     * @param originCode Code that refers one of the address of the baseAddress collection.
     *                   It has to be the same code of one of the address's addressCode.
     *                   It represents the origin address.
     * @param destinationCode Code that refers one of the address of the baseAddress collection.
     *                        It has to be the same code of one of the address's addressCode.
     *                        It represents the destination address.
     * @param baseAddresses Collection of physical addresses that will be referred
     *                      to as the destination or origination of 1 or more invoice
     *                      line entries
     * @param lines Collection of invoice lines requiring tax calculation
     * @param detailLevel Specifies the level of tax detail to return
     * @param referenceCode For returns (see {@link AvalaraDocumentType}), refers to the
     *                      {@link AdjustTaxRequest} of the original invoice.
     * @param locationCode Location Code value. It is Also referred to as a Store
     *                     Location, Outlet Id, or Outlet code is a number assigned by
     *                     the State which identifies a Store location. Some state returns
     *                     require taxes are broken out separately for Store Locations.
     * @param commit Commit flag. If Commit is set to true, tax for the transaction
     *               is saved, posted and committed as tax document.
     * @param batchCode The batchCode value.
     * @param taxOverride Indicates to apply tax override to the document.
     * @param currencyCode It is 3 character ISO 4217 currency code.
     * @param serviceMode This is only supported by AvaLocal servers. It provides the
     *                    ability to controls whether tax is calculated locally or remotely
     *                    when using an AvaLocal server. The default is Automatic which
     *                    calculates locally unless remote is necessary for non-local
     *                    addresses.
     * @param paymentDate The date on which payment was made.
     * @param exchangeRate The exchange rate value. The string represents a
     *                     {@link BigDecimal}
     * @param exchangeRateEffDate The exchange rate effective date value.
     * @return The {@link AdjustTaxResult}
     *
     * @throws AvalaraRuntimeException
     */
    @Processor
    public AdjustTaxResult adjustTax(int adjustmentReason,
                                     String   adjustmentDescription,
                               String companyCode,
                               AvalaraDocumentType docType,
                               @Optional String docCode,
                               XMLGregorianCalendar docDate,
                               @Optional String salespersonCode,
                               String customerCode,
                               @Optional String customerUsageType,
                               String discount,
                               @Optional String purchaseOrderNo,
                               @Optional String exemptionNo,
                               String originCode,
                               String destinationCode,
                               List<Map<String, Object>> baseAddresses,
                               List<Map<String, Object>> lines,
                               DetailLevelType detailLevel,
                               @Optional String referenceCode,
                               @Optional String locationCode,
                               @Optional @Default("false") boolean commit,
                               @Optional String batchCode,
                               @Optional Map<String, Object> taxOverride,
                               @Optional String currencyCode,
                               @Optional @Default("AUTOMATIC") ServiceModeType serviceMode,
                               XMLGregorianCalendar paymentDate,
                               String exchangeRate,
                               XMLGregorianCalendar exchangeRateEffDate) {
        BigDecimal discountDecimal = discount == null ? null :  new BigDecimal(discount);
        BigDecimal exchangeRateDecimal = exchangeRate == null ? null :  new BigDecimal(exchangeRate);

        Map<String, Object> addresses = null;
        if (baseAddresses != null && !baseAddresses.isEmpty()) {
            addresses = new HashMap<String, Object>();
            addresses.put("baseAddress", baseAddresses);
        }

        Map<String, Object> mapLines = null;
        if (lines != null && !lines.isEmpty()) {
            mapLines = new HashMap<String, Object>();
            mapLines.put("line", lines);
        }
        GetTaxRequest getTaxRequest = (GetTaxRequest) mom.unmap(new MapBuilder().with("companyCode", companyCode)
                .with("docType", docType.toDocumentType())
                .with("docCode", docCode)
                .with("docDate", docDate)
                .with("salespersonCode", salespersonCode)
                .with("customerCode", customerCode)
                .with("customerUsageType", customerUsageType)
                .with("discount", discountDecimal)
                .with("purchaseOrderNo", purchaseOrderNo)
                .with("exemptionNo", exemptionNo)
                .with("originCode", originCode)
                .with("destinationCode", destinationCode)
                .with("addresses", addresses)
                .with("lines", mapLines)
                .with("detailLevel", detailLevel.toAvalaraDetailLevel())
                .with("referenceCode", referenceCode)
                .with("locationCode", locationCode)
                .with("commit", commit)
                .with("batchCode", batchCode)
                .with("taxOverride", taxOverride)
                .with("currencyCode", currencyCode)
                .with("serviceMode", serviceMode.toAvalaraServiceMode())
                .with("paymentDate", paymentDate)
                .with("exchangeRate", exchangeRateDecimal)
                .with("exchangeRateEffDate", exchangeRateEffDate).build(), GetTaxRequest.class);

        return apiClient.sendToAvalara(TaxRequestType.AdjustTax, mom.unmap(
                new MapBuilder()
                        .with("adjustmentReason", adjustmentReason)
                        .with("adjustmentDescription", adjustmentDescription)
                        .with("getTaxRequest", getTaxRequest)
                        .build(), AdjustTaxRequest.class
                )
            );
    }


    /**
     * Post Tax processor
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:post-tax}
     *
     * @param postTaxRequest a {@link PostTaxRequest} to post. Its fields represent:
     * <ul>
     *  <li>docId The original document's type, such as Sales Invoice or Purchase Invoice.</li>
     *  <li>companyCode Client application company reference code. If docId is specified,
     *                this is not needed.</li>
     *  <li>docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see 
     *                {@link AvalaraDocumentType} for more information about the specific 
     *                document types.</li>
     *  <li>docCode The internal reference code used by the client application.</li>
     *  <li>docDate The date on the invoice, purchase order, etc</li>
     *  <li>totalAmount The total amount (not including tax) for the document.
     *                    This is used for verification and reconciliation. This should 
     *                    be the <b>TotalAmount</b> returned by {@link GetTaxResult} when 
     *                    tax was calculated for this document; otherwise the web service 
     *                    will return an error.</li>
     *  <li>totalTax The total tax for the document. This is used for verification
     *                 and reconciliation. This should be the <b>TotalTax</b> returned by 
     *                 {@link GetTaxResult} when tax was calculated for this document;
     *                 otherwise the web service will return an error.</li>
     *  <li>commit The commit value. This has been defaulted to false. If this has 
     *               been set to true AvaTax will commit the document on this call. Seller's 
     *               system who wants to Post and Commit the document on one call should use 
     *               this flag.</li>
     *  <li>newDocCode The new document code value.</li>
     * </ul>
     * @return The {@link PostTaxResult}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public PostTaxResult postTax(final @Optional @Default("#[payload]") PostTaxRequest postTaxRequest) {
        return (PostTaxResult) apiClient.sendToAvalara(TaxRequestType.PostTax, postTaxRequest);
    }

    /**
     * Commit Tax processor
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:commit-tax}
     *
     * @param commitTaxRequest a {@link CommitTaxRequest} to commit. Its fields represent:
     * <ul>
     *  <li>docId The original document's type, such as Sales Invoice or Purchase Invoice.</li>
     *  <li>companyCode Client application company reference code. If docId is specified,
     *                    this is not needed.</li>
     *  <li>docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see 
     *                {@link AvalaraDocumentType} for more information about the specific 
     *                document types.</li>
     *  <li>docCode The internal reference code used by the client application.</li>
     *  <li>newDocCode The new document code value.</li>
     * </ul>
     *
     * @return The {@link CommitTaxRequest}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public CommitTaxResult commitTax(@Optional @Default("#[payload]") CommitTaxRequest commitTaxRequest) {
        return (CommitTaxResult) apiClient.sendToAvalara(
            TaxRequestType.CommitTax, commitTaxRequest);
    }

    /**
     * Get Tax History processor
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:get-tax-history}
     *
     * @param getTaxHistoryRequest a {@link GetTaxHistoryRequest} to post. Its fields represent:
     * 
     * <ul>
     *  <li>docId The original document's type, such as Sales Invoice or Purchase Invoice.</li>
     *  <li>companyCode Client application company reference code. If docId is specified, 
     *                    this is not needed.</li>
     *  <li>docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see 
     *                {@link AvalaraDocumentType} for more information about the specific 
     *                document types.</li>
     *  <li>docCode The internal reference code used by the client application.</li>
     *  <li>detailLevel Specifies the level of detail to return. See {@link DetailLevelType}.</li>
     * </ul>
     * 
     * @return The {@link GetTaxHistoryResult}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public GetTaxHistoryResult getTaxHistory(@Optional @Default("#[payload]") GetTaxHistoryRequest getTaxHistoryRequest) {
        return (GetTaxHistoryResult) apiClient.sendToAvalara(TaxRequestType.GetTaxHistory, getTaxHistoryRequest);
    }

    /**
     * Cancel tax, indicating the document that should be canceled and the reason
     * for the operation.
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:cancel-tax}
     * 
     * @param cancelTaxRequest a {@link CancelTaxRequest} to post. Its fields represent:
     * <ul>
     *  <li>docId The original document's type, such as Sales Invoice or Purchase Invoice.</li>
     *  <li>companyCode Client application company reference code. If docId is specified, 
     *                    this is not needed.</li>
     *  <li>docType The document type specifies the category of the document and affects
     *                how the document is treated after a tax calculation; see 
     *                {@link AvalaraDocumentType} for more information about the specific 
     *                document types.</li>
     *  <li>docCode The internal reference code used by the client application.</li>
     *  <li>cancelCode A code indicating the reason the document is getting canceled.</li>
     * </ul>
     *
     * @return The {@link CancelTaxResult}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public CancelTaxResult cancelTax(@Optional @Default("#[payload]") CancelTaxRequest cancelTaxRequest) {
        return (CancelTaxResult) apiClient.sendToAvalara(TaxRequestType.CancelTax, cancelTaxRequest);
    }

    /**
     * Validate Address processor.
     * <p>
     * This operation validates the supplied address, returning canonical form and 
     * additional delivery details if successfully validated.
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:validate-address}
     * 
     * @param address the @{link BaseAddress} to validate.
     * @param textCase The casing to apply to the validated address(es).
     * @param coordinates True, if you want in the result a not empty latitud and longitude.
     * @param taxability True, if you want the valid taxRegionId in the result.
     * @param date Date.
     * @return The {@link ValidateResult}
     * 
     * @throws AvalaraRuntimeException
     */
    @Processor
    public ValidateResult validateAddress(@Optional @Default("#[payload]") BaseAddress address,
                                          @Optional @Default("DEFAULT") TextCaseType textCase,
                                          @Optional @Default("false") boolean coordinates,
                                          @Optional @Default("false") boolean taxability,
                                          XMLGregorianCalendar date) {
        return apiClient.validateAddress((ValidateRequest) mom.unmap( 
                new MapBuilder()
                .with("address", address)
                .with("textCase", textCase.toAvalaraTextCase())
                .with("coordinates", coordinates)
                .with("taxability", taxability)
                .with("date", date)
                .build(), ValidateRequest.class
            )
        );
    }

    /**
     * Batch Fetch processor.
     * <p>
     * Fetches a Batch result
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:fetch-batch-file}
     *
     * @param batchId The numerical identifier of the BatchFile.
     * @return The {@link Map<String,BatchFileFetchResult>}
     *
     * @throws AvalaraRuntimeException
     */
    @Processor
    public Map<String,BatchFileFetchResult> fetchBatchFile(String batchId) {
        // This Request is needed to retrieve the batch file ids. The actual content cannot be retrieved at once.
        FetchRequest batchFetchRequest = new FetchRequest();
        batchFetchRequest.setFields("Files");
        batchFetchRequest.setFilters("BatchId=" + batchId);
        BatchFetchResult batchFetchResult = apiClient.fetchBatch(batchFetchRequest);
        Map<String,BatchFileFetchResult> resultHashMap = new HashMap<String, BatchFileFetchResult>();
        // This is in order to be able to return result and error file to the caller.
        for (BatchFile bf : batchFetchResult.getBatches().getBatch().get(0).getFiles().getBatchFile()) {
            if (bf.getName().equalsIgnoreCase("Result")) {
                FetchRequest fetchRequest = new FetchRequest();
                fetchRequest.setFields("*,Content");
                fetchRequest.setFilters("BatchFileId=" + bf.getBatchFileId());
                resultHashMap.put("result",apiClient.fetchBatchFile(fetchRequest));
            } else if (bf.getName().equalsIgnoreCase("Error")) {
                FetchRequest fetchRequest = new FetchRequest();
                fetchRequest.setFields("*,Content");
                fetchRequest.setFilters("BatchFileId=" + bf.getBatchFileId());
                resultHashMap.put("error", apiClient.fetchBatchFile(fetchRequest));
            }
        }

        return resultHashMap;
    }


    /**
     * Has the Batch Processing finished
     * <p>
     * Fetches a Batch result
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:is-batch-finished}
     *
     * @param batchId The numerical identifier of the BatchFile.
     * @return a boolean representing if the Batch finished or not
     *
     * @throws AvalaraRuntimeException
     */
    @Processor
    public boolean isBatchFinished(String batchId) {
        final FetchRequest batchFetchRequest = new FetchRequest();
        batchFetchRequest.setFilters("BatchId="+batchId);
        BatchFetchResult batchFetchResult = apiClient.fetchBatch(batchFetchRequest);
        if (batchFetchResult.getBatches().getBatch().size() == 0 || (batchFetchResult.getBatches().getBatch().get(0).getRecordCount() > batchFetchResult.getBatches().getBatch().get(0).getCurrentRecord())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Batch Save processor.
     * <p>
     * Saves a Batch
     *
     * {@sample.xml ../../../doc/avalara-connector.xml.sample avalara:save-batch}
     *
     * @param consoleUserName The username that is used to login to the Avalara console (UI).
     * @param consolePassword The password that is used to login to the Avalara console (UI).
     * @param avalaraClient Avalara's client
     * @param batchType The kind of records to be imported.
     * @param companyId The id of the company. (Need to be retrived from address bar in Avalara after hitting the Organization Tab)
     * @param content The content of this import, usually a csv file.
     * @param batchName The name of the batch.
     * @return The {@link BatchSaveResult}
     *
     * @throws AvalaraRuntimeException
     */

    @Processor
    public BatchSaveResult saveBatch(BatchType batchType,
                                       int companyId,
                                       String content,
                                       @Optional String batchName) {

        final BatchFile batchFile = new BatchFile();
        batchFile.setContent(content.getBytes()); // cxf takes care of base64 encoding
        batchFile.setContentType("application/csv");
        batchFile.setName(batchName+".csv"); // Must set extension, or Avalara will complain about missing ext
        final ArrayOfBatchFile arrayOfBatchFile = new ArrayOfBatchFile();
        arrayOfBatchFile.getBatchFile().add(batchFile);

        //Batch object to contain the file.
        //Have tested batchfile as well but all examples from Avalara is using this one.
        final Batch batch = new Batch();
        batch.setName(batchName);
        batch.setBatchTypeId(batchType.value());
        batch.setCompanyId(companyId);
        batch.setFiles(arrayOfBatchFile);
        return apiClient.saveBatch(batch);
    }

    /**
     * Connects to Avalara
     *
     * @param avalaraAccount Registered Avalara account
     * @param client Target client to which make the call
     * @param avalaraLicense The matching license for the account
     */
    @Connect
    public synchronized void connect(@ConnectionKey String avalaraAccount, @ConnectionKey String client, @Password String avalaraLicense)
            throws ConnectionException {
        if (apiClient == null ) {
            apiClient = new DefaultAvalaraClient(avalaraAccount, client, avalaraLicense, getAddressServiceEndpoint(), getTaxServiceEndpoint());
        }
    }

    @ValidateConnection
    public boolean isConnected() {
        return apiClient != null;
    }

    /**
     * Destroys the session
     */
    @Disconnect
    public synchronized void disconnect() {
        apiClient = null;
    }

    @ConnectionIdentifier
    public String getConnectionIdentifier() {
        return apiClient.getConnectionIdentifier();
    }
    
    /**
     * Sets the apiClient. 
     *
     * @param apiClient  with the apiClient.
     */
    
    public void setClient(AvalaraClient client)
    {
        this.apiClient = client;
    }

    public String getTaxServiceEndpoint() {
        return taxServiceEndpoint;
    }

    public void setTaxServiceEndpoint(String taxServiceEndpoint) {
        this.taxServiceEndpoint = taxServiceEndpoint;
    }

    public String getAddressServiceEndpoint() {
        return addressServiceEndpoint;
    }

    public void setAddressServiceEndpoint(String addressServiceEndpoint) {
        this.addressServiceEndpoint = addressServiceEndpoint;
    }

    public String getBatchServiceEndpoint() {
        return batchServiceEndpoint;
    }

    public void setBatchServiceEndpoint(String batchServiceEndpoint) {
        this.batchServiceEndpoint = batchServiceEndpoint;
    }
}
