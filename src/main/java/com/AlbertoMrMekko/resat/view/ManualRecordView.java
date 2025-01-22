package com.AlbertoMrMekko.resat.view;

import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

@Component
public class ManualRecordView
{
    private final BorderPane root;

    private final ViewManager viewManager;

    public ManualRecordView(final BorderPane root, final ViewManager viewManager)
    {
        this.root = root;
        this.viewManager = viewManager;
    }

    public void show()
    {
        // TODO .
    }
}
