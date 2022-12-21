module irie.composer.components.component;

import bindbc.raylib;

import irie.composer.events;

public struct Dimension {
    int x;
    int y;
    int width;
    int height;
}

bool PointInsideDimension(float x, float y, Dimension D) {
    float vWidth = D.x + D.width;
    float vHeight = D.y + D.height;

    return x >= D.x && y >= D.y &&
           x <= vWidth && y <= vHeight;
}

bool DimensionInsideDimension(Dimension D1, Dimension D2) {
    float vWidth1 = D1.x + D1.width;
    float vHeight1 = D1.y + D1.height;
    float vWidth2 = D2.x + D2.width;
    float vHeight2 = D2.y + D2.height;
    //Finish implementing me!
    return false;
}

class Component {
    public Dimension dimension;
    public Color background;
    public Color foreground;

    void Update() {}

    void Draw() {
        DrawRectangle(dimension.x, dimension.y, dimension.width, dimension.height, this.background);
    }

    auto GetX(){return dimension.x;}
    auto GetY(){return dimension.y;}
    auto GetWidth(){return dimension.width;}
    auto GetHeight(){return dimension.height;}
}