package dev.zemco.mediatoasts;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.text.Text;

// TODO: implement
public class UnsupportedPlatformScreen extends WarningScreen {

    private final Screen parent;

    public UnsupportedPlatformScreen(Screen parent) {
        super(Text.of("Header"), Text.of("Message"), Text.of("Narrated Text"));
        this.parent = parent;
    }

    @Override
    protected void initButtons(int yOffset) {

    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

}
