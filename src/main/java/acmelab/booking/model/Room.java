package acmelab.booking.model;

import lombok.Getter;

@Getter
public enum Room {
    MAIN_CONFERENCE_ROOM("Main conference room"),
    STRATEGY_ROOM("Strategy room"),
    DEVELOPMENT_ROOM("Development room");

    private final String roomName;

    Room(String roomName) {
        this.roomName = roomName;
    }

    public static Room fromString(String roomName) {
        for (Room room : Room.values()) {
            if (room.roomName.equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        throw new IllegalArgumentException("Unknown room name: " + roomName);
    }

}
