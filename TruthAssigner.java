public class TruthAssigner {
    public void assign(Boolean[] truthFunction) {
        for (int i = 0; i < truthFunction.length; i++) {
            PSymbol.of(i).setValue(truthFunction[i]);
        }
    }

    public void assign(boolean[] truthFunction, int[] hash) {
        for (int i = 0; i < truthFunction.length; i++) {
            PSymbol.of(hash[i]).setValue(truthFunction[i]);
        }
    }

    public void assign(boolean[] truthFunction, int low, int high) {
        low = Math.max(low, 0);
        high = Math.max(high, 0);
        for (int i = low; i <= high; i++) {
            PSymbol.of(i).setValue(truthFunction[i]);
        }
    }

    public Boolean[][] truthTraverser(int n) {
        if (n < 1) {
            System.err.println("Invalid truth number, to generate 1 " + "truth-table");
            n = 1;
        }
        Boolean[][] truthTraverser = new Boolean[1 << n][n];
        for (int truth = 0; truth < 1 << n; truth++) {
            for (int bit = 0; bit < n; bit++) {
                int value = truth & (1 << bit);
                if (value == 0) {
                    truthTraverser[truth][bit] = Boolean.FALSE;
                } else if (value == 1 << bit) {
                    truthTraverser[truth][bit] = Boolean.TRUE;
                }
            }
        }
        return truthTraverser;
    }
}
