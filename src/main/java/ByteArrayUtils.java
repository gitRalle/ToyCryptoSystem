import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

class ByteArrayUtils {

    private final Charset CHARSET;

    /**
     * constructor exists for CHARSET convenience. class should otherwise be static.
     * @param charset: the charset the user would like to use.
     */
    ByteArrayUtils(Charset charset)
    {
        CHARSET = charset;
    }

    /**
     * converts a short into a byte[].
     * @param value: the short value.
     * @return: the byte[].
     */
    byte[] getBytes(short value)
    {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        return buffer.array();
    }

    /**
     * converts a string into a byte[].
     * @param value: the string
     * @return: the byte[].
     */
    byte[] getBytes(String value)
    {
        return value.getBytes(CHARSET);
    }

    /**
     * converts a byte[] into a short.
     * @param input: the byte[]
     * @return: the short.
     */
    short getShort(byte[] input)
    {
        return ByteBuffer.wrap(input).getShort();
    }

    /**
     * converts a byte[] into a string.
     * @param input: the byte[].
     * @return: the string.
     */
    String getText(byte[] input)
    {
        return new String(input, CHARSET);
    }

    /**
     * pads a byte(32) to a byte[], if the byte[].length % 2 != 0.
     * @param input: the byte[].
     * @return: the padded byte[].
     */
    byte[] pad(byte[] input)
    {
        if (input.length % 2 != 0)
            return ArrayUtils.add(input, (byte) 32);
        else
            return input;
    }

    /**
     * splits a byte[] using either delimiter byte(0) || byte(32).
     * @param input: the byte[] to be split.
     * @return: the List<byte[]> containing the split byte arrays.
     */
    List<byte[]> split(byte[] input)
    {
        List<byte[]> list = new LinkedList<>();

        int from = 0;
        for (int i = 0; i < input.length; i++) {
            if (i == 0) {
                if (input[i] == 0 || input[i] == 32) {
                    from++;
                }
                continue;
            }

            if (input[i] == 0 || input[i] == 32) {
                list.add(Arrays.copyOfRange(input, from, i));
                from = i + 1;
            }

            if (i == input.length - 1) {
                if (from != 0) {
                    if (input[i] == 0 || input[i] == 32)
                        continue;
                    list.add(Arrays.copyOfRange(input, from, input.length));
                }
            }
        }

        return list;
    }


}
