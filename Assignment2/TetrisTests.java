import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.*;

import org.junit.jupiter.api.Test;
import views.LoadView;
import views.TetrisView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TetrisTests {

    @Test
    void testTetrisPiece2() {
        TetrisPiece a = new TetrisPiece(TetrisPiece.L1_STR);
        TetrisPiece b = new TetrisPiece(TetrisPiece.L2_STR);
        System.out.println(a.equals(b));
//
//        int[][] a = new int[10][24];
//        a[10][24] = 7;
        //        TetrisModel model = new TetrisModel();
//        model.executeMove(TetrisModel.MoveType.DOWN);
//        TetrisPiece[] pieces = TetrisPiece.getPieces();
//        TetrisPiece piece = pieces[1];
//        System.out.println(piece.next);
//        piece = piece.next;
//        System.out.println(piece.next);
//        piece = piece.next;
//        System.out.println(piece.next);
//        piece = piece.next;
//        System.out.println(piece.next);
//        piece = piece.next;
//        System.out.println(piece.next);



//        STICK_STR -----------
//        L1_STR  ------------
//        L2_STR
//        S1_STR
//        S2_STR
//        SQUARE_STR
//        PYRAMID_STR -------
//        TetrisPiece piece = new TetrisPiece(TetrisPiece.PYRAMID_STR);
//        System.out.println(piece);
//        piece = piece.computeNextRotation();
//        System.out.println(piece);
//        piece = piece.computeNextRotation();
//        System.out.println(piece);
//        piece = piece.computeNextRotation();
//        System.out.println(piece);
//        piece = piece.computeNextRotation();
//        System.out.println(piece);
    }

    @Test
    void testTetrisPiece1() {
//        HashMap<Integer, Integer> dic = new HashMap<Integer, Integer>();
//        dic.put(1,1);
//        System.out.println(dic.get(10) != null);

        TetrisPoint p1 = new TetrisPoint(0, 0);
        TetrisPoint p2 = new TetrisPoint(1, 0);
        TetrisPoint p3 = new TetrisPoint(2, 0);
        TetrisPoint p4 = new TetrisPoint(3, 0);
        TetrisPoint[] t1 = {p1, p2, p3, p4};

        TetrisPoint p5 = new TetrisPoint(0, 1);
        TetrisPoint p6 = new TetrisPoint(0, 0);
        TetrisPoint p7 = new TetrisPoint(1, 1);
        TetrisPoint p8 = new TetrisPoint(1, 3);
        TetrisPoint[] t2 = {p5, p6, p7, p8};

        TetrisPiece testpiece1 = new TetrisPiece(t1);
        TetrisPiece testpiece2 = new TetrisPiece(t2);
//        piece.
    }

    //Piece tests
    @Test
    void testPiece() {

        TetrisPiece piece = new TetrisPiece("0 0	0 1	 0 2  0 3");
        int [] output = piece.getLowestYVals();
        int [] target = {0};
        for (int i =0; i< output.length; i++) {
            assertEquals(output[i], target[i], "Error when testing lowest Y values");
        }
    }

    @Test
    void testMakeFastRotations() {
        TetrisPiece piece = new TetrisPiece(TetrisPiece.S2_STR);
        piece = TetrisPiece.makeFastRotations(piece);
        String[] target = {"0 0 0 1 1 1 1 2", "0 1 1 0 1 1 2 0", "0 0 0 1 1 1 1 2", "0 1 1 0 1 1 2 0"};
        int counter = 0;
        while(counter < 4){
            TetrisPiece np = new TetrisPiece(target[counter]);
            piece = piece.fastRotation();
            assertTrue(np.equals(piece), "Error when testing piece equality");
            counter++;
        }

    }

    @Test
    void testComputeNextRotation_Eric(){
        TetrisPiece piece = new TetrisPiece(TetrisPiece.S2_STR);
        TetrisPiece res = piece.computeNextRotation();
    }

    @Test
    void testEquals() {

        TetrisPiece pieceA = new TetrisPiece("1 0  1 1  1 2  0 1");
        TetrisPiece pieceB = new TetrisPiece("1 0  1 1  1 2  0 1");
        assertTrue(pieceB.equals(pieceA), "Error when testing piece equality");
        assertTrue(pieceA.equals(pieceB), "Error when testing piece equality");


    }


    @Test
    void testPlacePiece() {

        TetrisBoard board = new TetrisBoard(10,24);
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);

        board.commit();
        int retval = board.placePiece(pieceA, 0,0);
        assertEquals(TetrisBoard.ADD_OK,retval);

        board.commit();
        retval = board.placePiece(pieceA, 12,12); //out of bounds
        assertEquals(TetrisBoard.ADD_OUT_BOUNDS,retval);

        board.commit();
        retval = board.placePiece(pieceA, 0,0);
        assertEquals(TetrisBoard.ADD_BAD, retval);

        //fill the entire row
        retval = board.placePiece(pieceA, 2,0); board.commit();
        retval = board.placePiece(pieceA, 4,0); board.commit();
        retval = board.placePiece(pieceA, 6,0); board.commit();
        retval = board.placePiece(pieceA, 8,0);
        assertEquals(TetrisBoard.ADD_ROW_FILLED, retval);

        for (int i = 0; i < board.getWidth(); i++) {
            assertEquals(board.getGrid(i,0), true);
            assertEquals(board.getGrid(i,1), true);
            assertEquals(board.getGrid(i,2), false);
        }

    }

    @Test
    void testPlacementHeight() {
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);
        TetrisBoard board = new TetrisBoard(10,24); board.commit();
        int retval = board.placePiece(pieceA, 0,0); board.commit();
        int x = board.placementHeight(pieceA, 0);
        assertEquals(2,x);
        retval = board.placePiece(pieceA, 0,2); board.commit();
        x = board.placementHeight(pieceA, 0);
        assertEquals(4,x);

    }

    @Test
    void testClearRows() {
        TetrisBoard board = new TetrisBoard(10,24); board.commit();
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);

        //fill two rows completely
        int retval = board.placePiece(pieceA, 0,0); board.commit();
        retval = board.placePiece(pieceA, 2,0); board.commit();
        retval = board.placePiece(pieceA, 4,0); board.commit();
        retval = board.placePiece(pieceA, 6,0); board.commit();
        retval = board.placePiece(pieceA, 8,0);

        int rcleared = board.clearRows();
        assertEquals(2, rcleared);
    }

//    @Test
//    void testClearRows_2() {
//        int width = 2;
////        boolean[][] tetrisGrid = {{true, true}, {false, false}, {false, false}, {false, false}, {false, false}};
////        int[] rowCounts = {2, 0, 0, 0, 0};
//
//        boolean[][] tetrisGrid = {{true, true}, {true, true}, {false, false}, {false, false}, {false, false}};
//        boolean[] emptyArray = {false, false};
//        ArrayList<boolean[]> tempGrid = new ArrayList<>(Arrays.asList(tetrisGrid));
//        tempGrid.remove(0);
//        tempGrid.add(emptyArray);
//        tetrisGrid = tempGrid.toArray(tetrisGrid);
//        System.out.println(Arrays.toString(tempGrid));
//
//    }

//    @Test
//    void testLoadView() {
//        TetrisModel model = new TetrisModel();
//        Stage stage = new Stage();
//        TetrisView tetrisView = new TetrisView(model, stage);
//        LoadView loadView = new LoadView(tetrisView);
//        javafx.scene.control.ListView<String> boardsList = new ListView<>();
//        loadView.getFiles(boardsList);
//    }

}
