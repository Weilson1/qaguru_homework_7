package ru.mail.galyavievai;

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

public class ZipArchiveTest {
    private ClassLoader cl = ZipArchiveTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src\\test\\resources\\files\\archivetest.zip");
        //Проверяем PDF
        ZipEntry pdfEntry = zipFile.getEntry("PDFdoc.pdf");
        try (InputStream inputPdf = zipFile.getInputStream(pdfEntry)) {
            PDF parsedPdf = new PDF(inputPdf);
            assertThat(parsedPdf.text).contains("Документ Word преобразованный в PDF.");
        }
        // Проверяем CSV
        ZipEntry csvEntry = zipFile.getEntry("Spisok_knig.csv");
        try (InputStream inputCsv = zipFile.getInputStream(csvEntry)) {
            CSVReader readCsv = new CSVReader(new InputStreamReader(inputCsv));
            List<String[]> list = readCsv.readAll();
            assertThat(list)
                    .hasSize(6)
                    .contains(new String[]{"Книга", "Автор"},
                            new String[]{"«Властелин колец»", "Джон Р.М. Толкин"},
                            new String[]{"«Гордость и предубеждение»", "Джейн Остин"},
                            new String[]{"«Тёмные начала»", "Филип Пулман"},
                            new String[]{"«Автостопом по галактике»", "Дуглас Адамс"},
                            new String[]{"«Гарри Поттер и Кубок огня»", "Джоан Роулинг"});
        }
        //Проверяем XLSX
        ZipEntry xlsxEntry = zipFile.getEntry("Spisok_lydei.xlsx");
        try (InputStream inputXlsx = zipFile.getInputStream(xlsxEntry)) {
            XLS parcedXlsx = new XLS(inputXlsx);
            assertThat(parcedXlsx.excel.getSheetAt(0).getRow(5).getCell(0).getStringCellValue())
                    .isEqualTo("Василиса");
        }
    }
}

