import java.security.MessageDigest;

/**
 * Class to encrypt password into SHA-256 hash before storing or sending it.
 */
public class EncryptPassword {

    /**
     * Convert the entered password to hash.
     * @param value Entered password.
     * @return Hashed password.
     */
    public static String getHash(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Convert the byte array to hexadecimal value.
     * @param bytes Byte array.
     * @return Hashed password in hexadecimal format.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes)
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}