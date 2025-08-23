package mcdonald.api.main;

import javax.swing.JPanel;

import mcdonald.view.panels.home.admin.BlockUserPanel;
import mcdonald.view.panels.home.admin.RegisterStaffPanel;
import mcdonald.view.panels.home.admin.StaffDetailPanel;
import mcdonald.view.panels.home.admin.StaffMenuPanel;
import mcdonald.view.panels.home.client.ClientHomePanel;
import mcdonald.view.panels.home.client.CreateOrderPanel;
import mcdonald.view.panels.home.client.ViewProductInfo;
import mcdonald.view.panels.home.staff.OrderDetailsPanel;
import mcdonald.view.panels.home.staff.StaffHomePanel;
import mcdonald.view.panels.main.LoginPanel;
import mcdonald.view.panels.main.RegisterPanel;

public enum MainPanels {
    LOGIN(new LoginPanel()),
    REGISTER(new RegisterPanel()),
    REGISTER_STAFF(new RegisterStaffPanel()),
    CLIENT_HOME(new ClientHomePanel()),
    STAFF_HOME(new StaffHomePanel()),
    STAFF_MENU(new StaffMenuPanel()),
    STAFF_DETAILS(new StaffDetailPanel()),
    CREATE_ORDER(new CreateOrderPanel()),
    ORDER_DETAILS(new OrderDetailsPanel()),
    BLOCK_USERS(new BlockUserPanel()),
    VIEW_PRODUCT_INFO(new ViewProductInfo());

    private final JPanel panel;

    MainPanels(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getPanel() {
        return panel;
    }
}
