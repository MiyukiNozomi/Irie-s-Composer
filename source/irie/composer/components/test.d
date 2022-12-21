module irie.composer.components.test;

import irie.composer.common;

public class TestComponent : Component {

    const auto fontSize = 15;

    public this(Color color) {
        this.background = color;
    }

    override void Draw() {
        super.Draw();
        int textWidth = MeasureText("Feature Not Implemented", fontSize) / 2;
        int xOffset = cast(int)((this.dimension.width / 2.5) - textWidth);

        int x = this.dimension.x + xOffset;
        int y = this.dimension.y + (this.dimension.height / 2);

        DrawText("Feature Not Implemented\n",
            x, y, fontSize,
            BLACK);
        DrawText("Ask MiyukiNozomi#6838",
            x, y + fontSize, fontSize,
            BLACK);
        DrawText("in case you believe this",
            x, y + fontSize * 2, fontSize,
            BLACK);
        DrawText("was a mistake.",
            x, y + fontSize * 3, fontSize,
            BLACK);
    }

}