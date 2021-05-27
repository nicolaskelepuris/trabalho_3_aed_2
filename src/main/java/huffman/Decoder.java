package huffman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Decoder {
    /**
     * Decodifica um arquivo que foi codificado usando o algoritmo Huffman
     * @param filePath - caminho do arquivo que sera decodificado
     * @throws IOException
     */
    public static void decode(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);

        String path = filePath.replace("-encoded.bin", "");

        path = path.replace(Paths.get(path).getFileName().toString(), "") + "decoded-" + Paths.get(path).getFileName();

        FileOutputStream outputStream = new FileOutputStream(path);

        Node<Character> root = readTrie(inputStream);

        int length = BinaryStdIn.readInt(inputStream);

        for (int i = 0; i < length; i++) {
            Node<Character> currentNode = root;
            while (!currentNode.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean(inputStream);
                if (bit) {
                    currentNode = currentNode.Rigth;
                } else {
                    currentNode = currentNode.Left;
                }
            }
            BinaryStdOut.write(currentNode.Value, 8, outputStream);
        }
        BinaryStdIn.close(inputStream);
        BinaryStdOut.close(outputStream);
    }

    private static Node<Character> readTrie(FileInputStream inputStream) {
        boolean isLeaf = BinaryStdIn.readBoolean(inputStream);
        if (isLeaf) {
            return new Node<Character>(BinaryStdIn.readChar(inputStream), -1);
        } else {
            return new Node<Character>('\0', -1, readTrie(inputStream), readTrie(inputStream));
        }
    }
}
