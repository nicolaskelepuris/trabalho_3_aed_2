package huffman;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try {
            Encoder.encode("teste-txt.txt");
            Encoder.encode("teste-doc.docx");
            Encoder.encode("teste-img.png");
            Decoder.decode("encoded-teste-txt.txt.bin");
            Decoder.decode("encoded-teste-doc.docx.bin");
            Decoder.decode("encoded-teste-img.png.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
