module irie.composer.components.mainsplit;

import irie.composer.common;

public class MainSplit : Component {
    Component left;
    Component right;

    float diviserPosition;
    Dimension diviser;

    const int diviserWidth = 3;

    public this(Component left, float defaultDivisorPosition, Component right) {
        this.left  = left;
        this.right = right;
        this.diviserPosition = defaultDivisorPosition;

        this.background = BLACK;
    }

    public override void Update() {
        left.Update();
        right.Update();
        
        this.dimension = 
            Dimension(
                IrieComposer.Get.tray.GetWidth(),
                IrieComposer.Get.menuBar.GetHeight()
            );
        this.dimension.width  = GetScreenWidth() - this.dimension.x;
        this.dimension.height = GetScreenHeight() - this.dimension.y;

        // calculate dimensions of both components
        float width = this.dimension.width;
        float height = this.dimension.height;

        float leftWidth = width * diviserPosition;
        float rightWidth = width - leftWidth;

        leftWidth  -= diviserWidth;
     //   rightWidth -= diviserWidth;

        left.dimension  = Dimension(this.GetX(), this.GetY(), cast(int)leftWidth, this.GetHeight());
        right.dimension = Dimension(cast(int)(this.GetX() + leftWidth + diviserWidth),
                                    this.GetY(), cast(int)rightWidth, this.GetHeight());

        Vector2 mousePos = GetMousePosition();

        diviser = Dimension(left.GetWidth() + left.GetX(), this.GetY(),
                      diviserWidth, this.GetHeight());

        Dimension localDimension = Dimension(
            diviser.x - (diviserWidth * 2), 
            diviser.y,
            diviser.width + (diviserWidth * 4),
            diviser.height
        );

        //Sys.Printfln("MainSplit","%f-%f", mousePos.x, mousePos.y);
        if (!IsMouseButtonDown(MOUSE_BUTTON_LEFT) ||
            !PointInsideDimension(mousePos.x, mousePos.y, localDimension))
            return;
        float xMousePos = mousePos.x - GetX();
        diviserPosition = xMousePos / cast(float) this.dimension.width;
        if (diviserPosition < 0) 
            diviserPosition = diviserWidth;
    }

    public override void Draw() {
        super.Draw();
        left.Draw();
        right.Draw();

        DrawRectangle(diviser.x, diviser.y, diviser.width, diviser.height, Theme.GetColor("diviser"));
    }
}