package de.johner;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RecallService {

    @Inject
    @RestClient
    RecallsApiClient recallsApiClient;

    public String getRecallsAsCsv(String searchParams, String sort, Integer count, Integer limit, Integer skip) {
        Map<String, Object> apiResponse = recallsApiClient.getRecalls(searchParams, sort, count, limit, skip);
        List<Map<String, Object>> results = (List<Map<String, Object>>) apiResponse.get("results");

        StringWriter stringWriter = new StringWriter();

        if (results.size() > 0) {
            try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withDelimiter(';'))) {
                // Header
                List<String> headers = new ArrayList<>(results.get(0).keySet());
                headers.remove("openfda");
                csvPrinter.printRecord(headers);

                // Data
                for (Map<String, Object> result : results) {
                    List<String> row = new ArrayList<>();
                    for (String header : headers) {
                        Object value = result.get(header);
                        if (value instanceof List) {
                            row.add(String.join("|", (List<String>) value));
                        } else {
                            row.add(value != null ? value.toString() : "");
                        }
                    }
                    csvPrinter.printRecord(row);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error creating CSV", e);
            }
        }

        return stringWriter.toString();
    }
}
