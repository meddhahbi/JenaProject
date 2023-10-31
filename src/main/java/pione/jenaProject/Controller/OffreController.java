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
@RequestMapping("/offres")
@CrossOrigin("*")
public class OffreController {
    @GetMapping
    public ResponseEntity<Object> performQuery() {
        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                " SELECT ?offre ?offreTitle ?offreDescription\n" +
                "WHERE {\n" +
                "   ?Offre a :offre .\n" +
                "   ?Offre :offreId ?offreId .\n" +
                "  ?Offre :offreTitle ?offreTitle .\n" +
                "  ?Offre :offreDescription ?offreDescription .\n" +
                "    ?Offre :user ?user_id .\n" +
                "    ?user_id a :User .\n" +
                "    ?user_id :username ?username .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode offre = solution.get("offre");
                RDFNode offreId = solution.get("offreId");
                RDFNode offreDescription = solution.get("offreDescription");
                RDFNode offreTitle = solution.get("offreTitle");



                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("offre", offre.toString());
                resultItem.put("offreId", offreId.toString());
                resultItem.put("offreDescription", offreDescription.toString());
                resultItem.put("offreTitle", offreTitle.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
