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
            throw new IllegalArgumentException("Implication pattern requires exactly 2 arguments.");
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
                PExpression.contain_of(
                        PExpression.not_of(φ.get()),
                        PExpression.not_of(ψ.get())
                ),
                PExpression.contain_of(ψ.get(), φ.get())
        );
    });
}
