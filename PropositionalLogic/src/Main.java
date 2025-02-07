import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        /* A = ((¬A0)->A1) */
        Supplier<PExpression> A = () -> (
                PExpression.contain_of(
                        PExpression.not_of(PExpression.var_of(PSymbol.of(0))),
                        PExpression.var_of(PSymbol.of(1))
                )
        );
        printTruthTable(A, "A");

        /* B = (A0 ∧ A1 ∧ A2) */
        Supplier<PExpression> B = () -> (
                PExpression.and_of(
                        PExpression.var_of(PSymbol.of(0)),
                        PExpression.var_of(PSymbol.of(1)),
                        PExpression.var_of(PSymbol.of(2))
                )
        );
        printTruthTable(B, "B");

        /* C = ((A0 ∧ (A0 → A1)) → A1) */
        Supplier<PExpression> C = () -> (
                PExpression.contain_of(
                        PExpression.and_of(
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.contain_of(
                                        PExpression.var_of(PSymbol.of(0)),
                                        PExpression.var_of(PSymbol.of(1))
                                )
                        ), PExpression.var_of(PSymbol.of(1))
                )
        );
        printTruthTable(C, "C");

        /* D = (A0 ∨ A1 ∨ A2) */
        Supplier<PExpression> D = () -> (
                PExpression.or_of(
                        PExpression.var_of(PSymbol.of(0)),
                        PExpression.var_of(PSymbol.of(1)),
                        PExpression.var_of(PSymbol.of(2))
                )
        );
        printTruthTable(D, "D");

        /* X = ((¬A0) ∧ A1) ∨ (A0 ∧ (¬A1)) */
        Supplier<PExpression> X = () -> (
                PExpression.or_of(
                        PExpression.and_of(
                                PExpression.not_of(PExpression.var_of(PSymbol.of(0))),
                                PExpression.var_of(PSymbol.of(1))
                        ), PExpression.and_of(
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.not_of(PExpression.var_of(PSymbol.of(1)))
                        )
                )
        );
        printTruthTable(X, "X");

        /* E = A0 ↔ A0 ↔ (A0 ∨ A0) ↔ (A0 ∧ A0) ↔ (A0 ∨ A0 ∨ A0) ↔ (A0 ∧ A0 ∧
        A0) */
        Supplier<PExpression> E = () -> (
                PExpression.equiv_of(
                        PExpression.var_of(PSymbol.of(0)),
                        PExpression.var_of(PSymbol.of(0)),
                        PExpression.or_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0))),
                        PExpression.and_of(PExpression.var_of(PSymbol.of(0)), PExpression.var_of(PSymbol.of(0))),
                        PExpression.or_of(
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.var_of(PSymbol.of(0))
                        ),
                        PExpression.and_of(
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.var_of(PSymbol.of(0)),
                                PExpression.var_of(PSymbol.of(0))
                        )
                )
        );
        printTruthTable(E, "E");

        /* pattern f(s,t): s -> t */
        Supplier<PExpression> fst = getFst();
        printTruthTable(fst, "fst");

        /*  */
    }

    private static Supplier<PExpression> getFst() {
        // antecedent -> consequent
        Pattern f = new Pattern(expressions -> {
            if (expressions.size() != 2) {
                throw new IllegalArgumentException("Implication pattern requires exactly 2 arguments.");
            }
            Supplier<PExpression> antecedent = expressions.get(0);
            Supplier<PExpression> consequent = expressions.get(1);
            return () -> PExpression.contain_of(antecedent.get(), consequent.get());
        });
        // antecedent : A0
        Supplier<PExpression> s = () -> PExpression.var_of(PSymbol.of(0));
        // consequent : A0 -> A1
        Supplier<PExpression> t = () -> PExpression.contain_of(
                PExpression.var_of(PSymbol.of(0)),
                PExpression.var_of(PSymbol.of(1))
        );
        // (A0) -> (A0 -> A1)
        return f.substitute(s, t);
    }

    public static void printTruthTable(Supplier<PExpression> pExp, String pName) {
        PExpression pExpGet = pExp.get();
        System.out.println("> " + pName + " : " + pExpGet);
        List<PSymbol> variableList = new ArrayList<>(pExpGet.getVariableSet());
        variableList.sort(Comparator.comparingInt(PSymbol::getType));
        System.out.println("Variable List: " + variableList);
        Boolean[][] truthTable = (new TruthAssigner()).truthTraverser(variableList.size());
        for (Boolean[] truth : truthTable) {
            StringBuilder varValStr = new StringBuilder();
            for (PSymbol pSymbol : variableList) {
                int pIdx = variableList.indexOf(pSymbol);
                try {
                    pSymbol.setValue(truth[pIdx]);
                    varValStr.append(pSymbol).append(" : ").append(pSymbol.getValue()).append("\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            pExpGet = pExp.get();
            System.out.println(">>>>> Truth Table =====\n" +
                               varValStr +
                               pName +
                               " : " +
                               pExpGet.getValue() +
                               "\n" +
                               "===== Truth Table <<<<<");
        }
    }
}