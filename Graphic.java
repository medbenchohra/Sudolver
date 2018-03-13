package dz.medbenchohra.sudolver;

import java.util.HashSet;
import java.util.Set;

public class Graphic {
    public static void printMatrix(int[][] matrix) {
        System.out.println("\n -----------------------\n"
                + deepToString(matrix)
                + "\n -----------------------\n");
    }

    public static String deepToString(Object[] a) {
        if (a == null)
            return "null";

        int bufLen = 20 * a.length;
        if (a.length != 0 && bufLen <= 0)
            bufLen = Integer.MAX_VALUE;
        StringBuilder buf = new StringBuilder(bufLen);
        deepToString(a, buf, new HashSet<>());
        return buf.toString();
    }

    private static void deepToString(Object[] a, StringBuilder buf, Set<Object[]> dejaVu) {
        if (a == null) {
            buf.append("null");
            return;
        }
        int iMax = a.length - 1;
        if (iMax == -1) {
            buf.append("[]");
            return;
        }

        dejaVu.add(a);
        for (int i = 0; ; i++) {

            buf.append("|");
            Object element = a[i];
            if (element == null) {
                buf.append("null");
            } else {
                Class<?> eClass = element.getClass();

                if (eClass.isArray()) {
                    if (eClass == int[].class)
                        buf.append(toString((int[]) element) + "|");
                    else { // element is an array of object references
                        if (dejaVu.contains(element))
                            buf.append("[...]");
                        else
                            deepToString((Object[]) element, buf, dejaVu);
                    }
                } else {  // element is non-null and not an array
                    buf.append(element.toString());
                }
            }
            if (i == iMax)
                break;
            buf.append("\n");
            if (i % 3 == 2)
                buf.append("|                       |\n");
        }
        dejaVu.remove(a);
    }

    public static String toString(int[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append(' ');
        for (int i = 0; ; i++) {
            if (a[i] == 0) {
                b.append("-");
            } else {
                b.append(a[i]);
            }
            if (i == iMax)
                return b.append(" ").toString();
            b.append(" ");
            if (i % 3 == 2)
                b.append("  ");
        }
    }
}
