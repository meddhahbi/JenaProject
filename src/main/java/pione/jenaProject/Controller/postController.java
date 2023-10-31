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
@RequestMapping("/blogs")
@CrossOrigin("*")
public class postController {



    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?Post ?blogId ?blogTitle ?blogDescription  ?user_id ?username ?comment_id ?comment\n" +
                "WHERE {\n" +
                "   ?Post a :Post  .\n" +
                "   ?Post :blogId ?blogId .\n" +
                "   ?Post :blogTitle ?blogTitle .\n" +
                "   ?Post :blogDescription ?blogDescription .\n" +

                "   ?Post :postulerArticle ?user_id .   \n" +
                "   ?user_id a :User .\n" +
                "   ?user_id :username ?username .\n" +

                "   ?Post :evaluer ?comment_id .\n" +
                "   ?comment_id a :Commentaire .\n" +
                "   ?comment_id :comment ?comment .\n" +
                "}";



        String serviceEndpoint = "http://localhost:3030/test/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode Post = solution.get("Post");
                RDFNode blogId = solution.get("blogId");
                RDFNode blogTitle = solution.get("blogTitle");
                RDFNode blogDescription = solution.get("blogDescription");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode comment_id = solution.get("comment_id");
                RDFNode comment = solution.get("comment");





                    Map<String, Object> resultItem = new HashMap<>();
                    resultItem.put("Post", Post.toString());
                    resultItem.put("blogId", blogId.toString());
                    resultItem.put("blogTitle", blogTitle.toString());
                    resultItem.put("blogDescription", blogDescription.toString());
                    resultItem.put("user_id", user_id.toString());
                    resultItem.put("username", username.toString());
                    resultItem.put("comment_id", comment_id.toString());
                    resultItem.put("comment", comment.toString());
                    queryResults.add(resultItem);

            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/{filterValue}")
    public ResponseEntity<Object> performDynamicFilteredQuery(@PathVariable String filterValue) {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?Post ?blogId ?blogTitle ?blogDescription ?user_id ?username ?comment_id ?comment\n" +
                "WHERE {\n" +
                "   ?Post a :Post .\n" +
                "   ?Post :blogId ?blogId .\n" +
                "   ?Post :blogTitle ?blogTitle .\n" +
                "   ?Post :blogDescription ?blogDescription .\n" +
                "   ?Post :postulerArticle ?user_id .\n" +
                "   ?user_id a :User .\n" +
                "   ?user_id :username ?username .\n" +
                "   ?Post :evaluer ?comment_id .\n" +
                "   ?comment_id a :Commentaire .\n" +
                "   ?comment_id :comment ?comment .\n" +
                "   FILTER (regex(?blogTitle, \"" + filterValue + "\", \"i\"))\n" +
                "}";


    String serviceEndpoint = "http://localhost:3030/test/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode Post = solution.get("Post");
                RDFNode blogId = solution.get("blogId");
                RDFNode blogTitle = solution.get("blogTitle");
                RDFNode blogDescription = solution.get("blogDescription");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode comment_id = solution.get("comment_id");
                RDFNode comment = solution.get("comment");


                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("Post", Post.toString());
                resultItem.put("blogId", blogId.toString());
                resultItem.put("blogTitle", blogTitle.toString());
                resultItem.put("blogDescription", blogDescription.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                resultItem.put("comment_id", comment_id.toString());
                resultItem.put("comment", comment.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
