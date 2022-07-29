package ic.doc.web;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class PDFResultPage implements Page {

  private final String query;
  private final String answer;

  public PDFResultPage(String query, String answer) {
    this.query = query;
    this.answer = answer;
  }

  @Override
  public void writeTo(HttpServletResponse resp) throws IOException {
    resp.setContentType("application/pdf");

    File tempFile = File.createTempFile("TemporaryDataFile", ".markdown");
    File pdfF = File.createTempFile("Result", ".pdf");

    FileWriter fileWriter = new FileWriter(tempFile);

    if (answer == null || answer.isEmpty()) {
      fileWriter.write("# Sorry\n");
      fileWriter.write("Sorry, we didn't understand\n" + query + "\n");
    } else {
      fileWriter.write("# " + query + "\n");
      fileWriter.write(answer + "\n");
    }
    fileWriter.close();

    try {
      new ProcessBuilder("pandoc", tempFile.getAbsolutePath(), "-s", "-o", pdfF.getAbsolutePath())
          .start()
          .waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    InputStream input = new FileInputStream(pdfF);
    input.transferTo(resp.getOutputStream());
  }
}
