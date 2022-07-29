package ic.doc.web;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MarkdownResultPage implements Page {

  private final String query;
  private final String answer;

  public MarkdownResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  @Override
  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("text/markdown");

    File tempFile = File.createTempFile("TemporaryDataFile", ".tmp");
    FileWriter fileWriter = new FileWriter(tempFile);

    if (answer == null || answer.isEmpty()) {
      fileWriter.write("# Sorry\n");
      fileWriter.write("Sorry, we didn't understand " + query + "\n");
    } else {
      fileWriter.write("# " + query + "\n");
      fileWriter.write(answer + "\n");
    }

    fileWriter.close();

    InputStream input = new FileInputStream(tempFile);
    input.transferTo(resp.getOutputStream());
  }
}
