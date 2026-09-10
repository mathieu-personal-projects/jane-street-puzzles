import java.math.BigInteger;

/**
 * The point of this puzzle is to understand if Andy got fooled based on probability :
 * 
 * The trick is this since the tetrahedron is a 3D figure, all hexagons are touching his home
 * so when he goes on the 2D figure (the kitchen floor), only the tiles next to home touches home.
 * 
 * We can wonder that if Andy is going to walk randomly, will he get close to his home (and be fooled/never notice the difference)
 * or, he will land on a tile that isn't next to home and understand the reality ?
 * 
 * Since andy can only go in 3 directions, we can determine these : 
 * p is the probabilty that he gets home, and each number is how far is he from home 
 * p0 = he is really close from home so the probability of him getting fooled is high
 * p3 = he is far from home so the probability of him getting fooled is low
 * 
 * p0 = 1/3 + 2/3 * p1
 * p1 = 1/3 * p0 + 1/3 * p2
 * p2 = 1/3 * p1 + 1/3 * p3
 * p3 = 2/3 * p2 
 * 
 * @author: Mathieu Audibert
 * @license : GPL-3.0
 */
public class Solver {

    static Fraction bf(long n, long d) {
        return new Fraction(n, d);
    }
    public static void main(String[] args) {
        Fraction[][] coeffs = {
            { bf(1,1),  bf(-2,3), bf(0,1),  bf(0,1)  },
            { bf(-1,3), bf(1,1),  bf(-1,3), bf(0,1)  },
            { bf(0,1),  bf(-1,3), bf(1,1),  bf(-1,3) },
            { bf(0,1),  bf(0,1),  bf(-2,3), bf(1,1)  } 
        };

        Fraction[] rhs = { bf(1,3), bf(0,1), bf(0,1), bf(0,1) };
        Fraction[] solution = solve(coeffs, rhs);

        String[] names = {"p0", "p1", "p2", "p3"};
        System.out.println("Solution:");
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + names[i] + " = " + solution[i]);
        }

        Fraction p0 = solution[0];
        Fraction answer = Fraction.ONE.subtract(p0);

        System.out.println("p0 (probability Andy doesn't notice) = " + p0);
        System.out.println("Answer (probability Andy discovers he's not on the truncated tetrahedron) = " + answer);
    }

    static Fraction[] solve(Fraction[][] matrix, Fraction[] rhs) {
        int n = rhs.length;
        Fraction[][] a = new Fraction[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, a[i], 0, n);
            a[i][n] = rhs[i];
        }
        for (int column = 0; column < n; column++) {
            int pivot = column;
            while (a[pivot][column].equals(Fraction.ZERO)) pivot++;
            Fraction[] temp = a[column];
            a[column] = a[pivot];
            a[pivot] = temp;
            Fraction divisor = a[column][column];
            for (int j = column; j <= n; j++) a[column][j] = a[column][j].divide(divisor);
            for (int i = 0; i < n; i++) {
                if (i == column) continue;
                Fraction factor = a[i][column];
                for (int j = column; j <= n; j++) a[i][j] = a[i][j].subtract(factor.multiply(a[column][j]));
            }
        }
        Fraction[] result = new Fraction[n];
        for (int i = 0; i < n; i++) result[i] = a[i][n];
        return result;
    }

    static final class Fraction {
        static final Fraction ZERO = new Fraction(0, 1);
        static final Fraction ONE = new Fraction(1, 1);
        final BigInteger numerator;
        final BigInteger denominator;

        Fraction(long numerator, long denominator) {
            this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }

        Fraction(BigInteger numerator, BigInteger denominator) {
            if (denominator.signum() == 0) throw new ArithmeticException("zero denominator");
            if (denominator.signum() < 0) {
                numerator = numerator.negate();
                denominator = denominator.negate();
            }
            BigInteger gcd = numerator.gcd(denominator);
            this.numerator = numerator.divide(gcd);
            this.denominator = denominator.divide(gcd);
        }

        Fraction add(Fraction other) {
            return new Fraction(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
        }
        Fraction subtract(Fraction other) { return add(other.negate()); }
        Fraction multiply(Fraction other) { return new Fraction(numerator.multiply(other.numerator), denominator.multiply(other.denominator)); }
        Fraction divide(Fraction other) { return multiply(other.reciprocal()); }
        Fraction reciprocal() { return new Fraction(denominator, numerator); }
        Fraction negate() { return new Fraction(numerator.negate(), denominator); }
        public boolean equals(Object value) { return value instanceof Fraction && numerator.equals(((Fraction) value).numerator) && denominator.equals(((Fraction) value).denominator); }
        public int hashCode() { return numerator.hashCode() * 31 + denominator.hashCode(); }
        public String toString() { return denominator.equals(BigInteger.ONE) ? numerator.toString() : numerator + "/" + denominator; }
    }
}
