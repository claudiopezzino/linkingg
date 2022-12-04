package control.controlutilities;

import java.io.*;

/* Not possible to return generic type "O" because of Sonar Warning during casting -> (O) in.readObject() */
public final class Copier {

    //////////////////
    private Copier(){}
    //////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    public static <O extends Serializable> Object deepCopy(O object) throws CopyException{
        if (object == null)
            return null;

        try ( ByteArrayOutputStream bout = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bout) ) {

            out.writeObject(object);
            out.flush();

            return clone(bout);

        }catch (IOException ioException){
            throw new CopyException();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
    private static Object clone(ByteArrayOutputStream bout) throws CopyException {
        Object clone;
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
             ObjectInputStream in = new ObjectInputStream(bin)) {

            clone = in.readObject();
        }catch (IOException | ClassNotFoundException e){
            throw new CopyException();
        }

        return clone;
    }
    //////////////////////////////////////////////////////////////////////////////////

}
