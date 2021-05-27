package huffman;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try {
            Encoder.Encode("teste-txt.txt");
            Encoder.Encode("teste-doc.docx");
            Encoder.Encode("teste-img.png");
            Decoder.Decode("encoded-teste-txt.txt.bin");
            Decoder.Decode("encoded-teste-doc.docx.bin");
            Decoder.Decode("encoded-teste-img.png.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
