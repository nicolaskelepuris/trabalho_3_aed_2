package huffman;

public final class App {
    private App() {
    }
    public static void main(String[] args) {
        Encoder.Encode("teste-txt.txt");
        Encoder.Encode("teste-doc.docx");
        Encoder.Encode("teste-img.png");
    }
}
