package com.avalara.avatax.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.5.2
 * 2015-11-11T16:45:33.322-03:00
 * Generated source version: 2.5.2
 * 
 */
@WebService(targetNamespace = "http://avatax.avalara.com/services", name = "BatchSvcSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface BatchSvcSoap {

    /**
     * Saves a Batch entry
     */
    @WebResult(name = "BatchSaveResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchSave", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchSave")
    @WebMethod(operationName = "BatchSave", action = "http://avatax.avalara.com/services/BatchSave")
    @ResponseWrapper(localName = "BatchSaveResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchSaveResponse")
    public com.avalara.avatax.services.BatchSaveResult batchSave(
        @WebParam(name = "Batch", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.Batch batch
    );

    /**
     * Deletes one or more BatchFiles
     *             
     */
    @WebResult(name = "BatchFileDeleteResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchFileDelete", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileDelete")
    @WebMethod(operationName = "BatchFileDelete", action = "http://avatax.avalara.com/services/BatchFileDelete")
    @ResponseWrapper(localName = "BatchFileDeleteResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileDeleteResponse")
    public com.avalara.avatax.services.DeleteResult batchFileDelete(
        @WebParam(name = "DeleteRequest", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.DeleteRequest deleteRequest
    );

    /**
     * Tests connectivity and version of the
     *                 service
     *             
     */
    @WebResult(name = "PingResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "Ping", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.Ping")
    @WebMethod(operationName = "Ping", action = "http://avatax.avalara.com/services/Ping")
    @ResponseWrapper(localName = "PingResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.PingResponse")
    public com.avalara.avatax.services.PingResult ping(
        @WebParam(name = "Message", targetNamespace = "http://avatax.avalara.com/services")
        java.lang.String message
    );

    /**
     * Deletes one or more Batches
     *             
     */
    @WebResult(name = "BatchDeleteResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchDelete", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchDelete")
    @WebMethod(operationName = "BatchDelete", action = "http://avatax.avalara.com/services/BatchDelete")
    @ResponseWrapper(localName = "BatchDeleteResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchDeleteResponse")
    public com.avalara.avatax.services.DeleteResult batchDelete(
        @WebParam(name = "DeleteRequest", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.DeleteRequest deleteRequest
    );

    /**
     * Checks authentication and authorization to one or more operations on the service.
     *             
     */
    @WebResult(name = "IsAuthorizedResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "IsAuthorized", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.IsAuthorized")
    @WebMethod(operationName = "IsAuthorized", action = "http://avatax.avalara.com/services/IsAuthorized")
    @ResponseWrapper(localName = "IsAuthorizedResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.IsAuthorizedResponse")
    public com.avalara.avatax.services.IsAuthorizedResult isAuthorized(
        @WebParam(name = "Operations", targetNamespace = "http://avatax.avalara.com/services")
        java.lang.String operations
    );

    /**
     * Fetches one or more BatchFiles
     *             
     */
    @WebResult(name = "BatchFileFetchResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchFileFetch", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileFetch")
    @WebMethod(operationName = "BatchFileFetch", action = "http://avatax.avalara.com/services/BatchFileFetch")
    @ResponseWrapper(localName = "BatchFileFetchResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileFetchResponse")
    public com.avalara.avatax.services.BatchFileFetchResult batchFileFetch(
        @WebParam(name = "FetchRequest", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.FetchRequest fetchRequest
    );

    /**
     * Fetches one or more Batch
     *             
     */
    @WebResult(name = "BatchFetchResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchFetch", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFetch")
    @WebMethod(operationName = "BatchFetch", action = "http://avatax.avalara.com/services/BatchFetch")
    @ResponseWrapper(localName = "BatchFetchResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFetchResponse")
    public com.avalara.avatax.services.BatchFetchResult batchFetch(
        @WebParam(name = "FetchRequest", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.FetchRequest fetchRequest
    );

    /**
     * Processes one or more Batches
     *             
     */
    @WebResult(name = "BatchProcessResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchProcess", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchProcess")
    @WebMethod(operationName = "BatchProcess", action = "http://avatax.avalara.com/services/BatchProcess")
    @ResponseWrapper(localName = "BatchProcessResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchProcessResponse")
    public com.avalara.avatax.services.BatchProcessResult batchProcess(
        @WebParam(name = "BatchProcessRequest", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.BatchProcessRequest batchProcessRequest
    );

    /**
     * Saves a Batch File
     */
    @WebResult(name = "BatchFileSaveResult", targetNamespace = "http://avatax.avalara.com/services")
    @RequestWrapper(localName = "BatchFileSave", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileSave")
    @WebMethod(operationName = "BatchFileSave", action = "http://avatax.avalara.com/services/BatchFileSave")
    @ResponseWrapper(localName = "BatchFileSaveResponse", targetNamespace = "http://avatax.avalara.com/services", className = "com.avalara.avatax.services.BatchFileSaveResponse")
    public com.avalara.avatax.services.BatchFileSaveResult batchFileSave(
        @WebParam(name = "BatchFile", targetNamespace = "http://avatax.avalara.com/services")
        com.avalara.avatax.services.BatchFile batchFile
    );
}
