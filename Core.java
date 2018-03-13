package dz.medbenchohra.sudolver;

import java.util.*;

public class Core {

    public static int[][] solveSudoku(int[][] matrix)  {

        int[][] solutionMatrix;
        TreeMap<Couple,HashSet<Integer>> draft;

        solutionMatrix = updateFinalMatrix(matrix);
        draft = fillDraft(matrix);
        eliminateDuplicates(draft);
        addUnaryDraftCellsToTheFinalMatrix(solutionMatrix, draft, extractUnaryDraftCells(draft));

        return solutionMatrix;
    }

    private static void eliminateDuplicates(TreeMap<Couple, HashSet<Integer>> draft) {
        HashSet<Integer> elementsList;
        Integer unaryItem;
        int numberOfIterations = extractUnaryDraftCells(draft).size();

        for (int i = 0; i < numberOfIterations; i++) {
            System.out.println(" i == " + i + " ; noi ==  " + numberOfIterations);
            for (Couple couple : draft.keySet()) {
                elementsList = draft.get(couple);
                if (elementsList.size() == 1) {  // the current cell is unary
                    unaryItem = (Integer) elementsList.toArray()[0];
                    numberOfIterations += clearCorrespondentRowsAndColumns(unaryItem, couple, draft);
                    numberOfIterations += clearCorrespondentSubMatrix(unaryItem, couple, draft);
                }
            }
        }

    }

    // returns the number of the resulted new unary cells
    private static int clearCorrespondentRowsAndColumns(Integer unaryItem, Couple couple, TreeMap<Couple, HashSet<Integer>> draft) {
        int row = couple.i;
        int column = couple.j;
        HashSet<Integer> currentCouple;
        int numberNewUnaryCells = 0;

        for (int k = 0; k < 9; k++) {
            currentCouple = draft.get(new Couple(row, k));
            if (currentCouple != null  &&  k != column) {
                if (currentCouple.remove(unaryItem) && currentCouple.size() == 1)
                    numberNewUnaryCells++;
            }
            currentCouple = draft.get(new Couple(k, column));
            if (currentCouple != null  &&  k != row) {
                if (currentCouple.remove(unaryItem) && currentCouple.size() == 1)
                    numberNewUnaryCells++;
            }
        }

        return numberNewUnaryCells;
    }

    // returns the number of the resulted new unary cells
    private static int clearCorrespondentSubMatrix(Integer unaryItem, Couple couple, TreeMap<Couple, HashSet<Integer>> draft) {
        int row = couple.i;
        int column = couple.j;
        Couple subMatrixTopLeftCorner = new Couple(couple.i-couple.i%3,couple.j-couple.j%3);
        int subMatrixTopLeftCornerRow = subMatrixTopLeftCorner.i;
        int subMatrixTopLeftCornerColumn = subMatrixTopLeftCorner.j;
        HashSet<Integer> currentCouple;
        int numberNewUnaryCells = 0;


        for (int i = subMatrixTopLeftCornerRow; i < subMatrixTopLeftCornerRow + 3; i++) {
            for (int j = subMatrixTopLeftCornerColumn; j < subMatrixTopLeftCornerColumn + 3; j++) {
                currentCouple = draft.get(new Couple(i, j));
                if (currentCouple != null  &&  i != row  &&  j != column)
                    if (currentCouple.remove(unaryItem) && currentCouple.size() == 1)
                        numberNewUnaryCells++;
            }
        }

        return numberNewUnaryCells;
    }

    private static LinkedList<Couple> extractUnaryDraftCells(TreeMap<Couple, HashSet<Integer>> draft) {
        LinkedList<Couple> unaryDraftCells = new LinkedList<>();

        for (Couple couple : draft.keySet()) {
            if (draft.get(couple).size() == 1) {
                unaryDraftCells.add(couple);
            }
        }

        return unaryDraftCells;
    }

    private static void addUnaryDraftCellsToTheFinalMatrix (int[][] matrix, TreeMap<Couple, HashSet<Integer>> draft, LinkedList<Couple> unaryDraftCells) {
        for (Couple couple : draft.keySet()) {
            if (draft.get(couple).size() == 1) {
                matrix[couple.i][couple.j] = (int)draft.get(couple).toArray()[0];
                unaryDraftCells.add(couple);
            }
        }
    }

    private static int[][] updateFinalMatrix(int[][] matrix) { // fill the final matrix with the already found solution
        int[][]finalMatrix = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(matrix[i][j] != 0)
                    finalMatrix[i][j] = matrix[i][j];
            }
        }
        return finalMatrix;
    }

    private static TreeMap<Couple, HashSet<Integer>> fillDraft(int[][] matrix) {
        Couple couple;
        TreeMap<Couple, HashSet<Integer>> draft = new TreeMap<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                couple = new Couple(i,j);
                if(isBlank(couple,matrix))
                    draft.put(couple,fillCellPossibilities(couple,matrix));
            }
        }

        return draft;
    }

    private static HashSet<Integer> fillCellPossibilities(Couple couple, int[][] matrix) {
        HashSet<Integer> possibilities = new HashSet<>();
        HashSet<Integer> existing = new HashSet<>();
        int[][] currentSubMatrix = subMatrixOf(couple,matrix);


        for (int i = 0; i < 3; i++) {           //check the current sub-matrix
            for (int j = 0; j < 3; j++) {
                existing.add(currentSubMatrix[i][j]);
            }
        }

        for (int k = 0; k < 9; k++) {
            existing.add(matrix[k][couple.j]);  // check the column
            existing.add(matrix[couple.i][k]);  // check the row
        }

        for (int i = 1; i <= 9; i++) {  // fill the possibilities (complimentary of existing)
            if (!existing.contains(i))
                possibilities.add(i);
        }

        return possibilities;
    }

    private static int[][] subMatrixOf(Couple couple,int[][] matrix) {
        int[][] subMatrix = new int[3][3];
        Couple topLeftCouple = new Couple(couple.i-couple.i%3,couple.j-couple.j%3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                subMatrix[i][j] = matrix[topLeftCouple.i+i][topLeftCouple.j+j];
            }
        }

        return subMatrix;
    }

    private static boolean isBlank(Couple couple,int[][] matrix) {  // tells wether this cell is blank (== 0)
        return (matrix[couple.i][couple.j] == 0);
    }


    private static class Couple implements Comparable {

        int i;
        int j;

        public Couple(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "(" + (i+1) + "," + (j+1) + ")";
        }

        @Override
        public boolean equals(Object obj) {
            Couple c = (Couple) obj;
            return (this.i == c.i  &&  this.j == c.j);
        }

        @Override
        public int compareTo(Object o) {
            Couple couple = (Couple) o;
            if (couple.i < this.i)
                return 1;
            else if (couple.i > this.i)
                return -1;
            else {
                if (couple.j < this.j)
                    return 1;
                else if (couple.j > this.j)
                    return -1;
                else
                    return 0;
            }
        }
    }

}
