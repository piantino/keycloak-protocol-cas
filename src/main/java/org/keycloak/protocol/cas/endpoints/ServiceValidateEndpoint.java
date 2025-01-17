package org.keycloak.protocol.cas.endpoints;

import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.protocol.cas.representations.CASServiceResponse;
import org.keycloak.protocol.cas.utils.CASValidationException;
import org.keycloak.protocol.cas.utils.ContentTypeHelper;
import org.keycloak.protocol.cas.utils.ServiceResponseHelper;

import javax.ws.rs.core.*;
import java.util.Map;

public class ServiceValidateEndpoint extends ValidateEndpoint {
    @Context
    private Request restRequest;

    public ServiceValidateEndpoint(RealmModel realm, EventBuilder event) {
        super(realm, event);
    }

    @Override
    protected Response successResponse() {
        UserSessionModel userSession = clientSession.getUserSession();
        Map<String, Object> attributes = getUserAttributes();
        CASServiceResponse serviceResponse = ServiceResponseHelper.createSuccess(userSession.getUser().getUsername(), attributes);
        return prepare(Response.Status.OK, serviceResponse);
    }

    @Override
    protected Response errorResponse(CASValidationException e) {
        CASServiceResponse serviceResponse = ServiceResponseHelper.createFailure(e.getError(), e.getErrorDescription());
        return prepare(e.getStatus(), serviceResponse);
    }

    private Response prepare(Response.Status status, CASServiceResponse serviceResponse) {
        MediaType responseMediaType = new ContentTypeHelper(request, restRequest, session.getContext().getUri()).selectResponseType();
        return ServiceResponseHelper.createResponse(status, responseMediaType, serviceResponse);
    }
}
