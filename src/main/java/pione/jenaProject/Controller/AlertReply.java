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
@RequestMapping("/reply")
@CrossOrigin("*")
public class AlertReply {
    @GetMapping
    public ResponseEntity<Object> performQuery() {



        String queryString = " PREFIX : <http://www.semanticweb.org/wassim/ontologies/2023/9/untitled-ontology-8#>\n" +
                " SELECT ?reply ?replyId ?replyDescription ?replyAlertId\n" +
                "WHERE {\n" +
                "   ?reply a :Reply .\n" +
                "   ?reply :replyId ?replyId .\n" +
                "  ?reply :replyDescription ?replyDescription .\n" +
                "   ?reply :replyAlertId ?replyAlertId .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/mootez/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode reply = solution.get("reply");
                RDFNode replyId = solution.get("replyId");
                RDFNode replyDescription = solution.get("replyDescription");
                RDFNode alertReplyId = solution.get("replyAlertId");




                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("reply", reply.toString());
                resultItem.put("replyId", replyId.toString());
                resultItem.put("replyDescription", replyDescription.toString());
                resultItem.put("replyAlertId", alertReplyId.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
