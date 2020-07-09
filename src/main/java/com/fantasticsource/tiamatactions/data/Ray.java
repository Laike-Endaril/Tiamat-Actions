package com.fantasticsource.tiamatactions.data;

import net.minecraft.util.math.Vec3d;

public class Ray
{
    public Vec3d origin, direction;

    public Ray(Vec3d origin, Vec3d direction)
    {
        this.origin = origin;
        this.direction = direction;
    }
}
