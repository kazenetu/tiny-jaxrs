package web.common.base;

import java.util.List;

/**
 * CSV出力用エンティティクラスのジェネリクスクラス
 */
public class CsvEntity<T> {

    /**
     * 出力カラム名リスト
     */
    private List<String> columns;

    /**
     * 出力ヘッダカラム名リスト
     */
    private List<String> columnHeaders;

    /**
     * ジェネリクス元を取得
     */
    private List<T> csvData;

    /**
     * 必要なパラメータを設定
     * @param columns 出力対象カラムのgetメソッドのリスト
     * @param csvData 出力レコードデータ
     */
    public CsvEntity(List<String> columns,List<String> columnHeaders,List<T> csvData){
        this.columns = columns;
        this.columnHeaders = columnHeaders;
        this.csvData = csvData;
    }

    /**
     * 必要なパラメータを設定
     * @param columns 出力対象カラムのgetメソッドのリスト
     * @param csvData 出力レコードデータ
     */
    public CsvEntity(List<String> columns,List<T> csvData){
        this.columns = columns;
        this.columnHeaders = null;
        this.csvData = csvData;
    }

    /**
     * 出力カラム名が設定されているか否か
     * @return
     */
    public boolean isExitstColumns(){
        return (this.columns !=null && !this.columns.isEmpty());
    }

    /**
     * @return columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * 出力ヘッダカラム名が設定されているか否か
     * @return
     */
    public boolean isExitstColumnHeaders(){
        return (this.columnHeaders !=null && !this.columnHeaders.isEmpty());
    }

    /**
     * @return columnHeaders
     */
    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    /**
     * @return csvData
     */
    public List<T> getCsvData() {
        return csvData;
    }

}
