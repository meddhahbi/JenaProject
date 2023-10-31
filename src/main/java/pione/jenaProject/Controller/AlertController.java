package pione.jenaProject.Controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/alerts")
@CrossOrigin("*")
public class AlertController {


    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                " SELECT ?alert ?alertId ?alertDescription ?alertTitle\n" +
                "WHERE {\n" +
                "   ?alert a :Alert .\n" +
                "   ?alert :alertId ?alertId .\n" +
                "  ?alert :alertDescription ?alertDescription .\n" +
                "   ?alert :alertTitle ?alertTitle .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/mootez/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode alert = solution.get("alert");
                RDFNode alertId = solution.get("alertId");
                RDFNode alertDescription = solution.get("alertDescription");
                RDFNode alertTitle = solution.get("alertTitle");




                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("alert", alert.toString());
                resultItem.put("alertId", alertId.toString());
                resultItem.put("alertDescription", alertDescription.toString());
                resultItem.put("alertTitle", alertTitle.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
