package web.common.util;

import java.net.URL;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;

public class PdfUtil implements AutoCloseable{

    private JasperReport report = null;

    @Override
    public void close() {
        if(report != null){
            report = null;
        }
    }

    /**
     * PDFデータの取得
     *
     * @param reportName レポート名
     * @param params パラメータ
     * @param dataSource データソース
     * @return PDFデータ
     * @throws JRException 実行時の例外エラー
     */
    public byte[] getPdfBytes(String reportName, Map<String, Object> params, JRDataSource dataSource)
            throws JRException {
        String filePath = getResourcePath(reportName + ".jasper");

        if (filePath != null) {
            return JasperRunManager.runReportToPdf(filePath, params, dataSource);
        }

        filePath = getResourcePath(reportName + ".jrxml");
        report = JasperCompileManager.compileReport(filePath);
        return JasperRunManager.runReportToPdf(report, params, dataSource);
    }

    /**
     * リソースのパスを取得する
     *
     * @param fileName リソースファイル名
     * @return リソースファイルのパス
     */
    private String getResourcePath(String fileName) {
        URL fileUrl = this.getClass().getClassLoader().getResource(fileName);
        if (fileUrl == null) {
            return null;
        }
        return fileUrl.getPath();
    }
}
