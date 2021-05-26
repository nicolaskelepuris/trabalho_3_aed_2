package huffman;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import huffman.lists.LinkedList;
import huffman.lists.ListNode;

public class Encoder {
    public static void Encode(File file) {
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String content = new String(bytes);

        if (content.isEmpty()) {
            return;
        }

        // cria todos os nodes que tem seu valor em Char e a quantidade de vezes que apareceu no conteudo do arquivo
        LinkedList<Node<Character>> nodes = CreateTrieNodes(content);

        // junta todos os nodes ate formar o node raiz, sempre juntando os 2 nodes com menor quantidade de vezes que apareceu no conteudo do arquivo
        Node<Character> root = JoinTrieNodes(nodes);

        // cria os codigos binarios para cada char encontrado no conteudo do arquivo de acordo com a arvore criada
        // codigo: sempre que for para o node da esquerda adiciona FALSE, sempre que for para o node da direita adiciona TRUE, ate chegar no no folha
        // exemplo:
        //              root
        //             -    -
        //   (false)left    rigth(true)
        //
        LinkedList<Code> codes = CreateCodes(root);
    }

    private static LinkedList<Code> CreateCodes(Node<Character> trieRoot) {
        // TODO: implementar criacao dos codigos
        return new LinkedList<Code>();
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
