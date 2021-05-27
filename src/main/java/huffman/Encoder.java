package huffman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import huffman.lists.LinkedList;
import huffman.lists.ListNode;

public class Encoder {
    public static void Encode(String filePath) {
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String content = new String(bytes);

        if (content.isEmpty()) {
            return;
        }

        // cria todos os nodes que tem seu valor em Char e a quantidade de vezes que
        // apareceu no conteudo do arquivo
        LinkedList<Node<Character>> nodes = CreateTrieNodes(content);

        // junta todos os nodes ate formar o node raiz, sempre juntando os 2 nodes com
        // menor quantidade de vezes que apareceu no conteudo do arquivo
        Node<Character> root = JoinTrieNodes(nodes);

        // cria os codigos binarios para cada char encontrado no conteudo do arquivo de
        // acordo com a arvore criada
        // codigo: sempre que for para o node da esquerda adiciona FALSE, sempre que for
        // para o node da direita adiciona TRUE, ate chegar no no folha
        // exemplo:
        //
        // (false)left <- root -> rigth(true)
        //
        LinkedList<Code> codes = CreateCodes(root);

        boolean[] encodedContent = EncodeContent(content, codes);

        String path = "encoded-" + Paths.get(filePath).getFileName() + ".bin";

        CreateEncodedFile(encodedContent, path);
    }

    private static void CreateEncodedFile(boolean[] encodedContent, String path) {
        try {
            FileOutputStream writer = new FileOutputStream(path);
            WriteBooleans(writer, encodedContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o arquivo apos compressao: " + path);
            e.printStackTrace();
        }
    }

    private static boolean[] EncodeContent(String content, LinkedList<Code> codes) {
        StringBuilder builder = new StringBuilder();

        for (char character : content.toCharArray()) {
            String encodedChar = codes.FirstOrDefault((node) -> node.Value.Key.charValue() == character).Value.Value;
            builder.append(encodedChar);
        }

        // TODO: encodar o mapeamento de char -> code no arquivo para poder fazer o decode depois

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

    // escreve os bools de byte em byte (8 em 8 bits) no arquivo
    private static void WriteBooleans(OutputStream out, boolean[] array) throws IOException {
        for (int i = 0; i < array.length; i += 8) {
            int currentByte = 0;
            for (int j = Math.min(i + 7, array.length - 1); j >= i; j--) {
                currentByte = (currentByte << 1) | (array[j] ? 1 : 0);
            }
            out.write(currentByte);
        }
    }

    private static LinkedList<Code> CreateCodes(Node<Character> trieRoot) {
        LinkedList<Code> codes = new LinkedList<Code>();

        TraverseTrie(trieRoot, codes, "");

        return codes;
    }

    private static void TraverseTrie(Node<Character> node, LinkedList<Code> codes, String currentCode) {
        if (node.isLeaf()) {
            codes.Add(new Code(node.Value, currentCode, node.Count));
            return;
        }

        if (node.Left != null) {
            TraverseTrie(node.Left, codes, currentCode + "0");
        }

        if (node.Rigth != null) {
            TraverseTrie(node.Rigth, codes, currentCode + "1");
        }
    }

    private static Node<Character> JoinTrieNodes(LinkedList<Node<Character>> nodes) {
        while (!nodes.hasOnlyOneElement()) {
            ListNode<Node<Character>> minimumCountNode = FindMinumumCountNode(nodes);

            nodes.Remove(minimumCountNode);

            ListNode<Node<Character>> newMinimumCountNode = FindMinumumCountNode(nodes);

            nodes.Remove(newMinimumCountNode);

            Node<Character> joinNode = new Node<Character>(null,
                    minimumCountNode.Value.Count + newMinimumCountNode.Value.Count);
            joinNode.Left = minimumCountNode.Value;
            joinNode.Rigth = newMinimumCountNode.Value;
            nodes.Add(joinNode);
        }

        return nodes.Primeiro.Value;
    }

    private static ListNode<Node<Character>> FindMinumumCountNode(LinkedList<Node<Character>> nodes) {
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

    private static LinkedList<Node<Character>> CreateTrieNodes(String content) {
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
