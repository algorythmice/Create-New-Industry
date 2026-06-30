package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

public final class EnergyUnit {

    public static String format(float joules) {
        if (joules >= 1_000_000f)
            return String.format("%.2f MJ", joules / 1_000_000f);

        if (joules >= 1000f)
            return String.format("%.2f kJ", joules / 1000f);

        return String.format("%.0f J", joules);
    }
}
