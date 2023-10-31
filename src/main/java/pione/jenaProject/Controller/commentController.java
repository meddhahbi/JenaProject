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
@RequestMapping("/comments")
@CrossOrigin("*")
public class commentController {

    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?commentaire ?commentId ?comment ?user_id ?username\n" +
                "WHERE {\n" +
                "    ?commentaire a :Commentaire .\n" +
                "    ?commentaire :commentId ?commentId .\n" +
                "    ?commentaire :comment ?comment .\n" +
                "    ?commentaire :ajouter ?user_id .\n" +
                "    ?user_id a :User .\n" +
                "    ?user_id :username ?username .\n" +
                "}";



        String serviceEndpoint = "http://localhost:3030/test/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode commentaire = solution.get("commentaire");
                RDFNode commentId = solution.get("commentId");
                RDFNode comment = solution.get("comment");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");




                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("commentaire", commentaire.toString());
                resultItem.put("commentId", commentId.toString());
                resultItem.put("comment", comment.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
