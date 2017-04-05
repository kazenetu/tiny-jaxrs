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
     * 必要なパラメータを設定
     * @param columns 出力対象カラムのgetメソッドのリスト
     * @param csvData 出力レコードデータ
     */
    public CsvEntity(List<String> columns,List<T> csvData){
        this.columns = columns;
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
     * @return csvData
     */
    public List<T> getCsvData() {
        return csvData;
    }


}
