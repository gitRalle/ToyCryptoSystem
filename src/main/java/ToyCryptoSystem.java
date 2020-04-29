import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ToyCryptoSystem {

    private static final Charset CHARSET = StandardCharsets.US_ASCII;
    private static final ByteArrayUtils utils = new ByteArrayUtils(CHARSET);

    public static void main(String[] args) throws Exception {
        final HashSet<String> dict = new IO().readFile(CHARSET);
        final byte[] message = generateMessage(dict);
        final byte[] key = generateKey();

        System.out.printf("key=%d, words=%s%n", utils.getShort(key), utils.getText(message));

        for (Map.Entry<Short, ArrayList<String>> kv :
        filter(bruteForce(cipher(message, key), dict)).entrySet())
            System.out.printf("key=%d, words=%s%n", kv.getKey(), kv.getValue().toString());
    }


    /**
     * encrypts/decrypts a byte[] message using a byte[] 16-bit key.
     *
     * @param input: the message to be encrypted/decrypted.
     * @param key:   the key to be used in the encryption/decryption.
     * @return: the encrypted/decrypted message.
     */
    private static byte[] cipher(final byte[] input, final byte[] key) {
        final byte[] padded = utils.pad(input);
        final byte[] mask = new byte[padded.length];
        final byte[] output = new byte[padded.length];

        int k = 0;
        for (int i = 0; i < padded.length / 2; i++) {
            for (byte b : key)
                mask[k++] = b;
        }

        int i = 0;
        for (byte b : padded)
            output[i] = (byte) (b ^ mask[i++]);

        return output;
    }

    /**
     * generates a random message.
     * @param dict: the dictionary used to fetch the words from.
     * @return: the message.
     */
    private static byte[] generateMessage(final Collection<String> dict) {
        ArrayList<String> listDict = new ArrayList<>(dict);
        Random random = new Random();
        final int length = 2 + random.nextInt(7);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rand = random.nextInt(dict.size() - 1);
            builder.append(listDict.get(rand));
            if (i != length - 1)
                builder.append(' ');
        }

        return new String(builder).getBytes(CHARSET);
    }


    /**
     * generates a random key in the interval { -2 * 2^14, 2 * 2^14 }
     * @return: the key
     */
    private static byte[] generateKey() {
        Random random = new Random();
        int randInt = Short.MIN_VALUE + random.nextInt(2 * Short.MAX_VALUE);
        return utils.getBytes((short) randInt);
    }


    /**
     * a function to brute-force through 2^16 keys.
     *
     * @param cipherText: the encrypted cipherText to be brute-forced through.
     * @param dict:       the dictionary that will be used to compare decrypted bits of strings with.
     * @return: the data structure HashMap<Short, ArrayList<String>>,
     * where the key is the 16-bit key used for the successful decryption,
     * and the value is a list of all the words that were matched with a word in the specified dictionary.
     */
    private static HashMap<Short, ArrayList<String>> bruteForce(final byte[] cipherText, final Collection<String> dict) {

        final HashMap<Short, ArrayList<String>> keys = new HashMap<>();

        for (short key = Short.MIN_VALUE; key < Short.MAX_VALUE; key++) {

            final byte[] plainText = cipher(cipherText, utils.getBytes(key));

            final List<byte[]> words = utils.split(plainText);

            if (words.size() == 0)
            {
                if (dict.contains(utils.getText(plainText).toLowerCase()))
                    keys.put(key, new ArrayList<>() {{ add(utils.getText(plainText)); }});
            }
            else
            {
                final ArrayList<String> val = new ArrayList<>();

                for (byte[] word : words)
                {
                    if (dict.contains(utils.getText(word).toLowerCase()))
                    {
                        val.add(utils.getText(word));
                        keys.put(key, val);
                    }
                }
            }
        }
        return keys;
    }


    private static HashMap<Short, ArrayList<String>> filter(final HashMap<Short, ArrayList<String>> keys) {
        final HashMap<Short, ArrayList<String>> filteredKeys = new HashMap<>();

        int max = 0;
        int maxL = 0;

        for (Map.Entry<Short, ArrayList<String>> kv : keys.entrySet()) {
            int size = kv.getValue().size();
            int len = kv.getValue().toString().length();
            if (size * len > max * maxL) {
                max = size;
                maxL = len;
            }
        }

        for (Map.Entry<Short, ArrayList<String>> kv : keys.entrySet()) {
            if (kv.getValue().size() == max && kv.getValue().toString().length() == maxL)
                filteredKeys.put(kv.getKey(), kv.getValue());
        }

        return filteredKeys;
    }


}
