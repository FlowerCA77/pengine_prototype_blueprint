import java.util.*;
import java.util.function.Supplier;

/**
 * @author Chen Ao
 */
@SuppressWarnings({"NonAsciiCharacters", "unused"})
public class Axiom {
    // A1 : φ → (ψ → φ)
    public static Pattern A1 = new Pattern(expressions -> {
        if (expressions.size() != 2) {
            throw new IllegalArgumentException("Implication pattern requires exactly 2 arguments.");
        }
        Supplier<PExpression> φ = expressions.get(0);
        Supplier<PExpression> ψ = expressions.get(1);
        return () -> PExpression.contain_of(φ.get(), PExpression.contain_of(ψ.get(), φ.get()));
    });

    // A2 : (φ → (ψ → χ)) → ((φ → ψ) → (φ → χ))
    public static Pattern A2 = new Pattern(expressions -> {
        if (expressions.size() != 3) {
            throw new IllegalArgumentException("Implication pattern requires exactly 3 arguments.");
        }
        Supplier<PExpression> φ = expressions.get(0);
        Supplier<PExpression> ψ = expressions.get(1);
        Supplier<PExpression> χ = expressions.get(2);
        return () -> PExpression.contain_of(
                PExpression.contain_of(φ.get(), PExpression.contain_of(ψ.get(), χ.get())),
                PExpression.contain_of(
                        PExpression.contain_of(φ.get(), ψ.get()),
                        PExpression.contain_of(φ.get(), χ.get())
                )
        );
    });

    // A3 : (¬φ → ¬ψ) → (ψ → φ)
    public static Pattern A3 = new Pattern(expressions -> {
        if (expressions.size() != 2) {
            throw new IllegalArgumentException("Implication pattern requires exactly 2 arguments.");
        }
        Supplier<PExpression> φ = expressions.get(0);
        Supplier<PExpression> ψ = expressions.get(1);
        return () -> PExpression.contain_of(
                PExpression.contain_of(PExpression.not_of(φ.get()), PExpression.not_of(ψ.get())),
                PExpression.contain_of(ψ.get(), φ.get())
        );
    });

    public Set<Set<Pattern>> axiomSets = new HashSet<>();
    public Set<Pattern> hilbert = new HashSet<>();

    public Axiom() {
        hilbert.add(A1);
        hilbert.add(A2);
        hilbert.add(A3);
        axiomSets.add(hilbert);
    }

    public static Boolean checkTruthValue(Supplier<PExpression> pExp) {
        PExpression pExpGet = pExp.get();
        List<PSymbol> variableList = new ArrayList<>(pExpGet.getVariableSet());
        variableList.sort(Comparator.comparingInt(PSymbol::getType));
        Boolean[][] truthTable = (new TruthAssigner()).truthTraverser(variableList.size());
        PSymbol.initSymbolTable();
        for (Boolean[] truth : truthTable) {
            for (PSymbol pSymbol : variableList) {
                int pIdx = variableList.indexOf(pSymbol);
                try {
                    pSymbol.setValue(truth[pIdx]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!pExp.get().getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * To prove the correctness of Hilbert's axioms here, we do so by checking the truth values. For the convenience of
     * programming, we directly use the <code>PSymbol.value</code> truth value to represent the truth value of the
     * pattern, because the truth value of any propositional expression and that of a propositional symbol are equivalent
     * in the pattern.
     */
    public void proofHilbert() {
        Supplier<PExpression> A1realize = A1.substitute(
                () -> PExpression.var_of(PSymbol.of(0)),
                () -> PExpression.var_of(PSymbol.of(1))
        );
        System.out.println("A1: " + checkTruthValue(A1realize));
        Supplier<PExpression> A2realize = A2.substitute(
                () -> PExpression.var_of(PSymbol.of(0)),
                () -> PExpression.var_of(PSymbol.of(1)),
                () -> PExpression.var_of(PSymbol.of(2))
        );
        System.out.println("A2: " + checkTruthValue(A2realize));
        Supplier<PExpression> A3realize = A3.substitute(
                () -> PExpression.var_of(PSymbol.of(0)),
                () -> PExpression.var_of(PSymbol.of(1))
        );
        System.out.println("A3: " + checkTruthValue(A3realize));
    }
}
