import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("CommentedOutCode")
public class PExpression {
    private final List<PSymbol> pSymbolList;

    private Boolean value;

    private PExpression(List<PSymbol> pSymbolList) {
        this.pSymbolList = pSymbolList;
    }

    /**
     * <h1>Make proposition expression A<sub>n</sub></h1>
     * <p>
     * Make a proposition which only contains single 1 variable, it looks
     * like A<sub>n</sub>, which n is a natural
     * number.
     *
     * @param pSymbol the propositional variable
     * @return expression A<sub>n</sub>
     */
    public static PExpression var_of(PSymbol pSymbol) {
        if (pSymbol.type() < 0) {
            throw new IllegalArgumentException("not a propositional variable");
        }
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(pSymbol);
        pExpression.value = pSymbol.getValue();
        return pExpression;
    }

    /**
     * <h1>Make proposition expression (¬s)</h1>
     * <p>
     * Make a proposition which is an exist proposition's not.
     *
     * @param s the origin proposition
     * @return (¬ s)
     */
    public static PExpression not_of(PExpression s) {
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(PSymbol.of(-1));
        pExpression.pSymbolList.add(PSymbol.of(-3));
        pExpression.pSymbolList.addAll(s.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-2));

        if (Boolean.FALSE.equals(s.getValue())) {
            pExpression.value = Boolean.TRUE;
        } else if (Boolean.TRUE.equals(s.getValue())) {
            pExpression.value = Boolean.FALSE;
        } else {
            pExpression.value = null;
        }

        return pExpression;
    }
