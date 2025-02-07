public class TruthAssigner {
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