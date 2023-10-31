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
@RequestMapping("/reviews")
@CrossOrigin("*")
public class ReviewController {

    @GetMapping
    public ResponseEntity<Object> performQuery() {

        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "  SELECT ?Review ?ratingId ?ratingComment ?user_id ?username ?category_id ?categoryName" +
                "  WHERE {\n" +
                "    ?Review a :Review .\n" +
                "    ?Review :ratingId ?ratingId .\n" +
                "    ?Review :ratingComment ?ratingComment .\n" +
                "    ?Review :rate ?user_id .\n" +
                "    ?user_id a :User .\n" +
                "    ?user_id :username ?username .\n" +
                "    ?Review :evaluer ?gig_id .\n" +
                "    ?gig_id a :Gig .\n" +
                "    ?gig_id :gigTitle ?gigTitle .\n" +
                "}";



        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();


            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode Review = solution.get("Review");
                RDFNode ratingId = solution.get("ratingId");
                RDFNode ratingComment = solution.get("ratingComment");
                RDFNode username = solution.get("username");
                RDFNode gigTitle = solution.get("gigTitle");


                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("Review", Review.toString());
                resultItem.put("ratingId", ratingId.toString());
                resultItem.put("ratingComment", ratingComment.toString());
                resultItem.put("username", username.toString());
                resultItem.put("gigTitle", gigTitle.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    }
