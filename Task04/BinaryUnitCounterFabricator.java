public class BinaryUnitCounterFabricator implements Fabricatable {
    private double[] args;

    public BinaryUnitCounterFabricator(double[] args) {
        this.args = args;
    }

    @Override
    public BinaryUnitCounter fabricate() {
        BinaryUnitCounter buc = new BinaryUnitCounter(args);
        buc.calculate();
        return buc;
    }
}

