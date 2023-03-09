package model;

import java.io.Serializable;
import java.util.*;

/** An immutable representation of a tetris piece in a particular rotation.
 *  Each piece is defined by the blocks that make up its body.
 *
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisPiece implements Serializable {

    /*
     Implementation notes:
     -The starter code does a few simple things
     -It stores the piece body as a TetrisPoint[] array
     -The attributes in the TetrisPoint class are x and y coordinates
     -Don't assume there are 4 points in the piece body; there might be less or more!
    */
    private TetrisPoint[] body; // y and x values that make up the body of the piece.
    private int[] lowestYVals; //The lowestYVals array contains the lowest y value for each x in the body.
    private int width;
    private int height;
    public TetrisPiece next;
//    private TetrisPiece next; // We'll use this to link each piece to its "next" rotation.
    static private TetrisPiece[] pieces;	// array of rotations for this piece


    // String constants for the standard 7 tetris pieces
    public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
    public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
    public static final String L2_STR		= "0 0	1 0  1 1  1 2";
    public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
    public static final String S2_STR		= "0 1	1 1  1 0  2 0";
    public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
    public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";

    /**
     * Defines a new piece given a TetrisPoint[] array that makes up its body.
     * Makes a copy of the input array and the TetrisPoint inside it.
     * Note that this constructor should define the piece's "lowestYVals". For each x value
     * in the body of a piece, the lowestYVals will contain the lowest y value for that x in the body.
     * This will become useful when computing where the piece will land on the board!!
     */
    public TetrisPiece(TetrisPoint[] points) {
        //your code here!
        this.body = points.clone();
        int[] lst = {-1, -1, -1, -1};
        int len = 0;
        for (TetrisPoint p : this.body){
            if (lst[p.x] == -1) {
                len += 1;
                lst[p.x] = p.y;
            }
            if (lst[p.x] > p.y) {
                lst[p.x] = p.y;
            }
        }

        this.lowestYVals = new int[len];
        for (int i = 0; i < lst.length; i++) {
            if (lst[i] == -1) {break;}
            this.lowestYVals[i] = lst[i];
        }

        this.width = this.lowestYVals.length;
        int maxY = 0;
        for (TetrisPoint p : this.body){
            if(p.y > maxY) {
                maxY = p.y;
            }
        }
        this.height = maxY + 1;
    }

    /**
     * Alternate constructor for a piece, takes a String with the x,y body points
     * all separated by spaces, such as "0 0  1 0  2 0  1 1".
     */
    public TetrisPiece(String points) {
        this(parsePoints(points));
    }

    /**
     * Returns the width of the piece measured in blocks.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the piece measured in blocks.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a pointer to the piece's body. The caller
     * should not modify this array.
     *
     * @return point array that defines piece body
     */
    public TetrisPoint[] getBody() {
        return body;
    }

    /**
     * Returns a pointer to the piece's lowest Y values. For each x value
     * across the piece, this gives the lowest y value in the body.
     * This is useful for computing where the piece will land (if dropped).
     * The caller should not modify the values that are returned
     *
     * @return array of integers that define the lowest Y value for every X value of the piece.
     */
    public int[] getLowestYVals() {
        return lowestYVals;
    }

    /**
     * Returns true if two pieces are the same --
     * their bodies contain the same points.
     * Interestingly, this is not the same as having exactly the
     * same body arrays, since the points may not be
     * in the same order in the bodies. Used internally to detect
     * if two rotations are effectively the same.
     *
     * @param obj the object to compare to this
     *
     * @return true if objects are the same
     */
    public boolean equals(Object obj) {
        List thisOne = Arrays.stream(this.getBody().clone()).toList();
        List otherOne = Arrays.stream(((TetrisPiece) obj).getBody().clone()).toList();
        return thisOne.containsAll(otherOne);
    }

    /**
     * This is a static method that will return all rotations of
     * each of the 7 standard tetris pieces:
     * STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
     * The next (counterclockwise) rotation can be obtained
     * from each piece with the fastRotation() method.
     * This method will be called by the model to facilitate
     * selection of random pieces to add to the board.
     * The pieces can be easily rotated because the rotations
     * have been precomputed.
     *
     * @return a list of all the rotations for all the given pieces.
     */
    public static TetrisPiece[] getPieces() {
        // lazy evaluation -- create static array only if needed
        if (TetrisPiece.pieces==null) {
            // use makeFastRotations() to compute all the rotations for each piece
            try {
                TetrisPiece.pieces = new TetrisPiece[]{
                        makeFastRotations(new TetrisPiece(STICK_STR)),
                        makeFastRotations(new TetrisPiece(L1_STR)),
                        makeFastRotations(new TetrisPiece(L2_STR)),
                        makeFastRotations(new TetrisPiece(S1_STR)),
                        makeFastRotations(new TetrisPiece(S2_STR)),
                        makeFastRotations(new TetrisPiece(SQUARE_STR)),
                        makeFastRotations(new TetrisPiece(PYRAMID_STR)),
                };
            } catch (UnsupportedOperationException e) {
                System.exit(1);
            }
        }
        return TetrisPiece.pieces;
    }

    /**
     * Returns a pre-computed piece that is 90 degrees counter-clockwise
     * rotated from the receiver. Fast because the piece is pre-computed.
     * This only works on pieces set up by makeFastRotations(), and otherwise
     * just returns null.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece fastRotation() {
        return next;
    }

    /**
     * Given the "first" root rotation of a piece, computes all
     * the other rotations and links them all together
     * in a circular list. The list should loop back to the root as soon
     * as possible.
     * Return the root piece. makeFastRotations() will need to relies on the
     * pointer structure setup here. The suggested implementation strategy
     * is to use the subroutine computeNextRotation() to generate 90 degree rotations.
     * Use this to generate a sequence of rotations and link them together.
     * Continue generating rotations until you generate the piece you started with.
     * Use Piece.equals() to detect when the rotations
     * have gotten you back to the first piece.
     *
     * @param root the default rotation for a piece
     *
     * @return a piece that is a linked list containing all rotations for the piece
     */
    public static TetrisPiece makeFastRotations(TetrisPiece root) {
        TetrisPiece curr = root;
        while (! root.equals(curr.computeNextRotation())) {
            curr.next = curr.computeNextRotation();
            curr = curr.next;
        }
        curr.next = root;
        return root;
    }

    /**
     * Returns a new piece that is 90 degrees counter-clockwise
     * rotated from the receiver.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece computeNextRotation() {
        ArrayList<TetrisPoint> res = new ArrayList<>();
        boolean is_4x4_matrix = false;

        TetrisPoint test_point = this.body[0];
        int test_a = test_point.x;

        // check input matrix is 3x3 or 4x4 -> is_4x4_matrix
        for (TetrisPoint point : this.body) {
            if (point.x == 3 || point.y == 3){
                is_4x4_matrix = true;
                break;
            }
        }

        List<int[]> edgePoints;
        List<int[]> upMiddleEdgePoints;
        List<int[]> bottomMiddleEdgePoints;
        List<int[]> centerPoints;
        List<int[]> allPoints;

        if (! is_4x4_matrix){ // 3x3
            //list of corner point,
            edgePoints = Arrays.asList(new int[][] {{0, 0}, {2, 0}, {2, 2}, {0, 2}, {0, 0}});
            upMiddleEdgePoints = Arrays.asList(new int[][] {{1, 0}, {2, 1}, {1, 2}, {0, 1}, {1, 0}});
            centerPoints = Arrays.asList(new int[][] {{1, 1}, {1, 1}});

            allPoints = new ArrayList<>(edgePoints);
            allPoints.addAll(upMiddleEdgePoints);
            allPoints.addAll(centerPoints);
        } else{ // 4x4
            edgePoints = Arrays.asList(new int[][] {{0, 0}, {0, 3}, {3, 3}, {3, 0}, {0, 0}});
            upMiddleEdgePoints = Arrays.asList(new int[][] {{0, 2}, {2, 3}, {3, 1}, {1, 0}, {0, 2}});
            bottomMiddleEdgePoints = Arrays.asList(new int[][] {{0, 1}, {1, 3}, {3, 2}, {2, 0}, {0, 1}});
            centerPoints = Arrays.asList(new int[][] {{1, 1}, {1, 2}, {2, 2}, {2, 1}});

            allPoints = new ArrayList<>(edgePoints);
            allPoints.addAll(upMiddleEdgePoints);
            allPoints.addAll(bottomMiddleEdgePoints);
            allPoints.addAll(centerPoints);
        }

        for (TetrisPoint point : this.body) {
            int[] p_before_rotate = {point.x, point.y};
            for (int i = 0; i < allPoints.size(); i++){

                if (p_before_rotate[0] == allPoints.get(i)[0] && p_before_rotate[1] == allPoints.get(i)[1]) {
                    int[] p_after_rotate = allPoints.get(i + 1);
                    res.add(new TetrisPoint(p_after_rotate[0], p_after_rotate[1]));
                    break;
                }
            }
        }


        // move piece most left and bottom
        boolean atBottom = false;
        boolean atLeft = false;
        while (!atBottom || !atLeft) {
            // any x is 0, is very left; any y = 0, piece at bottom;
            for (TetrisPoint p : res) {
                if (p.x == 0) {
                    atLeft = true;
                }
                if (p.y == 0) {
                    atBottom = true;
                }
                if (atBottom && atLeft) {
                    break;
                }
            }

            // move left, x -= 1
            if (!atLeft) {
                for (TetrisPoint p : res){
                    p.x -= 1;
                }
            }

            // move down, y -= 1
            if (!atBottom) {
                for (TetrisPoint p : res){
                    p.y -= 1;
                }
            }
        }
        TetrisPoint[] finalResult = new TetrisPoint[] {res.get(0), res.get(1), res.get(2), res.get(3)};
        return new TetrisPiece(finalResult);
    }

    /**
     * Print the points within the piece
     *
     * @return a string representation of the piece
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < body.length; i++) {
            str += body[i].toString();
        }
        return str;
    }

    /**
     * Given a string of x,y pairs (e.g. "0 0 0 1 0 2 1 0"), parses
     * the points into a TPoint[] array.
     *
     * @param string input of x,y pairs
     *
     * @return an array of parsed points
     */
    private static TetrisPoint[] parsePoints(String string) {
        List<TetrisPoint> points = new ArrayList<TetrisPoint>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new TetrisPoint(x, y));
            }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }
        // Make an array out of the collection
        TetrisPoint[] array = points.toArray(new TetrisPoint[0]);
        return array;
    }
}
