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
@RequestMapping("/Reponse")
@CrossOrigin("*")
public class ReponseController {

    @GetMapping
    public ResponseEntity<Object> performSearch(@RequestParam("searchTerm") String searchTerm) {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?reponseId ?reponse ?user_id ?username ?condidature_id ?email ?lastName ?firstName\n" +
                "WHERE {\n" +
                "   ?Reponse :reponseId ?reponseId .\n" +
                "   ?Reponse :reponse ?reponse .\n" +
                "   ?Reponse :responseUser ?user_id .\n" +
                "   ?user_id :username ?username .\n" +
                "   ?Reponse :repondre ?condidature_id .\n" +
                "   ?condidature_id :email ?email .\n" +
                "   ?condidature_id :lastName ?lastName .\n" +
                "   ?condidature_id :firstName ?firstName .\n" +
                "   FILTER (regex(str(?reponse), '" + searchTerm + "', 'i') || " +
                "          regex(str(?email), '" + searchTerm + "', 'i') || " +
                "          regex(str(?lastName), '" + searchTerm + "', 'i') || " +
                "          regex(str(?firstName), '" + searchTerm + "', 'i') || " +
                "          regex(str(?username), '" + searchTerm + "', 'i')) .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/test/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode reponseId = solution.get("reponseId");
                RDFNode reponse = solution.get("reponse");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode condidature_id = solution.get("condidature_id");
                RDFNode email = solution.get("email");
                RDFNode lastName = solution.get("lastName");
                RDFNode firstName = solution.get("firstName");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("reponseId", reponseId.toString());
                resultItem.put("reponse", reponse.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                resultItem.put("condidature_id", condidature_id.toString());
                resultItem.put("email", email.toString());
                resultItem.put("lastName", lastName.toString());
                resultItem.put("firstName", firstName.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
