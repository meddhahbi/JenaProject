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
@RequestMapping("/projects")
@CrossOrigin("*")
public class ProjectController {
    @GetMapping("/filter")
    public ResponseEntity<Object> performFilteredQuery(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String technology
    ) {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?project ?projectId ?projectTitle ?progress ?category_id ?budget ?company ?technology ?user_id ?username ?category_id ?categoryName\n" +
                "WHERE {\n" +
                "   ?project a :Project .\n" +
                "   ?project :projectId ?projectId .\n" +
                "   ?project :projectTitle ?projectTitle .\n" +
                "   ?project :progress ?progress .\n" +
                "   ?project :category_id ?category_id .\n" +
                "   ?project :budget ?budget .\n" +
                "   ?project :company ?company .\n" +
                "   ?project :technology ?technology .\n"+
                "  ?project :travailler_sur ?user_id .\n" +
                "  ?user_id a :User .\n" +
                "  ?user_id :username ?username .\n" +
                "  ?category a :Category .\n" +
                "  ?project :filtrer ?category .\n" +
                "  ?category :categoryName ?categoryName .\n";



        if (company != null) {
            queryString += "   FILTER regex(str(?company), '" + company + "', 'i') .\n";
        }

        if (technology != null) {
            queryString += "   FILTER regex(str(?technology), '" + technology + "', 'i') .\n";
        }

        queryString += "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";
        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode project = solution.get("project");
                RDFNode projectTitle = solution.get("projectTitle");
                RDFNode projectId = solution.get("projectId");
                RDFNode progress = solution.get("progress");
                RDFNode budget = solution.get("budget");
                RDFNode category_id = solution.get("category_id");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode category_name = solution.get("categoryName");


                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("project", project.toString());
                resultItem.put("projectId", projectId.toString());
                resultItem.put("projectTitle", projectTitle.toString());
                resultItem.put("category_id", category_id.toString());
                resultItem.put("progress", progress.toString());
                resultItem.put("price", budget.toString());
                resultItem.put("company", company.toString());
                resultItem.put("technology", technology.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                resultItem.put("categoryName", category_name.toString());


                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<Object> performQuery() {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?project ?projectId ?projectTitle ?progress ?category_id ?budget ?company ?technology ?user_id ?username ?category_id ?categoryName\n" +
                "WHERE {\n" +
                "  ?project a :Project .\n" +
                "  ?project :projectId ?projectId .\n" +
                "  ?project :projectTitle ?projectTitle .\n" +
                "  ?project :progress ?progress .\n" +
                "  ?project :category_id ?category_id .\n" +
                "  ?project :budget ?budget .\n" +
                "  ?project :company ?company .\n" +
                "  ?project :technology ?technology .\n" +
                "  ?project :travailler_sur ?user_id .\n" +
                "  ?user_id a :User .\n" +
                "  ?user_id :username ?username .\n" +
                "  ?category a :Category .\n" +
                "  ?project :filtrer ?category .\n" +
                "  ?category :categoryName ?categoryName .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode project = solution.get("project");
                RDFNode projectId = solution.get("projectId");
                RDFNode projectTitle = solution.get("projectTitle");
                RDFNode progress = solution.get("progress");
                RDFNode category_id = solution.get("category_id");
                RDFNode budget = solution.get("budget");
                RDFNode company = solution.get("company");
                RDFNode technology = solution.get("technology");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode category_name = solution.get("categoryName");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("project", project.toString());
                resultItem.put("projectId", projectId.toString());
                resultItem.put("projectTitle", projectTitle.toString());
                resultItem.put("category_id", category_id.toString());
                resultItem.put("progress", progress.toString());
                resultItem.put("budget", budget.toString());
                resultItem.put("company", company.toString());
                resultItem.put("technology", technology.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                resultItem.put("categoryName", category_name.toString());






                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> performQuery(@PathVariable String categoryId) {

        // Modify the SPARQL query to filter by categoryId
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?project ?projectId ?projectTitle ?progress ?category_id ?budget ?company ?technology ?user_id ?username ?category_id ?categoryName\n" +
                "WHERE {\n" +
                "   ?project a :Project .\n" +
                "   ?project :projectId ?projectId .\n" +
                "   ?project :projectTitle ?projectTitle .\n" +
                "   ?project :progress ?progress .\n" +
                "   ?project :category_id ?category_id .\n" +
                "   ?project :budget ?budget .\n" +
                "   ?project :company ?company .\n" +
                "   ?project :technology ?technology .\n" +
                "  ?project :travailler_sur ?user_id .\n" +
                "  ?user_id a :User .\n" +
                "  ?user_id :username ?username .\n" +
                "  ?category a :Category .\n" +
                "  ?project :filtrer ?category .\n" +
                "  ?category :categoryName ?categoryName .\n" +
                "   FILTER(?category_id = \"" + categoryId + "\")\n" + // Add the filter
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode project = solution.get("project");
                RDFNode projectTitle = solution.get("projectTitle");
                RDFNode projectId = solution.get("projectId");
                RDFNode progress = solution.get("progress");
                RDFNode budget = solution.get("budget");
                RDFNode category_id = solution.get("category_id");
                RDFNode company = solution.get("company");
                RDFNode technology = solution.get("technology");
                RDFNode user_id = solution.get("user_id");
                RDFNode username = solution.get("username");
                RDFNode category_name = solution.get("categoryName");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("project", project.toString());
                resultItem.put("projectId", projectId.toString());
                resultItem.put("projectTitle", projectTitle.toString());
                resultItem.put("category_id", category_id.toString());
                resultItem.put("progress", progress.toString());
                resultItem.put("budget", budget.toString());
                resultItem.put("company", company.toString());
                resultItem.put("technology", technology.toString());
                resultItem.put("user_id", user_id.toString());
                resultItem.put("username", username.toString());
                resultItem.put("categoryName", category_name.toString());


                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
