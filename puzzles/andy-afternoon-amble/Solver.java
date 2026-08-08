import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tetrahedron truncated using a A4 group
 * faces : 0, 1, 2, 3 and home = 0
 */
public class Solver {

    public static final List<Integer> uTurn = Arrays.asList(1, 0, 3, 2);
    public static final List<Integer> lTurn = Arrays.asList(2, 0, 1, 3);
    public static final List<Integer> rTurn = Arrays.asList(3, 0, 2, 1);

    public static void main(String[] args) {
        
    }

    public static List<Integer> turnPerPermutationRule(List<Integer> state, List<Integer> turnPerm) {
        List<Integer> result = new ArrayList<>(4);

        for (int i=0; i < 4; i++) {
            result.add(state.get(turnPerm.get(i)));
        }

        return result;
    }

    public static void moveSphere(){
        return;
    }
}
