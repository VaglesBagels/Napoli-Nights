package com.example.napolinights.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Utility class for setting consistent stage properties across the application.
 */
public class StageConstants {

    // Constant for GST (Goods and Services Tax)
    public static final double GST = 0.1;  // Define GST as 10%

    // Default stage dimensions
    public static final double DEFAULT_MIN_WIDTH = 800;
    public static final double DEFAULT_MIN_HEIGHT = 600;

    // Paths to FXML files
    public static final String MENU_PAGE_PATH = "/view/Menu.fxml";
    public static final String ORDER_PAGE_PATH = "/view/Order.fxml";
    public static final String LOGIN_PAGE_PATH = "/view/Login.fxml";
    public static final String SIGN_UP_PAGE_PATH = "/view/SignUp.fxml";
    public static final String STAFF_LANDING_PAGE_PATH = "/view/StaffLandingPage.fxml";
    public static final String INCOMING_ORDERS_PAGE_PATH = "/view/IncomingOrders.fxml";
    public static final String VIEW_REPORTS_PAGE_PATH = "/view/ViewReports.fxml";

    // Page titles
    public static final String MENU_PAGE_TITLE = "View Menu";
    public static final String ORDER_PAGE_TITLE = "Order Here";
    public static final String LOGIN_PAGE_TITLE = "Login";
    public static final String SIGN_UP_PAGE_TITLE = "Sign Up";
    public static final String STAFF_LANDING_PAGE_TITLE = "Staff Landing Page";
    public static final String INCOMING_ORDERS_PAGE_TITLE = "Incoming Orders";
    public static final String VIEW_REPORTS_PAGE_TITLE = "Reports";

    /**
     * Sets the minimum width and height for the given stage.
     *
     * @param stage the stage to set the size for.
     */
    public static void setStageSize(Stage stage) {
        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
    }

    /**
     * Configures a stage with a consistent size and sets its scene.
     *
     * @param stage The stage to configure.
     * @param root  The root node of the scene.
     * @param title The title of the window.
     */
    public static void configureStage(Stage stage, Parent root, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        setStageSize(stage);
        stage.show();
    }

    /**
     * Opens a page based on the specified FXML file and sets the stage title.
     * Uses the StageConstants utility to ensure consistent stage size settings.
     *
     * @param fxmlPath Path to the FXML file.
     * @param stage The current stage where the scene will be set.
     * @param title The title of the window.
     */
    public static void openPage(String fxmlPath, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageConstants.class.getResource(fxmlPath));
            Parent page = loader.load();
            configureStage(stage, page, title);
        } catch (IOException e) {
            System.err.println("Error occurred while opening the " + title + " page.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the menu page in the provided stage.
     * @param stage The stage to display the menu page.
     */
    public static void openMenuPage(Stage stage) {
        openPage(MENU_PAGE_PATH, stage, MENU_PAGE_TITLE);
    }

    /**
     * Opens the order page in the provided stage.
     * @param stage The stage to display the order page.
     */
    public static void openOrderPage(Stage stage) {
        openPage(ORDER_PAGE_PATH, stage, ORDER_PAGE_TITLE);
    }

    /**
     * Opens the login page in the provided stage.
     * @param stage The stage to display the login page.
     */
    public static void openLoginPage(Stage stage) {
        openPage(LOGIN_PAGE_PATH, stage, LOGIN_PAGE_TITLE);
    }

    /**
     * Opens the sign up page in the provided stage.
     * @param stage The stage to display the sign up page.
     */
    public static void openSignUpPage(Stage stage) {
        openPage(SIGN_UP_PAGE_PATH, stage, SIGN_UP_PAGE_TITLE);
    }

    /**
     * Opens the Incoming Orders page in the provided stage.
     * @param stage The stage to display the incoming orders page.
     */
    public static void openIncomingOrdersPage(Stage stage) {
        openPage(INCOMING_ORDERS_PAGE_PATH, stage, INCOMING_ORDERS_PAGE_TITLE);
    }

    /**
     * Opens the View Reports page in the provided stage.
     * @param stage The stage to display the view reports page.
     */
    public static void openViewReportsPage(Stage stage) {
        openPage(VIEW_REPORTS_PAGE_PATH, stage, VIEW_REPORTS_PAGE_TITLE);
    }

    /**
     * Opens the Staff Landing page in the provided stage.
     * @param stage The stage to display the staff landing page.
     */
    public static void openStaffLandingPage(Stage stage) {
        openPage(STAFF_LANDING_PAGE_PATH, stage, STAFF_LANDING_PAGE_TITLE);
    }

}
