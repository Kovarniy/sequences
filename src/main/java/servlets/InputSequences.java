package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import logic.Sequence.Expression.OperatorType;
import logic.Sequence.Sequence;

import static logic.queries.GetQuery.newSeqQuery;
import static logic.queries.GetQuery.testRuleQuery;
import static logic.queries.GetQuery.useRuleQuery;

public class InputSequences extends HttpServlet {

    Sequence currentSeq;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getRequestDispatcher("/sss.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        String queryType = req.getParameter("queryType").trim();
        System.out.println(queryType);

        switch (queryType){
            case "newSequnces":
                /*TODO проверка на null*/
                currentSeq = newSeqQuery(req, resp);
                break;
            case "testRules":
                testRuleQuery(req, resp);
                break;
            case "useRule":
                useRuleQuery(req, resp, currentSeq);
                break;
        }

    }

}
