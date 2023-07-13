package dev.zemco.mediatoasts;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.Items;

import static com.google.common.base.Preconditions.checkArgument;

public class MediaInfoToast implements Toast {

    // R: 80, G: 0, B: 80, A: 255 in ARGB format
    private static final int ARTIST_COLOR = -11534256;
    // R: 0, G: 0, B: 0, A: 255 in ARGB format
    private static final int TITLE_COLOR = -16777216;

    private final MediaInfo mediaInfo;

    public MediaInfoToast(MediaInfo mediaInfo) {
        checkArgument(mediaInfo != null, "Media info cannot be null!");
        this.mediaInfo = mediaInfo;
    }

    @Override
    public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        context.drawTexture(TEXTURE, 0, 0, 0, 32, this.getWidth(), this.getHeight());

        TextRenderer textRenderer = manager.getClient().textRenderer;
        context.drawText(textRenderer, this.mediaInfo.artist(), 30, 7, ARTIST_COLOR, false);
        context.drawText(textRenderer, this.mediaInfo.title(), 30, 18, TITLE_COLOR, false);
        context.drawItem(Items.JUKEBOX.getDefaultStack(), 8, 8);

        return (startTime >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }

}
