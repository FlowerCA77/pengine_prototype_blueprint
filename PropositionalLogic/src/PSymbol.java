import java.util.Map;
import java.util.WeakHashMap;

public class PSymbol {
    /**
     * HashMap              ->      The simplest approach                                                               <br>
     * LinkedHashMap        ->      Maximize space use, but easily produce unreachable garbage                          <br>
     * WeakHashMap          ->      With auto GC                                                                        <br>
     * ConcurrentHashMap    ->      With thread safety                                                                  <br>
     */
    private static final Map<Integer, PSymbol> REF_CATCH = new WeakHashMap<>();
    private final int type;
    private Boolean value;

    private PSymbol(int type) {
        this.type = type;
    }

    public static PSymbol of(int type) {
        if (REF_CATCH.containsKey(type)) {
            return REF_CATCH.get(type);
        }
        PSymbol symbol = new PSymbol(type);
        REF_CATCH.put(type, symbol);
        return symbol;
    }

    @SuppressWarnings("unused")
    public static PSymbol of(int type, Boolean value) {
        PSymbol symbol = of(type);
        symbol.setValue(value);
        return symbol;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type >= 0) {
            return "A" + type;
        }
        return switch (type) {
            case -1 -> "(";
            case -2 -> ")";
            case -3 -> "¬";
            case -4 -> " → ";
            case -5 -> " ↔ ";
            case -6 -> " ∧ ";
            case -7 -> " ∨ ";
            case -8 -> "∀";  // for first-order logic
            case -9 -> "∃";  // for first-order logic
            default -> throw new IllegalArgumentException("Invalid expression type: " + type);
        };
    }

}
