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
@RequestMapping("/skills")
@CrossOrigin("*")
public class SkillController {
    @GetMapping
    public ResponseEntity<Object> performQuery() {
        String queryString =
//                " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
//                " SELECT ?skills ?skillName ?skillDescription\n" +
//                "WHERE {\n" +
//                "   ?Skills a :skills .\n" +
//                "   ?Skills :skillId ?skillId .\n" +
//                "  ?Skills :skillName ?skillName .\n" +
//                "  ?Skills :skillDescription ?skillDescription .\n" +
////                "    ?Skills :rate ?user_id .\n" +
////                "    ?user_id a :User .\n" +
////                "    ?user_id :username ?username .\n" +
//                "}";
        "PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n"+
        "SELECT ?skill ?skillId ?skillName ?username\n"+
        "WHERE { \n"+
                "?skill a :Skills .\n"+
                    "?skill :skillId ?skillId .\n"+
                    "?skill :skillName ?skillName .\n"+
                "?skill :maitriser ?user_id .\n" +
                " ?user_id a :User .\n" +
                " ?user_id :username ?username"+
//            ";\n"+
        "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode skill = solution.get("skill");
                RDFNode skillId = solution.get("skillId");
                RDFNode skillName = solution.get("skillName");
                RDFNode username = solution.get("username");
                System.out.println(username.toString());



                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("skill", skill.toString());
                resultItem.put("skillId", skillId.toString());
                resultItem.put("skillName", skillName.toString());
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
