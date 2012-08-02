/**
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */

package org.mule.modules.avalara.api;

import org.apache.commons.lang.Validate;
import org.mule.modules.avalara.TaxRequestType;
import org.mule.modules.avalara.exception.AvalaraRuntimeException;
import org.mule.modules.avalara.util.AvalaraProfileHandler;

import com.avalara.avatax.services.AddressSvc;
import com.avalara.avatax.services.AddressSvcSoap;
import com.avalara.avatax.services.BaseResult;
import com.avalara.avatax.services.PingResult;
import com.avalara.avatax.services.SeverityLevel;
import com.avalara.avatax.services.TaxSvc;
import com.avalara.avatax.services.TaxSvcSoap;
import com.avalara.avatax.services.ValidateRequest;
import com.avalara.avatax.services.ValidateResult;
import com.zauberlabs.commons.ws.connection.ConnectionBuilder;
import com.zauberlabs.commons.ws.security.Credential;

import java.lang.reflect.InvocationTargetException;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * @author Gaston Ponti
 * @since Oct 17, 2011
 */
public class DefaultAvalaraClient implements AvalaraClient
{
    private TaxSvcSoap taxSvcSoap;
    private AddressSvcSoap addressSvcSoap;
    private String addressEndpoint;
    private String taxEndpoint;
    
    private ThreadLocal<String> usernameLocal = new ThreadLocal<String>();
    private ThreadLocal<String> passwordLocal = new ThreadLocal<String>();
    private ThreadLocal<String> clientLocal = new ThreadLocal<String>();

    public DefaultAvalaraClient() {

    }
    
    public DefaultAvalaraClient(String addressEndpoint, String taxEndpoint) {
        Validate.notNull(addressEndpoint);
        Validate.notNull(taxEndpoint);
        this.setAddressEndpoint(addressEndpoint);
        this.setTaxEndpoint(taxEndpoint);
    }
    
    @Override
    public PingResult ping(String account, String license, String client, String message)
    {
        setCredential(account, license, client);
        return getTaxService().ping(message);
    }

    /** @see org.mule.modules.avalara.api.AvalaraClient#sendToAvalara(org.mule.modules.avalara.TaxRequestType, java.lang.Object) */
    @Override
    public <T extends BaseResult> T sendToAvalara(String account, String license, String client, TaxRequestType entityType, Object obj)
    {
        T response;
        setCredential(account, license, client);
        try
        {
            response = (T) getTaxService().getClass().getMethod(entityType.getResourceName(), obj.getClass()).invoke(getTaxService(), obj);
        }
        catch (InvocationTargetException e)
        {
            throw new AvalaraRuntimeException(e.getCause().getMessage());
        }
        catch (Exception e)
        {
            throw new AssertionError(e);
        }
        if (!response.getResultCode().equals(SeverityLevel.SUCCESS))
        {
            throw new AvalaraRuntimeException(response.getMessages());
        }
        return response;
    }

    @Override
    public ValidateResult validateAddress(String account, String license, String client, ValidateRequest validateRequest)
    {
        setCredential(account, license, client);
        return getAddressService().validate(validateRequest);
    }
    
    public String getUsername()
    {
        return usernameLocal.get();
    }

    public String getPassword()
    {
        return passwordLocal.get();
    }

    public String getClient()
    {
        return clientLocal.get();
    }

    protected AddressSvcSoap getAddressService()
    {
        if (addressSvcSoap == null)
        {
            addressSvcSoap = createConnection(AddressSvcSoap.class, AddressSvc.class, "address", AddressSvc.AddressSvcSoap, getAddressEndpoint());
        }
        return addressSvcSoap;
    }

    protected TaxSvcSoap getTaxService()
    {
        if (taxSvcSoap == null)
        {
            taxSvcSoap = createConnection(TaxSvcSoap.class, TaxSvc.class, "tax", TaxSvc.TaxSvcSoap, getTaxEndpoint());
        }
        return taxSvcSoap;
    }
    
    protected <A> A createConnection(Class<A> portType, Class<? extends Service> serviceType, String schemaName, QName portName, String endpoint)
    {
        return ConnectionBuilder.fromPortType(portType)
            .withServiceType(serviceType)
            .withClasspathWsdl(schemaLocation(schemaName))
            .withCredential(new Credential()
            {
                @Override
                public String getUsername()
                {
                    return usernameLocal.get();
                }
                
                @Override
                public String getPassword()
                {
                    return passwordLocal.get();
                }
            })
            .withHeader(new AvalaraProfileHandler(new AvalaraProfile(){
                @Override
                public String getClient()
                {
                    return clientLocal.get();
                }
            }))
            .withPortQName(portName)
            .withEndpoint(endpoint)
            .build();
    }

    protected String schemaLocation(String schemaName)
    {
        return "schema/" + schemaName + "svc.wsdl";
    }
    
    protected void setCredential(String account, String license, String client)
    {
        usernameLocal.set(account);
        passwordLocal.set(license);
        clientLocal.set(client);
    }

    public String getAddressEndpoint() {
        return addressEndpoint;
    }

    public void setAddressEndpoint(String addressEndpoint) {
        this.addressEndpoint = addressEndpoint;
    }

    public String getTaxEndpoint() {
        return taxEndpoint;
    }

    public void setTaxEndpoint(String taxEndpoint) {
        this.taxEndpoint = taxEndpoint;
    }

}
