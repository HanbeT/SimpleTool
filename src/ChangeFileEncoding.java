import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeFileEncoding {

    /** 文字コード(UTF8) */
    private static final String ENCODING_UTF8 = "UTF-8";
    /** 文字コード(SJIS) */
    private static final String ENCODING_SJIS = "Shift_JIS";
    /** 終了コード(正常終了) */
    private static final int ENDCODE_OK = 0;
    /** 終了コード(異常終了) */
    private static final int ENDCODE_ERR = 99;

    public static void main(String[] args) {
        // パラメータチェック
        if (args.length != 2) {
            System.exit(ENDCODE_ERR);
        }

        // インスタンス
        ChangeFileEncoding ins = new ChangeFileEncoding();

        // 変換元フォルダパス
        String srcPath = args[0];

        // 変換元フォルダ存在チェック
        if (!ins.isExistFile(srcPath)) {
            System.exit(ENDCODE_ERR);
        }

        // 変換文字コード
        String encoding = args[1];

        // 対象ファイル一覧
        List<File> fileList = new ArrayList<>();
        ins.getFileList(srcPath, fileList);

        try {
            ins.changeEncoding(fileList, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(ENDCODE_ERR);
        }
        System.out.println("処理が完了しました。");
        System.exit(ENDCODE_OK);
    }

    /**
     * ファイルの文字コードを変換して、出力する。
     * @param fileList 対象ファイル一覧
     * @param encoding 変換文字コード
     * @throws Exception
     */
    private void changeEncoding(List<File> fileList, String encoding)
            throws Exception {

        // ファイル内容
        byte[] contents = null;
        String outputStr = null;
        String fileEncode = null;

        // 出力ファイル
        PrintWriter pw = null;

        try {
            for (File file : fileList) {
                // ファイル内容を読み込む。
                contents = Files.readAllBytes(file.toPath());
                // 読み込んだファイルの文字コードを判定する。
                fileEncode = judgeEncoding(contents);
                // 読み込んだファイル内容を文字コードを指定して、変換する。
                outputStr = new String(contents, fileEncode);
                // ファイル出力
                pw = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(file), encoding)));
                pw.print(outputStr);
                pw.close();
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * 対象フォルダ配下のファイル一覧を取得する。
     * @param targetPath
     * @param fileList
     */
    private void getFileList(String targetPath, List<File> fileList) {
        // 対象ディレクトリ
        File dir = new File(targetPath);
        // ディレクトリパス一覧
        List<String> dirList = new ArrayList<>();
        // ファイル一覧を取得する。
        for (File tmp : dir.listFiles()) {
            if (tmp.isFile()) {
                fileList.add(tmp);
            }
            if (tmp.isDirectory()) {
                dirList.add(tmp.getPath());
            }
        }
        for (String tmpPath : dirList) {
            getFileList(tmpPath, fileList);
        }
        dirList.clear();
    }

    private String judgeEncoding(byte[] src) throws Exception {
        String encoding = null;

        if (judgeEncoding(src, ENCODING_SJIS)) {
            encoding = ENCODING_SJIS;
        } else if (judgeEncoding(src, ENCODING_UTF8)) {
            encoding = ENCODING_UTF8;
        } else {
            throw new Exception();
        }
        return encoding;
    }

    private boolean judgeEncoding(byte[] src, String encoding) throws UnsupportedEncodingException {
        byte[] tmp = new String(src, encoding).getBytes(encoding);
        return Arrays.equals(tmp, src);
    }

    /**
     * 対象のファイル、フォルダが存在するかをチェックする。
     * @param targetPath 対象パス
     * @return チェック結果(true:存在する/false:存在しない)
     */
    private boolean isExistFile(String targetPath) {
        return new File(targetPath).exists();
    }


}
