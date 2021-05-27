package huffman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import huffman.lists.LinkedList;
import huffman.lists.ListNode;

public class Encoder {
    /**
     * Codifica um arquivo usando o algoritmo Huffman
     * @param filePath
     * @throws UnsupportedEncodingException
     */
    public static void encode(String filePath) throws UnsupportedEncodingException {
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String content = new String(bytes, "ISO-8859-1");

        if (content.isEmpty()) {
            return;
        }

        // cria todos os nodes que tem seu valor em Char e a quantidade de vezes que
        // apareceu no conteudo do arquivo
        LinkedList<Node<Character>> nodes = createTrieNodes(content);

        // junta todos os nodes ate formar o node raiz, sempre juntando os 2 nodes com
        // menor quantidade de vezes que apareceu no conteudo do arquivo
        Node<Character> root = joinTrieNodes(nodes);

        // cria os codigos binarios (em string) para cada char encontrado no conteudo do arquivo de
        // acordo com a arvore criada
        // codigo: sempre que for para o node da esquerda adiciona FALSE, sempre que for
        // para o node da direita adiciona TRUE, ate chegar no no folha
        // exemplo:
        //
        // (false)left <- root -> rigth(true)
        //
        LinkedList<Code> codes = createCodes(root);

        // utiliza os codes criados (mapeamento entre char e string representando os bools do codigo)
        // para criar um array de booleanos com todo o conteudo do arquivo codificado
        boolean[] encodedContent = encodeContent(content, codes);

        // adiciona extensão .bin para criar arquivo binario
        String path = filePath + "-encoded.bin";

        // cria o arquivo codificado, primeiro escrevendo a trie e a quantidade de characters para posterior decodificacao
        // depois escreve o conteudo codificado
        createEncodedFile(encodedContent, path, root, content.length());
    }

    private static void createEncodedFile(boolean[] encodedContent, String path, Node<Character> root, int contentLength) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            // escreve no arquivo a trie, para posterior decodificacao
            writeTrie(root, outputStream);

            // escreve a quantidade de characters no arquivo original, para posterior decodificacao
            BinaryStdOut.write(contentLength, outputStream);

            // escreve o conteudo que foi codificado
            writeEncodedContent(outputStream, encodedContent);
            BinaryStdOut.close(outputStream);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o arquivo apos compressao: " + path);
            e.printStackTrace();
        }
    }

    private static boolean[] encodeContent(String content, LinkedList<Code> codes) {
        StringBuilder builder = new StringBuilder();

        for (char character : content.toCharArray()) {
            String encodedChar = codes.FirstOrDefault((node) -> node.Value.Key.charValue() == character).Value.Value;
            builder.append(encodedChar);
        }

        char[] chars = builder.toString().toCharArray();
        boolean[] encodedContent = new boolean[chars.length];

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '1') {
                encodedContent[i] = true;
            } else {
                encodedContent[i] = false;
            }
        }

        return encodedContent;
    }

    private static void writeTrie(Node<Character> node, FileOutputStream outputStream) {
        if (node.isLeaf()) {
            BinaryStdOut.write(true, outputStream);
            BinaryStdOut.write(node.Value, 8, outputStream);
            return;
        }
        BinaryStdOut.write(false, outputStream);
        writeTrie(node.Left, outputStream);
        writeTrie(node.Rigth, outputStream);
    }

    private static void writeEncodedContent(FileOutputStream outputStream, boolean[] encodedContent) throws IOException {
        for (boolean bool : encodedContent) {
            BinaryStdOut.write(bool, outputStream);
        }
    }

    private static LinkedList<Code> createCodes(Node<Character> trieRoot) {
        LinkedList<Code> codes = new LinkedList<Code>();

        traverseTrie(trieRoot, codes, "");

        return codes;
    }

    private static void traverseTrie(Node<Character> node, LinkedList<Code> codes, String currentCode) {
        if (node.isLeaf()) {
            codes.Add(new Code(node.Value, currentCode));
            return;
        }

        if (node.Left != null) {
            traverseTrie(node.Left, codes, currentCode + "0");
        }

        if (node.Rigth != null) {
            traverseTrie(node.Rigth, codes, currentCode + "1");
        }
    }

    private static Node<Character> joinTrieNodes(LinkedList<Node<Character>> nodes) {
        while (!nodes.hasOnlyOneElement()) {
            ListNode<Node<Character>> minimumCountNode = findMinumumCountNode(nodes);

            nodes.Remove(minimumCountNode);

            ListNode<Node<Character>> newMinimumCountNode = findMinumumCountNode(nodes);

            nodes.Remove(newMinimumCountNode);

            Node<Character> joinNode = new Node<Character>(null,
                    minimumCountNode.Value.Count + newMinimumCountNode.Value.Count);
            joinNode.Left = minimumCountNode.Value;
            joinNode.Rigth = newMinimumCountNode.Value;
            nodes.Add(joinNode);
        }

        return nodes.Primeiro.Value;
    }

    private static ListNode<Node<Character>> findMinumumCountNode(LinkedList<Node<Character>> nodes) {
        if (nodes.isEmpty())
            return null;

        ListNode<Node<Character>> minimumCountNode = nodes.Primeiro;

        ListNode<Node<Character>> result;
        do {
            int minimumCount = minimumCountNode.Value.Count;
            result = nodes.FirstOrDefault((node) -> node.Value.Count < minimumCount);

            if (result != null) {
                minimumCountNode = result;
            }
        } while (result != null);

        return minimumCountNode;
    }

    private static LinkedList<Node<Character>> createTrieNodes(String content) {
        LinkedList<Node<Character>> nodes = new LinkedList<Node<Character>>();

        for (char c : content.toCharArray()) {
            // checa se o char ja foi adicionado na lista
            ListNode<Node<Character>> listNode = nodes.FirstOrDefault((node) -> node.Value.Value == c);

            // se ja foi adicionado aumenta apenas a quantidade de aparicoes, se nao foi
            // adicionado, o adiciona
            if (listNode != null) {
                listNode.Value.Count++;
            } else {
                Node<Character> node = new Node<Character>(c);
                nodes.Add(node);
            }
        }

        return nodes;
    }
}
