package dev.zemco.mediatoasts;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import static com.google.common.base.Preconditions.checkArgument;

public class MediaInfoToast implements Toast {

    private static final Identifier TEXTURE = Identifier.ofVanilla("toast/recipe");

    // R: 80, G: 0, B: 80, A: 255 in ARGB format
    private static final int ARTIST_COLOR = -11534256;
    // R: 0, G: 0, B: 0, A: 255 in ARGB format
    private static final int TITLE_COLOR = -16777216;

    private final MediaInfo mediaInfo;
    private Visibility visibility;

    private long startTime;
    private boolean firstUpdateHandled;

    public MediaInfoToast(MediaInfo mediaInfo) {
        checkArgument(mediaInfo != null, "Media info cannot be null!");
        this.mediaInfo = mediaInfo;
        this.visibility = Visibility.HIDE;
    }

    @Override
    public void update(ToastManager manager, long time) {
        if (!this.firstUpdateHandled) {
            this.startTime = time;
            this.firstUpdateHandled = true;
        }

        this.visibility = time - this.startTime < 5000.d * manager.getNotificationDisplayTimeMultiplier()
            ? Visibility.SHOW
            : Visibility.HIDE;
    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {
        context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURE, 0, 0, this.getWidth(), this.getHeight());
        context.drawText(textRenderer, this.mediaInfo.artist(), 30, 7, ARTIST_COLOR, false);
        context.drawText(textRenderer, this.mediaInfo.title(), 30, 18, TITLE_COLOR, false);
        context.drawItem(Items.JUKEBOX.getDefaultStack(), 8, 8);
    }

    @Override
    public Visibility getVisibility() {
        return this.visibility;
    }

}
