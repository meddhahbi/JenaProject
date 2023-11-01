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
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {

    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                " SELECT ?category ?categoryId ?categoryDescription ?categoryName\n" +
                "WHERE {\n" +
                "   ?category a :Category .\n" +
                "   ?category :categoryId ?categoryId .\n" +
                "  ?category :categoryDescription ?categoryDescription .\n" +
                "   ?category :categoryName ?categoryName .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode category = solution.get("category");
                RDFNode categoryId = solution.get("categoryId");
                RDFNode categoryDescription = solution.get("categoryDescription");
                RDFNode categoryName = solution.get("categoryName");




                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("category", category.toString());
                resultItem.put("categoryId", categoryId.toString());
                resultItem.put("categoryDescription", categoryDescription.toString());
                resultItem.put("categoryName", categoryName.toString());
                queryResults.add(resultItem);
            }   

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/withSubCategories")
    public ResponseEntity<Object> performQuery2() {
        String queryString = "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX myOntology: <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                "SELECT ?categoryName ?categoryId (GROUP_CONCAT(DISTINCT ?typeName; SEPARATOR=', ') AS ?types)\n" +
                "WHERE {\n" +
                "  ?category rdf:type :Category .\n" +
                "  ?category :categoryName ?categoryName .\n" +
                "  ?category :categoryId ?categoryId .\n" +
                "  ?category rdf:type ?type .\n" +
                "  BIND(strafter(str(?type), str(myOntology:)) AS ?typeName)\n" +
                "}\n" +
                "GROUP BY ?categoryName ?categoryId";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";
        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode categoryName = solution.get("categoryName");
                RDFNode categoryId = solution.get("categoryId");
                RDFNode types = solution.get("types");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("categoryName", categoryName.toString());
                resultItem.put("categoryId", categoryId.toString());
                resultItem.put("types", types.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
