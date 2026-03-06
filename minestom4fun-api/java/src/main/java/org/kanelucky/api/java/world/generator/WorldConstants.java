package org.kanelucky.api.java.world.generator;

/**
 * @author Kanelucky
 */
public final class WorldConstants {
    public static final int SEA_LEVEL = 64;
    public static final int SURFACE_OFFSET = 30;
    public static final int BASE_HEIGHT = 63;

    public static final int WATER_LEVEL = 62;
    public static final double RIVER_THRESHOLD = 0.045;
    public static final int RIVER_DEPTH = 4;

    public static final double[] FREQ = {0.003, 0.01, 0.05};
    public static final double[] AMP = {35.0, 12.0, 4.0};

    public static final double TREE_CHANCE = 0.0025;

    public static final double SEAGRASS_CHANCE = 0.18;
    public static final double TALL_SEAGRASS_CHANCE = 0.35;
    public static final int MIN_WATER_DEPTH = 3;

    public static final double CAVE_THRESHOLD = 0.50;
    public static final double TUNNEL_THRESHOLD = 0.62;
    public static final int CAVE_MIN_Y = 8;
    public static final int CAVE_SURFACE_BUFFER = 8;

    private WorldConstants() {}
}