package com.example.fairyringhotkeys;

import lombok.RequiredArgsConstructor;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FairyRingHotkeysOverlay extends OverlayPanel
{
    private final FairyRingHotkeysConfig config;

    @Override
    public Dimension render(Graphics2D g)
    {
        panelComponent.getChildren().clear();
        panelComponent.setPreferredSize(new Dimension(200, 0));
        g.setFont(FontManager.getRunescapeSmallFont());

        panelComponent.getChildren().add(TitleComponent.builder().text("Fairy Favorites").build());
        panelComponent.getChildren().add(LineComponent.builder().left("1:").right(config.favorite1()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("2:").right(config.favorite2()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("3:").right(config.favorite3()).build());
        panelComponent.getChildren().add(LineComponent.builder().left("4:").right(config.favorite4()).build());
        return super.render(g);
    }
}