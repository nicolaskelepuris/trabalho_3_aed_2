package huffman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try {
            // testes com txt, docx e png
            Encoder.encode("teste-txt.txt");
            Encoder.encode("teste-doc.docx");
            Encoder.encode("teste-img.png");
            Decoder.decode("teste-txt.txt-encoded.bin");
            Decoder.decode("teste-doc.docx-encoded.bin");
            Decoder.decode("teste-img.png-encoded.bin");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String inputCommand = "1";

            while (areEqual(inputCommand, "1") || areEqual(inputCommand, "2")) {
                System.out.println("Comandos:\n1 - Comprimir arquivo\n2 - Descomprimir arquivo\n3 - Sair");
                inputCommand = reader.readLine();

                executeCommand(inputCommand, reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean areEqual(String str1, String str2) {
        return str1.compareTo(str2) == 0;
    }

    private static void executeCommand(String inputCommand, BufferedReader reader) throws IOException {
        switch (inputCommand) {
            case "1":
                System.out.println("Informe o caminho do arquivo a ser comprimido:");
                Encoder.encode(reader.readLine());
                break;
            case "2":
                System.out.println("Informe o caminho do arquivo a ser descomprimido:");
                Decoder.decode(reader.readLine());
                break;
            default:
                break;
        }
    }
}
