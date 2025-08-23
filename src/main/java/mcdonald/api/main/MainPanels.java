package mcdonald.api.main;

import javax.swing.JPanel;

import mcdonald.view.panels.home.client.ClientHomePanel;
import mcdonald.view.panels.home.client.CreateOrderPanel;
import mcdonald.view.panels.home.client.ViewProductInfo;
import mcdonald.view.panels.main.LoginPanel;
import mcdonald.view.panels.main.RegisterPanel;

public enum MainPanels {
    LOGIN(new LoginPanel()),
    REGISTER(new RegisterPanel()),
    CLIENT_HOME(new ClientHomePanel()),
    CREATE_ORDER(new CreateOrderPanel()),
    VIEW_PRODUCT_INFO(new ViewProductInfo());

    private final JPanel panel;

    MainPanels(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getPanel() {
        return panel;
    }
}
