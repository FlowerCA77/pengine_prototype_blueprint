import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Pattern {
    private final Function<List<Supplier<PExpression>>, Supplier<PExpression>> patternFunction;

    public Pattern(Function<List<Supplier<PExpression>>, Supplier<PExpression>> patternFunction) {
        this.patternFunction = patternFunction;
    }

    @SafeVarargs
    public final Supplier<PExpression> substitute(Supplier<PExpression>... expressions) {
        List<Supplier<PExpression>> exprList = Arrays.asList(expressions);
        return patternFunction.apply(exprList);
    }
}
