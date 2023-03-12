public class DisplayFactory {
    public static Display createDisplay(String type, int num) {
        if (type.equals("binary")) {
            return new BinaryDisplay(num);
        } else if (type.equals("hex")) {
            return new HexDisplay(num);
        } else {
            throw new IllegalArgumentException("Invalid display type: " + type);
        }
    }
}