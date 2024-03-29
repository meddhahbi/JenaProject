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
@RequestMapping("/gigs")
@CrossOrigin("*")
public class GigController {

    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "  SELECT ?Gig ?gigId ?gigDescription ?gigTitle ?orders ?price ?user_id ?username ?category_id ?categoryName" +
                "  WHERE {\n" +
                "    ?Gig a :Gig .\n" +
                "    ?Gig :gigId ?gigId .\n" +
                "    ?Gig :gigDescription ?gigDescription .\n" +
                "    ?Gig :gigTitle ?gigTitle .\n" +
                "    ?Gig :orders ?orders .\n" +
                "    ?Gig :price ?price .\n" +
                "    ?Gig :publier ?user_id .\n" +
                "    ?user_id a :User .\n" +
                "    ?user_id :username ?username .\n" +
                "    ?Gig :categoriser ?category_id .\n" +
                "    ?category_id a :Category .\n" +
                "    ?category_id :categoryName ?categoryName .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode gig = solution.get("Gig");
                RDFNode gigId = solution.get("gigId");
                RDFNode gigDescription = solution.get("gigDescription");
                RDFNode gigTitle = solution.get("gigTitle");
                RDFNode orders = solution.get("orders");
                RDFNode price = solution.get("price");
                RDFNode username = solution.get("username");
                RDFNode categoryName = solution.get("categoryName");




                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("gig", gig.toString());
                resultItem.put("gigId", gigId.toString());
                resultItem.put("gigDescription", gigDescription.toString());
                resultItem.put("gigTitle", gigTitle.toString());
                resultItem.put("orders", orders.toString());
                resultItem.put("price", price.toString());
                resultItem.put("username", username.toString());
                resultItem.put("categoryName", categoryName.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }


}
