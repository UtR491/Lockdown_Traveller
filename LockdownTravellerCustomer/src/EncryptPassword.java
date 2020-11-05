import java.security.MessageDigest;

/**
 * Convert the password to it's SHA-256 hash.
 */
public class EncryptPassword {
    /**
     * Return the SHA-256 hash for a given value.
     * @param value Password to be converted.
     * @return Hash of the password in hexadecimal format.
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
     * Convert the hash from byte array to hexadecimal string.
     * @param bytes Hash in the byte array form.
     * @return Hexadecimal string of the hash.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes)
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}