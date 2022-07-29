package ic.doc;

import ic.doc.web.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServer {

  public WebServer() throws Exception {
    Server server = new Server(Integer.parseInt(System.getenv("PORT")));

    ServletHandler handler = new ServletHandler();
    handler.addServletWithMapping(new ServletHolder(new Website()), "/*");
    server.setHandler(handler);

    server.start();
  }

  public static void main(String[] args) throws Exception {
    new WebServer();
  }

  static class Website extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      String query = req.getParameter("q");
      if (query == null) {
        new IndexPage().writeTo(resp);
      } else {
        String output = req.getParameter("output");
        Page page;
        switch (output) {
          case "HTML":
            page = new HTMLResultPage(query, new QueryProcessor().process(query));
            break;
          case "markdown":
            page = new MarkdownResultPage(query, new QueryProcessor().process(query));
            break;
          case "PDF":
            page = new PDFResultPage(query, new QueryProcessor().process(query));
            break;
          default:
            page = new HTMLResultPage(null, null);
            break;
        }
        page.writeTo(resp);
      }
    }
  }
}
