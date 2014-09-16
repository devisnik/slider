/*
 * Created on 23.07.2006 by leck
 *
 */
package de.devisnik.sliding;

public class ShiftingEvent {
    
    private final IPiece itsPiece;
    private final Point itsOldPosition;
    private final Point itsNewPosition;

    public ShiftingEvent(IPiece piece, Point oldPosition, Point newPosition) {
        itsPiece = piece;
        itsOldPosition = oldPosition;
        itsNewPosition = newPosition;
    }
       
    public IPiece getPiece() {
        return itsPiece;
    }
       
    public Point getOldPosition() {
        return itsOldPosition;
    }
       
    public Point getNewPosition() {
        return itsNewPosition;
    }
}
