import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        /* A = ((¬A0)->A1) */
        Supplier<PExpression> A = () -> (PExpression.contain_of(PExpression.not_of(PExpression.var_of(PSymbol.of(0))), PExpression.var_of(PSymbol.of(1))));

        printTruthTable(A, "A", 2);

        /* B = (A0 ∧ A1 ∧ A2) */
        Supplier<PExpression> B = () -> (PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(1)), PExpression.var_of(PSymbol.of(2))));

        printTruthTable(B, "B", 3);

        /* C = ((A0 ∧ (A0 → A1)) → A1) */
        Supplier<PExpression> C = () -> (PExpression.contain_of(PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.contain_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(1)))), PExpression.var_of(PSymbol.of(1))));

        printTruthTable(C, "C", 2);

        /* D = (A0 ∨ A1 ∨ A2) */
        Supplier<PExpression> D = () -> (PExpression.or_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(1)), PExpression.var_of(PSymbol.of(2))));

        printTruthTable(D, "D", 3);

        /* X = ((¬A0) ∧ A1) ∨ (A0 ∧ (¬A1)) */
        Supplier<PExpression> X = () -> (PExpression.or_of(PExpression.and_of(PExpression.not_of(PExpression.var_of(PSymbol.of(0))), PExpression.var_of(PSymbol.of(1))), PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.not_of(PExpression.var_of(PSymbol.of(1))))));

        printTruthTable(X, "X", 2);

        /* E = A0 ↔ A0 ↔ (A0 ∨ A0) ↔ (A0 ∧ A0) ↔ (A0 ∨ A0 ∨ A0) ↔ (A0 ∧ A0 ∧
        A0) */

        Supplier<PExpression> E = () -> (PExpression.equiv_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0)), PExpression.or_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0))), PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0))), PExpression.or_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0))), PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0)))));

        printTruthTable(E, "E", 1);
    }

    public static void checkTruthTable(Supplier<PExpression> pExp, String pName, int n) {
        Boolean[][] truthTable = (new TruthAssigner()).truthTraverser(n);
        for (Boolean[] truth : truthTable) {
            try {
                for (int i = 0; i < n; i++) {
                    PSymbol.of(i).setValue(truth[i]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
            }
            StringBuilder varValStr = new StringBuilder();
            for (int k = 0; k < n; k++) {
                varValStr.append(PSymbol.of(k)).append(" : ").append(PSymbol.of(k).getValue()).append("\n");
            }
            System.out.println(">>>>> Truth Table =====\n" + varValStr + pName + " : " + pExp.get().getValue() + "\n" + "===== Truth Table <<<<<\n");
        }
    }

    public static void printTruthTable(Supplier<PExpression> pExp, String pName, int n) {
        System.out.println("> " + pName + " : " + pExp.get());
        System.out.println();
        checkTruthTable(pExp, pName, n);
    }
}

