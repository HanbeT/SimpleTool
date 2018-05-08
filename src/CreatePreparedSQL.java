import java.util.Scanner;

public class CreatePreparedSQL {

    /** バインド変数 */
    private final String BIND_VARIABLE = "\\?";
    private final String BIND_VARIABLE_COUNT = "?";

    /** カンマ */
    private final String COMMA = ",";

    public static void main(String[] args) {
        // インスタンス
        CreatePreparedSQL ins = new CreatePreparedSQL();
        //
        String result = null;
        // SQL
        String sql = null;
        // バインド変数の数
        int bindCount = 0;
        // パラメータ
        String tmpParams = null;
        String[] params = null;
        // パラメータ数
        int paramCount = 0;

        // SQL取得
        sql = ins.standardInput("SQLを入力してください。");
        if (ins.isEmpty(sql)) {
            System.out.println("SQLが未入力です。");
            System.exit(1);
        }

        // パラメータ取得
        tmpParams = ins.standardInput("パラメータを入力してください。");
        if (ins.isEmpty(tmpParams)) {
            System.out.println("パラメータが未入力です。");
            System.exit(1);
        }

        // 入力値をカンマ区切りに変換する。
        params = tmpParams.split(ins.COMMA);

        // パラメータ数分、置換処理を実行する。
        result = sql;
        for (String param : params) {
            result = result.replaceFirst(ins.BIND_VARIABLE, param.trim());
        }

        // 置換結果を出力
        System.out.println("");
        System.out.println("置換結果：");
        System.out.println(result);
        System.exit(0);
    }

    /**
     * 標準入力取得メソッド
     * @param promptWord プロンプト
     * @return 入力文字列
     */
    private String standardInput(String prompt) {
        String result = null;
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt + " > ");
        result = scanner.nextLine();
        return result;
    }

    /**
     * 空文字チェックメソッド
     * @param param チェック対象文字列
     * @return チェック結果
     */
    private boolean isEmpty(String param) {
        return param == null || "".equals(param);
    }

}
