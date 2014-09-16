package de.devisnik.sliding;

public interface IFrameListener {
    void handleSwap(IPiece left, IPiece right);
    void handleShifting(ShiftingEvent[] event);
}
