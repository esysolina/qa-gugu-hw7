package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadingAndCheckingFilesTest {

    @Test
    void parseFiles() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/zipexample.zip");

        ZipEntry pdfEntry = zipFile.getEntry("pdfexample.pdf");
        try (InputStream streamPdf = zipFile.getInputStream(pdfEntry)) {
            PDF parsedPdf = new PDF(streamPdf);
            assertThat(parsedPdf.text).contains("Chapter 1");
        }

        ZipEntry xlsxEntry = zipFile.getEntry("xlsxexample.xlsx");
        try (InputStream streamXlsx = zipFile.getInputStream(xlsxEntry)) {
            XLS parsedXls = new XLS(streamXlsx);
            assertThat(parsedXls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).isEqualTo("M.Gorky");
        }

        ZipEntry csvEntry = zipFile.getEntry("csvexample.csv");
        try (InputStream streamCsv = zipFile.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(streamCsv));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(3)
                    .contains(
                            new String[]{"E.M.Remarque", "Triumphal Arch"},
                            new String[]{"A.Kurpatov", "Red pill"},
                            new String[]{"G.G.Marques", "One hundred years of solitude"}
                    );
        }
    }
}
