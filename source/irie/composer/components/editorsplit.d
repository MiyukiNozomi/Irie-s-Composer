module irie.composer.components.editorsplit;

import irie.composer.common;

import irie.composer.components.test;
import irie.composer.components.tabpanel;

public class EditorSplit : Component {

    const auto fontSize = 15;

    TabPanel top;
    Component bottom;

    float diviserPosition = 0.7;
    Dimension diviser;

    const int diviserWidth = 3;

    public this() {
        top = new TabPanel();
        bottom = new TestComponent(GREEN);
        this.background = BLACK;
    }

    override void Update() {
        top.Update();
        bottom.Update();

        diviserPosition = Math.Clamp!float(0, 1.0, diviserPosition);

        // calculate dimensions of both components
        float height = this.dimension.height;

        float topHeight = height * diviserPosition;
        float bottomHeight = height - topHeight;

        topHeight -= diviserWidth;
     //   bottomHeight -= diviserWidth;

        top.dimension  = Dimension(this.GetX(), this.GetY(), this.GetWidth(), cast(int)topHeight);
        bottom.dimension = Dimension(this.GetX(), cast(int)(this.GetY() + topHeight + diviserWidth),
                                    this.GetWidth(), cast(int)bottomHeight);

        Vector2 mousePos = GetMousePosition();

        diviser = Dimension(this.GetX(), top.GetHeight() + top.GetY(),
                      this.GetWidth(), diviserWidth);

        Dimension localDimension = Dimension( 
            diviser.x,
            diviser.y - (diviserWidth * 2),
            diviser.width,
            diviser.height + (diviserWidth * 4)
        );

        if (!IsMouseButtonDown(MOUSE_BUTTON_LEFT) ||
            !PointInsideDimension(mousePos.x, mousePos.y, localDimension))
            return;
        float yMousePos = mousePos.y - GetY();
        diviserPosition = yMousePos / cast(float) this.dimension.height;
        if (diviserPosition < 0) 
            diviserPosition = diviserWidth;

        top.Update();
        bottom.Update();
    }

    override void Draw() {
        super.Draw();

        top.Draw();
        bottom.Draw();

        DrawRectangle(diviser.x, diviser.y, diviser.width, diviser.height, Theme.GetColor("diviser"));
    }

}