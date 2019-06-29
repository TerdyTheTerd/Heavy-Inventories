package superscary.heavyinventories.client.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.client.event.ClientEventHandler;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.util.EnumTagID;
import superscary.heavyinventories.util.Logger;
import superscary.heavyinventories.util.PlayerHelper;
import superscary.heavyinventories.util.Toolkit;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static superscary.heavyinventories.util.Constants.MODID;

public class ItemUpgradeToken extends Item
{

    public ItemUpgradeToken(String name)
    {
        super();
        this.setUnlocalizedName(name);
        this.setRegistryName(MODID, name);
        this.setCreativeTab(HeavyInventories.TAB);
        this.setMaxStackSize(16);

        HIItemRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        Random random = new Random();
        double upgrade = Toolkit.roundDouble(1 + (10 - 1) * random.nextDouble());

        PlayerHelper helper = new PlayerHelper(playerIn);
        IWeighable weighable = helper.getWeightCapability();
        IOffset offset = helper.getWeightOffsetCapability();
        offset.setOffset(offset.getOffset() + upgrade);

        if (helper.getEntityData().hasKey(EnumTagID.WEIGHT.getId()))
        {
            weighable.setMaxWeight(weighable.getMaxWeight() + upgrade);
            helper.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), weighable.getMaxWeight());
            ClientEventHandler.setPlayerWeight(weighable.getMaxWeight());

            if (worldIn.isRemote) helper.sendToast(new TextComponentTranslation("hi.splash.upgradeToken", upgrade));
            Logger.log(Level.INFO, "Updated player weight from %s to %s by %s", Toolkit.roundDouble(weighable.getMaxWeight()), Toolkit.roundDouble(weighable.getMaxWeight() + upgrade), upgrade);

            stack.shrink(1);

            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("Upgrades the player's max carry weight");
    }

}
