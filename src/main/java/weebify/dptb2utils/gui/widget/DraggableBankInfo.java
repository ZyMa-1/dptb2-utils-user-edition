package weebify.dptb2utils.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import weebify.dptb2utils.DPTB2Utils;

public class DraggableBankInfo extends ClickableWidget {

    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    public float relX, relY;

    public DraggableBankInfo(float relX, float relY) {
        super(
                0,
                0,
                120,
                MinecraftClient.getInstance().textRenderer.fontHeight * 2 + 11,
                Text.empty()
        );

        this.relX = relX;
        this.relY = relY;
    }

    public void updatePosition(int screenWidth, int screenHeight) {
        setX((int) (screenWidth * relX));
        setY((int) (screenHeight * relY));
    }

    @Override
    protected void renderWidget(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer renderer = mc.textRenderer;
        DPTB2Utils mod = DPTB2Utils.getInstance();

        Text jackpotText = Text.empty()
                .append(Text.literal("Jackpot: "))
                .append(Text.literal("1,000,000").formatted(Formatting.GOLD))
                .append(Text.literal(" ⛂").formatted(Formatting.GOLD));

        Text tntText = Text.literal(
                "Players with TNT: 0"
        );

        int widgetWidth = Math.max(
                renderer.getWidth(jackpotText),
                renderer.getWidth(tntText)
        );

        int widgetHeight =
                renderer.fontHeight * 2 + 11;

        if (widgetWidth + 8 != getWidth()) {
            setWidth(widgetWidth + 8);
        }

        if (widgetHeight != getHeight()) {
            setHeight(widgetHeight);
        }

        if (mod.getBoolConfig("bankInfo.renderBackground")) {
            context.fill(
                    getX(),
                    getY(),
                    getX() + getWidth(),
                    getY() + getHeight(),
                    0x63000000
            );
        }

        boolean shadow =
                mod.getBoolConfig("bankInfo.textShadow");

        context.drawText(
                renderer,
                jackpotText,
                getX() + 4,
                getY() + 4,
                Colors.WHITE,
                shadow
        );

        context.drawText(
                renderer,
                tntText,
                getX() + 4,
                getY() + 7 + renderer.fontHeight,
                Colors.WHITE,
                shadow
        );
    }

    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {
        if (isMouseOver(mouseX, mouseY) && button == 0) {
            dragging = true;
            dragOffsetX = (int) (mouseX - getX());
            dragOffsetY = (int) (mouseY - getY());
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(
            double mouseX,
            double mouseY,
            int button
    ) {
        if (dragging && button == 0) {
            dragging = false;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(
            double mouseX,
            double mouseY,
            int button,
            double dx,
            double dy
    ) {
        if (!dragging) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        int newX = (int) (mouseX - dragOffsetX);
        int newY = (int) (mouseY - dragOffsetY);

        int screenWidth =
                client.getWindow().getScaledWidth();
        int screenHeight =
                client.getWindow().getScaledHeight();

        setX(newX);
        setY(newY);

        relX = (float) newX / screenWidth;
        relY = (float) newY / screenHeight;

        return true;
    }

    @Override
    protected void appendClickableNarrations(
            NarrationMessageBuilder builder
    ) {}
}