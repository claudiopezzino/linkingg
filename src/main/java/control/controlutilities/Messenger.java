package control.controlutilities;

import model.Device;
import model.Filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public final class Messenger {

    /////////////////////
    private Messenger(){}
    /////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static <E> void sendMessage(Device device, List<E> list, Filter filter) throws MessengerException{
        try(Socket socket = new Socket(device.ipAddress(), device.portNumber());
            InputStream sis = socket.getInputStream();
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            SecureObjectInputStream secureOis = new SecureObjectInputStream(sis)) {

            os.writeObject(filter);
            for (E elem  : list) {
                if (isReceiverReplying(secureOis))
                    os.writeObject(elem);
                else
                    throw new MessengerException();
            }

        } catch (IOException | ClassNotFoundException exception) {
            throw new MessengerException();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static boolean isReceiverReplying(SecureObjectInputStream secureOis) throws IOException, ClassNotFoundException {
        String currReply = (String) secureOis.readObject();
        return currReply.equals("next");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
