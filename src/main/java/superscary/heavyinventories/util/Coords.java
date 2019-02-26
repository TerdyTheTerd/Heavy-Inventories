package superscary.heavyinventories.util;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class Coords
{

    private double x;
    private double y;
    private double z;

    public Coords(EnderTeleportEvent event)
    {
        this.x = event.getTargetX();
        this.y = event.getTargetY();
        this.z = event.getTargetZ();
    }

    public Coords(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}
