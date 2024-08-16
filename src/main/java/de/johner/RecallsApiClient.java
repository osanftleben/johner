package de.johner;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@Path("/device/recall.json")
@RegisterRestClient(configKey="recalls-api")
public interface RecallsApiClient {
    @GET
    Map<String, Object> getRecalls(@QueryParam("search") String search, @QueryParam("sort") String sort, @QueryParam("count") Integer count, @QueryParam("limit") Integer limit, @QueryParam("skip") Integer skip);
}
