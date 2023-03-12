public class BinaryDisplay extends Display {
    private int num;

    public BinaryDisplay(int num) {
        this.num = num;
    }

    @Override
    public void display() {
        System.out.println(Integer.toBinaryString(num));
    }
}
