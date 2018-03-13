package dz.medbenchohra.sudolver;

public class Main {

    public static void main(String[] args) {

//        int[][] matrix = ExamplePacks.EasyPack.getPuzzle(1);
        int[][] matrix = ExamplePacks.MediumPack.getPuzzle(1);

        int[][] solutionMatrix = Core.solveSudoku(matrix);

        System.out.println("\n\n  The Puzzle Matrix");
        Graphic.printMatrix(matrix);
        System.out.println("\n\n  The Solution Matrix");
        Graphic.printMatrix(solutionMatrix);
    }
}
