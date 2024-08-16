package de.johner;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/recalls")
@Tag(name = "Recall Resource", description = "Endpoints for Medical Device Recalls")
public class RecallResource {

    @Inject
    RecallService recallService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
            summary = "Retrieve Medical Device Recalls",
            description = "Retrieves Medical Device Recalls based on the provided search parameters and returns them as a CSV file."
    )
    @APIResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = String.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid request parameters"
    )
    @APIResponse(
            responseCode = "500",
            description = "Internal server error"
    )
    public Response getRecalls(
            @Parameter(
                    description = "Search parameters for querying recalls. Multiple parameters can be combined using AND or OR. If you don’t specify a field to search, the API will search in every field.",
                    example = "root_cause_description.exact:\"Device Design\"",
                    required = false
            )
            @QueryParam("search") String searchParams,

            @Parameter(
                    description = "Sort the results of the search by the specified field in ascending or descending order by using the :asc or :desc modifier.",
                    example = ":asc"
            )
            @QueryParam("sort") String sort,

            @Parameter(
                    description = "Count the number of unique values of a certain field, for all the records that matched the search parameter. By default, the API returns the 1000 most frequent values."
            )
            @QueryParam("count") Integer count,

            @Parameter(
                    description = "Maximum number of results to return (maximum: 1000)",
                    example = "5"
            )
            @QueryParam("limit") int limit,

            @Parameter(
                    description = "Skip this number of records that match the search parameter, then return the matching records that follow. Use in combination with limit to paginate results. Currently, the largest allowed value for the skip parameter is 25000.",
                    example = "50"
            )
            @QueryParam("skip") int skip
            ) {
        String csv = recallService.getRecallsAsCsv(searchParams, sort, count, limit, skip);
        return Response.ok(csv)
                .header("Content-Disposition", "attachment; filename=recalls.csv")
                .build();
    }
}
