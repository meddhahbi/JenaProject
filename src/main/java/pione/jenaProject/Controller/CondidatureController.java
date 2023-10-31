package pione.jenaProject.Controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conadidature")
@CrossOrigin("*")
public class CondidatureController {

    @GetMapping
    public ResponseEntity<Object> performSearch(@RequestParam("searchTerm") String searchTerm) {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?condidatureId ?email ?lastName ?firstName ?lettre ?offre_id ?offreDescription\n" +
                "WHERE {\n" +
                "   ?Condidature :condidatureId ?condidatureId .\n" +
                "   ?Condidature :email ?email .\n" +
                "   ?Condidature :lastName ?lastName .\n" +
                "   ?Condidature :firstName ?firstName .\n" +
                "   ?Condidature :lettre ?lettre .\n" +
                "   ?Condidature :postuler ?offre_id .\n" +
                "   ?offre_id a :Offre .\n" +
                "   ?offre_id :offreDescription ?offreDescription .\n" +
                "   FILTER (regex(str(?email), '" + searchTerm + "', 'i') || " +
                "          regex(str(?lastName), '" + searchTerm + "', 'i') || " +
                "          regex(str(?firstName), '" + searchTerm + "', 'i') || " +
                "          regex(str(?lettre), '" + searchTerm + "', 'i') || " +
                "          regex(str(?offreDescription), '" + searchTerm + "', 'i')) .\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/test/sparql";
        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode condidatureId = solution.get("condidatureId");
                RDFNode email = solution.get("email");
                RDFNode lastName = solution.get("lastName");
                RDFNode firstName = solution.get("firstName");
                RDFNode lettre = solution.get("lettre");
                RDFNode offre_id = solution.get("offre_id");
                RDFNode offreDescription = solution.get("offreDescription");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("condidatureId", condidatureId.toString());
                resultItem.put("email", email.toString());
                resultItem.put("lastName", lastName.toString());
                resultItem.put("firstName", firstName.toString());
                resultItem.put("lettre", lettre.toString());
                resultItem.put("offre_id", offre_id.toString());
                resultItem.put("offreDescription", offreDescription.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
