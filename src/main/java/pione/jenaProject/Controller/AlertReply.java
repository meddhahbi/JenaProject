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
                " SELECT ?reply ?replyId ?ReplyDescription ?alertTitle ?replyAlertId  \n" +
                "WHERE {\n" +
                "   ?reply a :Reply .\n" +
                "   ?reply :replyId ?replyId .\n" +
                "  ?reply :ReplyDescription ?ReplyDescription .\n" +
                "    ?reply :traiter ?replyAlertId .\n" +
                "    ?replyAlertId a :Alert .\n" +
                "    ?replyAlertId :alertTitle ?alertTitle .\n" +


                "}";




        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();



            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode reply = solution.get("reply");
                RDFNode replyId = solution.get("replyId");
                RDFNode ReplyDescription = solution.get("ReplyDescription");

                RDFNode alertTitle = solution.get("alertTitle");





                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("reply", reply.toString());
                resultItem.put("replyId", replyId.toString());
                resultItem.put("ReplyDescription", ReplyDescription.toString());

                resultItem.put("alertTitle", alertTitle.toString());
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
