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
     * ジェネリクス元を取得
     */
    private List<T> csvData;


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
     * @param columns セットする columns
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }


    /**
     * @return csvData
     */
    public List<T> getCsvData() {
        return csvData;
    }

    /**
     * @param csvData セットする csvData
     */
    public void setCsvData(List<T> csvData) {
        this.csvData = csvData;
    }


}
