package com.zodiacgroup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import com.zodiacgroup.main.MainApp; 

public class BaseController {
	protected String currentPage;
	protected MainApp mainApp; 

	protected void initializeCurrentPageStyle(Label lblHome, Label lblCustomer, Label lblMechanic, 
	        Label lblSalesRep, Label lblInventory, Label lblPayment, Label lblViewReport) {
	    if (currentPage != null) {
	        switch (currentPage) {
	            case "home":
	                if (lblHome != null)
	                    lblHome.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "customer":
	                if (lblCustomer != null)
	                    lblCustomer.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "mechanic":
	                if (lblMechanic != null)
	                    lblMechanic.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "salesRep":
	                if (lblSalesRep != null)
	                    lblSalesRep.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "inventory":
	                if (lblInventory != null)
	                    lblInventory.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "payment":
	                if (lblPayment != null)
	                    lblPayment.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	            case "viewReport":
	                if (lblViewReport != null)
	                    lblViewReport.setStyle("-fx-font-weight: bold; -fx-underline: true;");
	                break;
	        }
	    }
	}

	protected void navigateToPage(MouseEvent event, String fxmlPath, String title, String pageName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();

			Object controller = loader.getController();
			if (controller instanceof BaseController) {
				((BaseController) controller).setCurrentPage(pageName);
				((BaseController) controller).setMainApp(this.mainApp); 
			}

			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			stage.show();
		} catch (Exception e) {
			System.err.println("Error loading " + title + " page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected void navigateToPage(ActionEvent event, String fxmlPath, String title, String pageName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();

			Object controller = loader.getController();
			if (controller instanceof BaseController) {
				((BaseController) controller).setCurrentPage(pageName);
				((BaseController) controller).setMainApp(this.mainApp); 
			}

			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));

			stage.show();

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();
		} catch (Exception e) {
			System.err.println("Error loading " + title + " page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected void styleCurrentPage(Label label, String pageName) {
		if (label != null && label.getStyle() == null) {
			if (currentPage != null && currentPage.equals(pageName)) {
				label.setStyle("-fx-font-weight: bold; -fx-underline: true;");
			} else {
				label.setStyle("-fx-font-weight: normal; -fx-underline: false;");
			}
		}
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	protected void handleLogout(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zodiacgroup/view/login.fxml"));
			Parent root = loader.load();

			LoginController controller = loader.getController();
			controller.setMainApp(mainApp);

			Stage stage = new Stage();
			stage.setTitle("Login");
			stage.setScene(new Scene(root));

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();

			stage.show();
		} catch (Exception e) {
			System.err.println("Error loading login page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected void configureMenuBasedOnRole(Label lblHome, Label lblCustomer, Label lblPayment, Label lblInventory,
			Label lblSalesRep, Label lblManager, Label lblViewReport, Label lblLogout, String currentRole) {
		if (lblHome != null)
			lblHome.setVisible(true);
		if (lblCustomer != null)
			lblCustomer.setVisible(true);
		if (lblPayment != null)
			lblPayment.setVisible(true);
		if (lblLogout != null)
			lblLogout.setVisible(true);

		if (lblInventory != null)
			lblInventory.setVisible(false);
		if (lblSalesRep != null)
			lblSalesRep.setVisible(false);
		if (lblManager != null)
			lblManager.setVisible(false);
		if (lblViewReport != null)
			lblViewReport.setVisible(false);

		if ("ADMIN".equals(currentRole)) {
			if (lblInventory != null)
				lblInventory.setVisible(true);
			if (lblSalesRep != null)
				lblSalesRep.setVisible(true);
			if (lblManager != null)
				lblManager.setVisible(true);
			if (lblViewReport != null)
				lblViewReport.setVisible(true);
		}
	}

	protected void setupNavigation(Label lblHome, Label lblCustomer, Label lblPayment, Label lblInventory,
			Label lblSalesRep, Label lblManager, Label lblViewReport, Label lblLogout, String currentRole) {
		if (lblHome != null) {
			lblHome.setOnMouseClicked(e -> {
				try {
					mainApp.showDashboard(currentRole);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}

		if (lblCustomer != null) {
			lblCustomer.setOnMouseClicked(e -> {
				try {
					mainApp.showCustomerView(currentRole);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}

		if (lblPayment != null) {
			lblPayment.setOnMouseClicked(e -> {
				try {
					mainApp.showPaymentView(currentRole);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}

		if (lblLogout != null) {
			lblLogout.setOnMouseClicked(e -> {
				try {
					mainApp.showLoginView();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}

		if ("ADMIN".equals(currentRole)) {
			if (lblInventory != null) {
				lblInventory.setOnMouseClicked(e -> {
					try {
						mainApp.showInventoryView(currentRole);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
			}

			if (lblSalesRep != null) {
				lblSalesRep.setOnMouseClicked(e -> {
					try {
						mainApp.showSalesRepView(currentRole);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
			}

			if (lblManager != null) {
				lblManager.setOnMouseClicked(e -> {
					try {
						mainApp.showMechanicView(currentRole);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
			}

			if (lblViewReport != null) {
				lblViewReport.setOnMouseClicked(e -> {
					try {
						mainApp.showReportView(currentRole);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
			}
		}
	}
}