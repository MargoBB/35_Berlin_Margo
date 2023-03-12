public class HexDisplay extends Display {
    private int num;

    public HexDisplay(int num) {
        this.num = num;
    }

    @Override
    public void display() {
        System.out.println(Integer.toHexString(num));
    }
}