//    public PExpression setSymbolToNewExpression(int symbolType, PExpression pExpression) {
//        assert symbolType >= 0;
//        List<PSymbol> pSymbolList = new LinkedList<>();
//        for (PSymbol pSymbol : this.pSymbolList) {
//            if (pSymbol.type() == symbolType) {
//                pSymbolList.addAll(pExpression.pSymbolList);
//            } else {
//                pSymbolList.add(pSymbol);
//            }
//        }
//        return new PExpression(pSymbolList);
//    }

    /**
     * <h1>Make proposition expression (s→t)</h1>
     * <p>
     * Connection two expressions by causality.
     *
     * @param s the reason
     * @param t the result
     * @return (s → t)
     */
    public static PExpression contain_of(PExpression s, PExpression t) {
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(PSymbol.of(-1));
        pExpression.pSymbolList.addAll(s.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-4));
        pExpression.pSymbolList.addAll(t.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-2));

        if (Objects.isNull(s.getValue()) || Objects.isNull(t.getValue())) {
            pExpression.value = null;
        } else {
            if (Boolean.TRUE.equals(s.getValue()) && Boolean.TRUE.equals(t.getValue())) {
                pExpression.value = Boolean.TRUE;
            } else if (Boolean.FALSE.equals(s.getValue()) && Boolean.TRUE.equals(t.getValue())) {
                pExpression.value = Boolean.TRUE;
            } else if (Boolean.FALSE.equals(s.getValue()) && Boolean.FALSE.equals(t.getValue())) {
                pExpression.value = Boolean.TRUE;
            } else if (Boolean.TRUE.equals(s.getValue()) && Boolean.FALSE.equals(t.getValue())) {
                pExpression.value = Boolean.FALSE;
            }
        }

        return pExpression;
    }

    /**
     * <h1>Make proposition expression (φ∧ψ)</h1>
     * <p>
     * Make proposition expression (φ∧ψ) which is the logical-and of φ, ψ
     * <p>
     * Associativity : (φ<sub>1</sub>∧φ<sub>2</sub>∧...∧φ<sub>n</sub>) is
     * well-defined and bracket-position-independent
     *
     * @param phi the first component φ
     * @param psi the second component ψ
     * @return (φ ∧ ψ) = (¬(φ → (¬ψ)))
     */
    public static PExpression and_of(PExpression phi, PExpression psi) {
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(PSymbol.of(-1));
        pExpression.pSymbolList.addAll(phi.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-6));
        pExpression.pSymbolList.addAll(psi.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-2));

        pExpression.value = not_of(contain_of(phi, not_of(psi))).getValue();

        return pExpression;
    }

    public static PExpression and_of(PExpression... phis) {
        if (phis.length < 1) {
            throw new IllegalArgumentException("not a propositional variable");
        } else if (phis.length == 1) {
            return phis[0];
        } else {
            PExpression pExpression = new PExpression(new LinkedList<>());
            pExpression.pSymbolList.add(PSymbol.of(-1));
            for (PExpression phi : phis) {
                pExpression.pSymbolList.addAll(phi.pSymbolList);
                pExpression.pSymbolList.add(PSymbol.of(-6));
            }
            pExpression.pSymbolList.removeLast();
            pExpression.pSymbolList.add(PSymbol.of(-2));

            pExpression.value = and_of(phis[0], and_of(Arrays.copyOfRange(phis, 1, phis.length))).getValue();

            return pExpression;
        }
    }

    /**
     * <h1>Make proposition expression (φ∨ψ)</h1>
     * <p>
     * Make proposition expression (φ∨ψ) which is the logical-or of φ, ψ
     * <p>
     * Associativity : (φ<sub>1</sub>∨φ<sub>2</sub>∨...∨φ<sub>n</sub>) is
     * well-defined and bracket-position-independent
     *
     * @param phi the first component φ
     * @param psi the second component ψ
     * @return (φ ∨ ψ) = ((¬φ) → ψ)
     */
    public static PExpression or_of(PExpression phi, PExpression psi) {
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(PSymbol.of(-1));
        pExpression.pSymbolList.addAll(phi.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-7));
        pExpression.pSymbolList.addAll(psi.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-2));

        pExpression.value = contain_of(not_of(phi), psi).getValue();

        return pExpression;
    }

    public static PExpression or_of(PExpression... phis) {
        if (phis.length < 1) {
            throw new IllegalArgumentException("not a propositional variable");
        } else if (phis.length == 1) {
            return phis[0];
        } else {
            PExpression pExpression = new PExpression(new LinkedList<>());
            pExpression.pSymbolList.add(PSymbol.of(-1));
            for (PExpression phi : phis) {
                pExpression.pSymbolList.addAll(phi.pSymbolList);
                pExpression.pSymbolList.add(PSymbol.of(-7));
            }
            pExpression.pSymbolList.removeLast();
            pExpression.pSymbolList.add(PSymbol.of(-2));

            pExpression.value = or_of(phis[0], or_of(Arrays.copyOfRange(phis, 1, phis.length))).getValue();

            return pExpression;
        }
    }

    /**
     * <h1>Make proposition expression (s↔t)</h1>
     * <p>
     * Make proposition expression which means s and t is co-implicative
     * (logical-equivalent).
     *
     * @param s the first component
     * @param t the second component
     * @return (s ↔ t) = ((s → t) ∧ (t → s))
     */
    public static PExpression equiv_of(PExpression s, PExpression t) {
        PExpression pExpression = new PExpression(new LinkedList<>());
        pExpression.pSymbolList.add(PSymbol.of(-1));
        pExpression.pSymbolList.addAll(s.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-5));
        pExpression.pSymbolList.addAll(t.pSymbolList);
        pExpression.pSymbolList.add(PSymbol.of(-2));

        pExpression.value = and_of(contain_of(s, t), contain_of(t, s)).getValue();

        return pExpression;
    }

    public static PExpression equiv_of(PExpression... xs) {
        if (xs.length < 1) {
            throw new IllegalArgumentException("not a propositional variable");
        } else if (xs.length == 1) {
            return xs[0];
        } else {
            PExpression pExpression = new PExpression(new LinkedList<>());
            pExpression.pSymbolList.add(PSymbol.of(-1));
            for (PExpression phi : xs) {
                pExpression.pSymbolList.addAll(phi.pSymbolList);
                pExpression.pSymbolList.add(PSymbol.of(-5));
            }
            pExpression.pSymbolList.removeLast();
            pExpression.pSymbolList.add(PSymbol.of(-2));

            pExpression.value = equiv_of(xs[0], equiv_of(Arrays.copyOfRange(xs, 1, xs.length))).getValue();

            return pExpression;
        }
    }

    @SuppressWarnings("unused")
    public int getSymbolTypeMax() {
        int type = 0;
        for (PSymbol symbol : pSymbolList) {
            type = Math.max(type, symbol.type());
        }
        return type;
    }

    /**
     * @deprecated The truth value of the expression after the processing mode replacement is too complex because we have
     * not implemented a truth value-based computing system. We plan to rewrite the system based on reasoning rather than
     * truth value assignment, so this function is deprecated.
     */
    @SuppressWarnings("unused")
    public PExpression setSymbolToNewExpression(int symbolType, PExpression pExpression) {
        return PExpression.var_of(PSymbol.of(0));
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PSymbol pSymbol : pSymbolList) {
            stringBuilder.append(pSymbol.toString());
        }
        return stringBuilder.toString();
    }
}
