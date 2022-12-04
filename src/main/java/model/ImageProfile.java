package model;

import model.db.dbexceptions.DBException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public final class ImageProfile {

    ////////////////////////
    private ImageProfile(){}
    ////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String fromBlobToString(String nick, Blob blob, String prefix) throws DBException, SQLException {
        if(blob != null) {
            String imgPath = prefix + nick + ".png";
            byte[] bytes = blob.getBytes(1, (int) blob.length());

            File file = new File(imgPath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
                fileOutputStream.write(bytes);
                return imgPath;
            } catch (IOException ioException) {
                throw new DBException();
            }
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
