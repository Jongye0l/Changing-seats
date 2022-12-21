package com.Jongyeol.ChangeSeat;

public class SeatSelectListener {
    private Seat selectedSeat;
    public void select(Seat seat) {
        if(selectedSeat == null) select1(seat);
        else if(selectedSeat == seat) select2(seat);
        else select3(seat);
    }
    private void select1(Seat seat) {
        selectedSeat = seat;
        seat.selected = true;
    }
    private void select2(Seat seat) {
        seat.selected = false;
        selectedSeat = null;
    }
    private void select3(Seat seat) {
        selectedSeat.selected = false;
        String temp = selectedSeat.name;
        selectedSeat.name = seat.name;
        seat.name = temp;
        selectedSeat = null;
    }
}
