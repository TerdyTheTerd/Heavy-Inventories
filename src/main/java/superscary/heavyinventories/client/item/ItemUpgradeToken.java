package superscary.heavyinventories.client.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import superscary.heavyinventories.HeavyInventories;
import superscary.heavyinventories.client.event.ClientEventHandler;
import superscary.heavyinventories.common.capability.offsets.IOffset;
import superscary.heavyinventories.common.capability.weight.IWeighable;
import superscary.heavyinventories.util.EnumTagID;
import superscary.heavyinventories.util.PlayerHelper;

import javax.annotation.Nullable;
import java.util.List;

import static superscary.heavyinventories.util.Constants.MODID;

public class ItemUpgradeToken extends Item
{

    private double upgradeAmount;

    public ItemUpgradeToken(String name, double upgradeAmount)
    {
        super();
        this.setUnlocalizedName(name);
        this.setRegistryName(MODID, name);
        this.setCreativeTab(HeavyInventories.TAB);
        this.setMaxStackSize(16);
        this.upgradeAmount = upgradeAmount;

        HIItemRegistry.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        PlayerHelper helper = new PlayerHelper(playerIn);
        IOffset offset = helper.getWeightOffsetCapability();
        offset.setOffset(offset.getOffset() + getUpgradeAmount());

        IWeighable weighable = helper.getWeightCapability();
        if (helper.getEntityData().hasKey(EnumTagID.WEIGHT.getId()))
        {
            weighable.setMaxWeight(weighable.getMaxWeight() + getUpgradeAmount());
            helper.getEntityData().setDouble(EnumTagID.WEIGHT.getId(), weighable.getMaxWeight());
            ClientEventHandler.setPlayerWeight(weighable.getMaxWeight());

            if (worldIn.isRemote) helper.sendToast(new TextComponentTranslation("hi.splash.upgradeToken", this.upgradeAmount));

            stack.shrink(1);

            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        PlayerHelper helper = new PlayerHelper(player);
        helper.setOffset(helper.getWeightOffsetCapability().getOffset() + upgradeAmount);

        return EnumActionResult.PASS;
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

    public double getUpgradeAmount()
    {
        return upgradeAmount;
    }
}
