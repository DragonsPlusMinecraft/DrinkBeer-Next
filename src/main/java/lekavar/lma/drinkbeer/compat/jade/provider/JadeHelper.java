package lekavar.lma.drinkbeer.compat.jade.provider;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.api.ITooltip;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.*;
import snownee.jade.impl.Tooltip;
import snownee.jade.impl.ui.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JadeHelper {
    public static final IElement ITEM_PLACEHOLDER = ItemStackElement.of(Items.AIR.getDefaultInstance());
    private static final IElement _ARROW = new ProgressArrowElement(1);
    public static final IElement ARROW_PLACEHOLDER = new SpacerElement(_ARROW.getSize());

    public static float getRowHeight(List<IElement> row)
    {
        float rowHeight = 0;
        for (IElement grid : row) {
            rowHeight = Math.max(rowHeight, grid.getCachedSize().y);
        }
        return rowHeight;
    }

    public static float[] getHeightsByRow(List<List<IElement>> grids)
    {
        float[] heights = new float[grids.size()];
        int i = 0;
        for (List<IElement> row : grids) {
            float rowHeight = getRowHeight(row);
            heights[i] = rowHeight;
            ++i;
        }
        return heights;
    }

    public static float getTotalHeight(List<List<IElement>> grids)
    {
        float[] heights = getHeightsByRow(grids);
        float totalHeight = 0;
        for (float height : heights) {
            totalHeight += height;
        }
        return totalHeight;
    }

    public static IElement createCenteredItem(IElementHelper helper, ItemStack stack, float scale, float rowHeight)
    {
        Vec2 standardSize = new Vec2(rowHeight, rowHeight);
        IElement ret = helper.item(stack, scale).size(standardSize);
        ret.translate(standardSize.add(ret.getSize().negated()).scale(0.5F));

        return ret;
    }

    public static IElement createSpacerItem(IElementHelper helper)
    {
        return helper.item(Items.AIR.getDefaultInstance());
    }

    public static IElement createSpacerItem()
    {
        return ItemStackElement.of(Items.AIR.getDefaultInstance());
    }

    public static List<List<IElement>> createRows(int height)
    {
        List<List<IElement>> grids =  new ArrayList<>();
        for (int i = 0; i < height; ++i) {
            grids.add(new ArrayList<>());
        }

        return grids;
    }

    public static IElement[][] layoutItems(List<IElement> itemElements, int itemCount)
    {
        int maxRow = (int) Math.round(Math.sqrt(itemCount));
        int maxCol = (itemCount - 1) / maxRow + 1;
        int gridsCount = maxRow * maxCol;

        IElement[][] grids = new IElement[maxRow][maxCol];

        for (int i = 0; i < gridsCount; ++i) {
            int row = i / maxCol;
            int col = i % maxCol;
            if (i < itemCount) {
                grids[row][col] = itemElements.remove(0);
            }
            else {
                grids[row][col] = ITEM_PLACEHOLDER;
            }
        }

        return grids;
    }

    public static ElementGroup placeItems(List<List<IElement>> grids, IElement[][] items, boolean placeHolder)
    {
        int gridsRows = grids.size();

        int itemsRows = items.length;
        if (itemsRows == 0) return new ElementGroup();

        int itemsCols = items[0].length;
        if (itemsCols == 0) return new ElementGroup();

        float rowHeight = getRowHeight(List.of(items[0]));

        return placeItems(grids, items, gridsRows, itemsRows, itemsCols, rowHeight, placeHolder);
    }

    public static ElementGroup placeItems(List<List<IElement>> grids, IElement[][] items, int gridRows, int itemRows, int itemCols, float rowHeight, boolean placeHolder)
    {
        ElementGroup helper = new ElementGroup();

        float delta = rowHeight * (gridRows - itemRows) / 2;

        IElement placeholder = JadeHelper.createSpacerItem(helper);

        for (int i = 0; i < gridRows; ++i) {
            List<IElement> row = grids.get(i);

            for (int j = 0; j < itemCols; ++j) {
                if (i < itemRows) {
                    row.add(helper.track(items[i][j].translate(new Vec2(0, delta))));
                }
                else if (placeHolder) {
                    // redundant tracking of same element will be automatically ignored
                    row.add(helper.track(placeholder));
                }
                else {
                    break;
                }
            }
        }

        return helper;
    }

    public static ElementGroup placeScaledItem(IElementHelper iHelper, List<List<IElement>> grids, ItemStack stack, float scale)
    {
        ElementGroup helper = new ElementGroup(iHelper);

        int tileCount = grids.size();
        if (tileCount == 1) {
            grids.get(0).add(helper.item(stack, scale));
        }

        float oneGridItemRetio = 1F / tileCount;
        float translateEff = oneGridItemRetio;

        if (scale < oneGridItemRetio) {
            return helper;
        }

        ItemStack iconItem = stack.copy();
        iconItem.setCount(1);

        float totalHeight = getTotalHeight(grids);

        IElement sample = helper.item(stack);
        float baseScale = totalHeight / sample.getSize().y;
        float iconScale = baseScale * scale;

        float tileWidth;
        tileWidth = totalHeight / tileCount;
        Vec2 tileSize = new Vec2(tileWidth, 0);

        //IElement icon = helper.item(iconItem, scale).size(tileSize);
        IElement icon = createCenteredItem(helper, iconItem, iconScale, totalHeight).size(tileSize);
        IElement spacer = helper.spacer(0,0).size(tileSize);
        // TODO: Fix TextElement being shadowed by icon element
        // IElement text = helper.text(new TextComponent(Integer.toString(itemCount)).withStyle(ChatFormatting.WHITE)).size(tileSize);
        IElement text = helper.item(stack, 0.01F).size(tileSize).translate(new Vec2(totalHeight, totalHeight).scale(
                ((oneGridItemRetio - 1) + (scale - oneGridItemRetio) * translateEff) / 2
        ));

        for (int i = 0; i < tileCount; ++i) {
            List<IElement> row = grids.get(i);
            for (int j = 0; j < tileCount; ++j) {
                if (i + j == 0) {
                    row.add(icon);
                }
                else if(i == tileCount - 1 && j == tileCount - 1) {
                    row.add(text);
                }
                else {
                    row.add(spacer);
                }
            }
        }
        return helper;
    }

    public static ElementGroup placeArrow(List<List<IElement>> grids)
    {
        return placeArrowProgress(grids, 1);
    }

    public static ElementGroup placeArrowProgress(List<List<IElement>> grids, float progress)
    {
        ElementGroup helper = new ElementGroup();
        int maxRow = grids.size();
        if (maxRow == 0) return helper;

        int arrowRow = (maxRow + 1) / 2 - 1;

        IElement arrow = helper.progressArrow(progress);
        if (maxRow % 2 == 0) {
            float delta = -arrow.getCachedSize().y / 2 + getTotalHeight(grids) / 2;
            arrow.translate(new Vec2(0, delta));
        }

        for (int i = 0; i < maxRow; ++i) {
            if (i == arrowRow) {
                grids.get(i).add(arrow);
            }
            else {
                grids.get(i).add(helper.spacer(arrow.getSize()));
            }
        }

        return helper;
    }

    public static void addGridsToTooltip(ITooltip tooltip, List<List<IElement>> grids)
    {
        for (List<IElement> row : grids) {
            tooltip.add(row);
        }
    }

    public static IElement translateDelta(IElement element, float x, float y)
    {
        return translateDelta(element, new Vec2(x, y));
    }

    /**
     * @see Element#translate(Vec2) will reset the translation,
     * translateDelta will only translate the element based on
     * its original position.
     */
    public static IElement translateDelta(IElement element, Vec2 delta)
    {
        return element.translate(element.getTranslation().add(delta));
    }

    public static class ElementGroup implements IElementHelper
    {
        private final IElementHelper helper;
        public Set<IElement> elements = new HashSet<>();

        ElementGroup() {
            this(null);
        }

        ElementGroup(IElementHelper helper) {
            this.helper = helper;
        }

        public IElement track(IElement element) {
            elements.add(element);
            return element;
        }

        public ElementGroup translateDelta(float x, float y)
        {
            return translateDelta(new Vec2(x, y));
        }

        public ElementGroup translateDelta(Vec2 delta)
        {
            for (IElement element : elements) {
                JadeHelper.translateDelta(element, delta);
            }

            return this;
        }

        @Override
        public IElement text(Component component) {
            IElement ret;
            if (helper != null) {
                ret = helper.text(component);
            }
            else {
                ret = new TextElement(component);
            }
            return track(ret);
        }

        @Override
        public IElement spacer(int i, int i1) {
            IElement ret;
            if (helper != null) {
                ret = helper.spacer(i, i1);
            }
            else {
                ret = new SpacerElement(new Vec2(i, i1));
            }
            return track(ret);
        }

        public IElement spacer(Vec2 vec) {
            IElement ret = new SpacerElement(vec);
            return track(ret);
        }

        @Override
        public IElement item(ItemStack itemStack) {
            IElement ret;
            if (helper != null) {
                ret = helper.item(itemStack);
            }
            else {
                ret = ItemStackElement.of(itemStack);
            }
            return track(ret);
        }

        @Override
        public IElement item(ItemStack itemStack, float v) {
            IElement ret;
            if (helper != null) {
                ret = helper.item(itemStack, v);
            }
            else {
                ret = ItemStackElement.of(itemStack, v);
            }
            return track(ret);
        }

        @Override
        public IElement item(ItemStack itemStack, float v, @Nullable String s) {
            IElement ret;
            if (helper != null) {
                ret = helper.item(itemStack, v, s);
            }
            else {
                ret = ItemStackElement.of(itemStack, v, s);
            }
            return track(ret);
        }

        @Override
        public IElement smallItem(ItemStack itemStack) {
            return null;
        }

        @Override
        public IElement fluid(JadeFluidObject jadeFluidObject) {
            IElement ret;
            if (helper != null) {
                ret = helper.fluid(jadeFluidObject);
            }
            else {
                ret = new FluidStackElement(jadeFluidObject);
            }
            return track(ret);
        }

        @SuppressWarnings("all")
        @Override
        public IElement progress(float v, @Nullable Component component, IProgressStyle style, @Nullable IBorderStyle borderStyle) {
            IElement ret;
            if (helper != null) {
                ret = helper.progress(v, component, style, borderStyle);
            }
            else {
                var boxStyle = BoxStyle.DEFAULT;
                if(borderStyle!=null && borderStyle instanceof BorderStyle borderStyle1){
                    boxStyle.borderColor = borderStyle1.color;
                    boxStyle.borderWidth = borderStyle1.width;
                }
                ret = new ProgressElement(v, component, (ProgressStyle)style, boxStyle, false);
            }
            return track(ret);
        }

        @Override
        public IElement progress(float v, @Nullable Component component, IProgressStyle iProgressStyle, IBoxStyle iBoxStyle, boolean b) {
            IElement ret;
            if (helper != null) {
                ret = helper.progress(v, component, iProgressStyle, iBoxStyle, b);
            }
            else {
                ret = new ProgressElement(v, component, iProgressStyle, iBoxStyle, b);
            }
            return track(ret);
        }

        @SuppressWarnings("all")
        @Override
        public IElement box(ITooltip tooltip, @Nullable IBorderStyle border) {
            IElement ret;
            if (helper != null) {
                ret = helper.box(tooltip, border);
            }
            else {
                ret = new BoxElement((Tooltip)tooltip, (IBoxStyle) border);
            }
            return track(ret);
        }

        @Override
        public IBoxElement box(ITooltip iTooltip, IBoxStyle iBoxStyle) {
            IElement ret;
            if (helper != null) {
                ret = helper.box(iTooltip, iBoxStyle);
            }
            else {
                ret = new BoxElement((Tooltip)iTooltip, iBoxStyle);
            }
            return (IBoxElement) track(ret);
        }

        @Override
        public ITooltip tooltip() {
            if (helper != null) {
                return helper.tooltip();
            }
            else {
                return null;
            }
        }

        @SuppressWarnings("all")
        @Override
        public IBorderStyle borderStyle() {
            if (helper != null) {
                return helper.borderStyle();
            }
            else {
                return new BorderStyle();
            }
        }

        @Override
        public IProgressStyle progressStyle() {
            if (helper != null) {
                return helper.progressStyle();
            }
            else {
                return new ProgressStyle();
            }
        }

        public IElement progressArrow(float progress) {
            return track(new ProgressArrowElement(progress));
        }
    }
}
