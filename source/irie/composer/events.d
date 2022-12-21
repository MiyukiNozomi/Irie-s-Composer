module irie.composer.events;

import core.thread;
import bindbc.raylib;

import irie.composer.common;

// A copy of Window's MSG system, but simpler

/**
    this system has been deprecated, i have to redevelop it.
*/

struct MouseInformation {
    Vector2 mousePosition;
    int button;
}

interface MouseListener {
    /**fires on a single click*/
    void MouseClicked(MouseInformation information);
    /**fires when continuelly pressed*/
    void MousePressed(MouseInformation information); 
}

class EventSystem : Thread {
    shared bool fireClick;
    shared bool firePressed;
    shared MouseInformation data;

    shared bool canRun = true;

    public this() {
        super(&run);
    }

    private void run() {
        while (canRun) {
            data = MouseInformation(GetMousePosition());
            if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
                fireClick = true;
                data.button = MOUSE_BUTTON_LEFT;
            }
            if (IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
                fireClick = true;
                data.button = MOUSE_BUTTON_RIGHT;
            }
            if (IsMouseButtonDown(MOUSE_BUTTON_LEFT)) {
                firePressed = true;
                data.button = MOUSE_BUTTON_LEFT;
            }
            if (IsMouseButtonDown(MOUSE_BUTTON_RIGHT)) {
                firePressed = true;
                data.button = MOUSE_BUTTON_RIGHT;
            }
        }
    }
}