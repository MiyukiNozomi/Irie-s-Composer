module irie.composer.util.math;

public class Math {
    private this() {}

    // returns the minimum value between 2 values
    template Min(T) {
        static T Min(T a, T b) {
            return a > b ? b : a;
        }
    }

    // returns the maximum value between 2 values.
    template Max(T) {
        static T Max(T a, T b) {
            return a < b ? b : a;
        }
    }

    template Clamp(T)
    {
        static T Clamp(T min, T max, T a) {
            if (a < min)return min;
            if (a > max)return max;
            return a;
        }
    }
}