package web.prog.client;

import java.util.ArrayList;

import web.prog.shared.Customer;
import web.prog.shared.InventoryItem;
import web.prog.shared.InventoryTable;
import web.prog.shared.LoginVerifier;
import web.prog.shared.POSTransaction;
import web.prog.shared.TransactionTable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWT_POS implements EntryPoint {

	final Button loginButton = new Button("Login");
	final TextBox usernameField = new TextBox();
	final PasswordTextBox passwordField = new PasswordTextBox();
	final Label errorLabel = new Label();
	final Button logoutButton = new Button("Logout");

	final Button createInvButton = new Button("Create Inventory Item");
	final Button deleteInvButton = new Button("Delete Inventory Item");
	final Button editInvButton = new Button("Edit Inventory Item");
	final Button createCustRecButton = new Button("Create Customer Record");
	final Button createPOSTranButton = new Button("Create POS Transaction");
	final Button listTranButton = new Button("List Transactions");

	// Sub fields for creating a new inventory item
	final TextBox descriptionField = new TextBox();
	final TextBox skuField = new TextBox();
	final TextBox pictureField = new TextBox();
	final TextBox priceField = new TextBox();
	final TextBox inventoryField = new TextBox();
	final Button createInvSubmitButton = new Button("Submit");

	// Sub fields for creating a new customer
	final TextBox custFirstName = new TextBox();
	final TextBox custLastName = new TextBox();
	final TextBox custID = new TextBox();
	final Button createCustSubmitButton = new Button("Submit");

	// Sub fields for POS Transactions
	final ListBox customerListBox = new ListBox();
	final ListBox itemListBox = new ListBox();
	final Button itemAddButton = new Button("Add");
	final Button createPOSTransactionSubmitButton = new Button("Submit");
	final Label createPOSTransactionTotal = new Label("0.00");
	final ListBox createPOSTransactionPaid = new ListBox();
	final ListBox transCustomerListBox = new ListBox();
	final Button transCustomerButton = new Button("Filter");
	public ArrayList<InventoryItem> invList = new ArrayList<InventoryItem>();

	public ArrayList<Customer> customerList = new ArrayList<Customer>();

	public ArrayList<InventoryItem> transactionItemList = new ArrayList<InventoryItem>();

	public InventoryTable editTable = new InventoryTable();
	public InventoryTable deleteTable = new InventoryTable();

	public TransactionTable transactionTable = new TransactionTable();

	int currentNumberOfItems = 0;
	int totalNum = 0;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		hideAll();
		hideFirstSubMenu();
		hideSecondSubMenu();
		hideThirdSubMenu();
		hideSixthSubMenu();
		setVisibilityForAllFields();

		setLoginVisible(true);
		setLogoutVisible(false);
		setMenuVisible(false);

		logoutButton.setWidth("188px");
		passwordField.setWidth("176px");
		passwordField.setHeight("18px");
		loginButton.setWidth("188px");

		//usernameField.setText("username");
		//passwordField.setText("password");

		createInvButton.setWidth("188px");
		deleteInvButton.setWidth("188px");
		editInvButton.setWidth("188px");
		createCustRecButton.setWidth("188px");
		createPOSTranButton.setWidth("188px");
		listTranButton.setWidth("188px");

		// Add login information to Root Panel
		RootPanel.get("usernameField").add(usernameField);
		RootPanel.get("passwordField").add(passwordField);
		RootPanel.get("loginButton").add(loginButton);
		RootPanel.get("errorLabel").add(errorLabel);
		RootPanel.get("logoutButton").add(logoutButton);

		// Add menu information to Root Panel
		RootPanel.get("createInvButton").add(createInvButton);
		RootPanel.get("deleteInvButton").add(deleteInvButton);
		RootPanel.get("editInvButton").add(editInvButton);
		RootPanel.get("createCustRecButton").add(createCustRecButton);
		RootPanel.get("createPOSTranButton").add(createPOSTranButton);
		RootPanel.get("listTranButton").add(listTranButton);

		// Add sub menu information for create inventory to Root Panel
		RootPanel.get("descriptionField").add(descriptionField);
		RootPanel.get("skuField").add(skuField);
		RootPanel.get("pictureField").add(pictureField);
		RootPanel.get("priceField").add(priceField);
		RootPanel.get("inventoryField").add(inventoryField);
		RootPanel.get("createInvSubmitButton").add(createInvSubmitButton);

		// Add sub menu information for create customer to Root Panel
		RootPanel.get("custFirstName").add(custFirstName);
		RootPanel.get("custLastName").add(custLastName);
		RootPanel.get("custID").add(custID);
		RootPanel.get("createCustSubmitButton").add(createCustSubmitButton);

		// Add sub menu information for create customer to Root Panel
		RootPanel.get("customerListBox").add(customerListBox);
		RootPanel.get("itemListBox").add(itemListBox);
		RootPanel.get("itemAddButton").add(itemAddButton);
		RootPanel.get("createPOSTransactionSubmitButton").add(
				createPOSTransactionSubmitButton);
		RootPanel.get("createPOSTransactionTotal").add(
				createPOSTransactionTotal);
		RootPanel.get("createPOSTransactionPaid").add(createPOSTransactionPaid);
		RootPanel.get("transCustomerListBox").add(transCustomerListBox);
		RootPanel.get("transCustomerButton").add(transCustomerButton);

		createPOSTransactionPaid.addItem("PAID");
		createPOSTransactionPaid.addItem("NOT PAID");
		createPOSTransactionPaid.setVisibleItemCount(1);

		// Focus the cursor on the name field when the app loads
		usernameField.setFocus(true);

		// Create a handler for the loginButton
		class LoginHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the loginButton.
			 */
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				String username = usernameField.getText();
				String password = passwordField.getText();
				if (!LoginVerifier.areCredentialsGood(username, password)) {
					errorLabel
							.setText("The username and/or password is not correct");
					return;
				} else {
					hideAll();
					setLogoutVisible(true);
					setMenuVisible(true);
				}
			}
		}

		// Add a handler to verify the username and password
		LoginHandler loginHandler = new LoginHandler();
		loginButton.addClickHandler(loginHandler);

		// Create a handler for the logoutButton
		class LogoutHandler implements ClickHandler {

			public void onClick(ClickEvent event) {
				hideAll();
				setLoginVisible(true);
				setMenuVisible(false);
				setLogoutVisible(false);
			}
		}

		// Add a handler to log the user out
		LogoutHandler logoutHandler = new LogoutHandler();
		logoutButton.addClickHandler(logoutHandler);

		class CreateInvButtonHandler implements ClickHandler {
			int createInvCount = 0;

			public void onClick(ClickEvent event) {
				createInvCount++;

				createInvButton.setVisible(true);
				Document.get().getElementById("createInvButton").getStyle()
						.setDisplay(Display.BLOCK);

				if (createInvCount % 2 == 1) {
					showFirstSubMenu();
					hideMenu();
					Document.get().getElementById("createInvButton").getStyle()
							.setDisplay(Display.BLOCK);
				} else {
					hideFirstSubMenu();
					showMenu();
				}
			}
		}

		// Add a handler to the Create Inventory Button
		CreateInvButtonHandler createInvButtonHandler = new CreateInvButtonHandler();
		createInvButton.addClickHandler(createInvButtonHandler);

		class DeleteInvButtonHandler implements ClickHandler {
			int deleteInvCount = 0;

			public void onClick(ClickEvent event) {
				deleteInvCount++;

				deleteInvButton.setVisible(true);
				Document.get().getElementById("deleteInvButton").getStyle()
						.setDisplay(Display.BLOCK);

				if (deleteInvCount % 2 == 1) {
					hideMenu();
					Document.get().getElementById("deleteInvButton").getStyle()
							.setDisplay(Display.BLOCK);
					Document.get().getElementById("deleteInvTable").getStyle()
							.setDisplay(Display.BLOCK);

					deleteTable.tableDraw();

					RootPanel.get("deleteInvTable").add(deleteTable.table);
				} else {

					showMenu();
					Document.get().getElementById("deleteInvTable").getStyle()
							.setDisplay(Display.NONE);
				}
			}
		}

		// Add a handler to the Delete Inventory Button
		DeleteInvButtonHandler deleteInvButtonHandler = new DeleteInvButtonHandler();
		deleteInvButton.addClickHandler(deleteInvButtonHandler);

		class EditInvButtonHandler implements ClickHandler {
			int editInvCount = 0;

			public void onClick(ClickEvent event) {
				editInvCount++;

				editInvButton.setVisible(true);
				Document.get().getElementById("editInvButton").getStyle()
						.setDisplay(Display.BLOCK);

				if (editInvCount % 2 == 1) {
					hideMenu();
					Document.get().getElementById("editInvButton").getStyle()
							.setDisplay(Display.BLOCK);
					Document.get().getElementById("editInvTable").getStyle()
							.setDisplay(Display.BLOCK);

					editTable.tableDraw();

					RootPanel.get("editInvTable").add(editTable.table);

				} else {
					Document.get().getElementById("editInvTable").getStyle()
							.setDisplay(Display.NONE);
					showMenu();
				}
			}
		}

		// Add a handler to the Edit Inventory Button
		EditInvButtonHandler editInvButtonHandler = new EditInvButtonHandler();
		editInvButton.addClickHandler(editInvButtonHandler);

		class CreateCustRecButtonHandler implements ClickHandler {
			int createCustCount = 0;

			public void onClick(ClickEvent event) {
				createCustCount++;

				createCustRecButton.setVisible(true);
				Document.get().getElementById("createCustRecButton").getStyle()
						.setDisplay(Display.BLOCK);

				if (createCustCount % 2 == 1) {
					showSecondSubMenu();
					hideMenu();
					Document.get().getElementById("createCustRecButton")
							.getStyle().setDisplay(Display.BLOCK);
				} else {
					hideSecondSubMenu();
					showMenu();
				}
			}
		}

		// Add a handler to the Create Customer Record Button
		CreateCustRecButtonHandler createCustRecButtonHandler = new CreateCustRecButtonHandler();
		createCustRecButton.addClickHandler(createCustRecButtonHandler);

		class CreatePOSTranButtonHandler implements ClickHandler {
			int createPOSCount = 0;

			public void onClick(ClickEvent event) {
				createPOSCount++;

				createPOSTranButton.setVisible(true);
				Document.get().getElementById("createPOSTranButton").getStyle()
						.setDisplay(Display.BLOCK);

				if (createPOSCount % 2 == 1) {
					hideMenu();
					showThirdSubMenu();
					Document.get().getElementById("createPOSTranButton")
							.getStyle().setDisplay(Display.BLOCK);
				} else {
					showMenu();
					hideThirdSubMenu();
				}
			}
		}

		// Add a handler to the Create POS Transaction Button
		CreatePOSTranButtonHandler createPOSTranButtonHandler = new CreatePOSTranButtonHandler();
		createPOSTranButton.addClickHandler(createPOSTranButtonHandler);

		class ListTranButtonHandler implements ClickHandler {
			int listTranCount = 0;

			public void onClick(ClickEvent event) {
				listTranCount++;

				listTranButton.setVisible(true);
				Document.get().getElementById("listTranButton").getStyle()
						.setDisplay(Display.BLOCK);

				transCustomerListBox.clear();
				for(Customer customer: customerList) {
				transCustomerListBox.addItem(customer.getCustomerFirstName() + " "
						+ customer.getCustomerLastName());
				transCustomerListBox.setVisibleItemCount(1);
				}
				
				
				if (listTranCount % 2 == 1) {
					hideMenu();
					showSixthSubMenu();
					Document.get().getElementById("listTranButton").getStyle()
							.setDisplay(Display.BLOCK);
					Document.get().getElementById("transCustomerListBox").getStyle()
							.setDisplay(Display.BLOCK);
					Document.get().getElementById("transactionTable")
							.getStyle().setDisplay(Display.BLOCK);
					Document.get().getElementById("transCustomerButton").getStyle()
							.setDisplay(Display.BLOCK);

					transactionTable.tableDraw();

					RootPanel.get("transactionTable").add(transactionTable.table);
				} else {
					hideSixthSubMenu();
					Document.get().getElementById("transactionTable")
							.getStyle().setDisplay(Display.NONE);
					showMenu();
				}
			}
		}

		// Add a handler to the List Transactions Button
		ListTranButtonHandler listTranButtonHandler = new ListTranButtonHandler();
		listTranButton.addClickHandler(listTranButtonHandler);

		class CreateTransCustomerButtonHandler implements ClickHandler {

			public void onClick(ClickEvent event) {

				final int customerIndex = transCustomerListBox.getSelectedIndex();
				transactionTable.tableDraw(customerList.get(customerIndex).getCustomerID());
			}
		}
		
		CreateTransCustomerButtonHandler createTransCustomerButtonHandler = new CreateTransCustomerButtonHandler();
		transCustomerButton.addClickHandler(createTransCustomerButtonHandler);

		
		class CreateInvSubmitButtonHandler implements ClickHandler {

			public void onClick(ClickEvent event) {

				final String description = descriptionField.getText();
				final String sku = skuField.getText();
				final String pictureLink = pictureField.getText();
				final String price = priceField.getText();
				final String inv = inventoryField.getText();

				final InventoryItem invItem = new InventoryItem(description,
						sku, pictureLink, price, inv);

				invList.add(invItem);
				editTable.invList.add(invItem);
				deleteTable.invList.add(invItem);

				double priceNum = Double.parseDouble(price);

				currentNumberOfItems++;
				totalNum++;

				itemListBox
						.addItem(invItem.getDescription() + " - " + priceNum);
				itemListBox.setVisibleItemCount(1);

				descriptionField.setText("");
				skuField.setText("");
				pictureField.setText("");
				priceField.setText("");
				inventoryField.setText("");
			}

		}

		CreateInvSubmitButtonHandler createInvSubmitButtonHandler = new CreateInvSubmitButtonHandler();
		createInvSubmitButton.addClickHandler(createInvSubmitButtonHandler);

		class CreateCustSubmitButtonHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				final String firstName = custFirstName.getText();
				final String lastName = custLastName.getText();
				final String id = custID.getText();

				int idNum = Integer.parseInt(id);

				final Customer customer = new Customer(firstName, lastName,
						idNum);

				customerList.add(customer);

				customerListBox.addItem(customer.getCustomerFirstName() + " "
						+ customer.getCustomerLastName());
				customerListBox.setVisibleItemCount(1);

				custFirstName.setText("");
				custLastName.setText("");
				custID.setText("");
			}

		}

		CreateCustSubmitButtonHandler createCustSubmitButtonHandler = new CreateCustSubmitButtonHandler();
		createCustSubmitButton.addClickHandler(createCustSubmitButtonHandler);

		class CreatePOSTransactionSubmitButtonHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				final int customerIndex = customerListBox.getSelectedIndex();
				final String total = createPOSTransactionTotal.getText();
				final int paidIndex = createPOSTransactionPaid
						.getSelectedIndex();

				boolean paid = false;
				double totalNum = Double.parseDouble(total);

				String customerText = customerListBox
						.getItemText(customerIndex);
				String paidText = createPOSTransactionPaid
						.getItemText(paidIndex);
				if (paidText.equals("PAID")) {
					paid = true;
				} else if (paidText.equals("NOT PAID")) {
					paid = false;
				}
				String[] parts = customerText.split("\\ ");

				String firstName = parts[0].trim();

				for (Customer c : customerList) {
					if (firstName.equals(c.getCustomerFirstName())) {
						POSTransaction transaction = new POSTransaction(c,
								transactionItemList, totalNum, paid);
						transactionTable.transactionList.add(transaction);
						break;
					}
				}
				
				createPOSTransactionTotal.setText("0.00");
				transactionItemList = new ArrayList<InventoryItem>();
			}

		}

		CreatePOSTransactionSubmitButtonHandler createPOSTransactionSubmitButtonHandler = new CreatePOSTransactionSubmitButtonHandler();
		createPOSTransactionSubmitButton
				.addClickHandler(createPOSTransactionSubmitButtonHandler);

		class ItemAddButtonHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				final int itemIndex = itemListBox.getSelectedIndex();

				String itemText = itemListBox.getItemText(itemIndex);

				String[] parts = itemText.split("\\-");

				String description = parts[0].trim();
				String price = parts[1].trim();

				for (InventoryItem i : invList) {
					if (description.equals(i.getDescription())) {
						transactionItemList.add(i);
					}
				}

				double priceNum = Double.parseDouble(price);

				final String total = createPOSTransactionTotal.getText();

				double totalNum = Double.parseDouble(total);

				totalNum = totalNum + priceNum;

				createPOSTransactionTotal.setText("" + totalNum);
			}
		}

		ItemAddButtonHandler itemAddButtonHandler = new ItemAddButtonHandler();
		itemAddButton.addClickHandler(itemAddButtonHandler);
	}

	private void hideAll() {
		Document.get().getElementById("login").getStyle()
				.setVisibility(Visibility.HIDDEN);

		Document.get().getElementById("menu").getStyle()
				.setVisibility(Visibility.HIDDEN);

		Document.get().getElementById("logout").getStyle()
				.setVisibility(Visibility.HIDDEN);
	}

	private void setLoginVisible(Boolean visible) {
		if (visible == true) {
			Document.get().getElementById("login").getStyle()
					.setVisibility(Visibility.VISIBLE);
		} else if (visible == false) {
			Document.get().getElementById("login").getStyle()
					.setVisibility(Visibility.HIDDEN);
		}
	}

	private void setMenuVisible(Boolean visible) {
		if (visible == true) {
			Document.get().getElementById("menu").getStyle()
					.setVisibility(Visibility.VISIBLE);
		} else if (visible == false) {
			Document.get().getElementById("menu").getStyle()
					.setVisibility(Visibility.HIDDEN);
		}
	}

	private void setLogoutVisible(Boolean visible) {
		if (visible == true) {
			Document.get().getElementById("logout").getStyle()
					.setVisibility(Visibility.VISIBLE);
		} else if (visible == false) {
			Document.get().getElementById("logout").getStyle()
					.setVisibility(Visibility.HIDDEN);
		}
	}

	private void hideMenu() {
		Document.get().getElementById("createInvButton").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("deleteInvButton").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("editInvButton").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("createCustRecButton").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("createPOSTranButton").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("listTranButton").getStyle()
				.setDisplay(Display.NONE);
	}

	private void showMenu() {

		Document.get().getElementById("createInvButton").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("deleteInvButton").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("editInvButton").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("createCustRecButton").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("createPOSTranButton").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("listTranButton").getStyle()
				.setDisplay(Display.BLOCK);
	}

	private void showFirstSubMenu() {
		Document.get().getElementById("sub1").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub2").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub3").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub4").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub5").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub6").getStyle()
				.setDisplay(Display.BLOCK);
	}

	private void hideFirstSubMenu() {
		Document.get().getElementById("sub1").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub2").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub3").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub4").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub5").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub6").getStyle()
				.setDisplay(Display.NONE);
	}

	private void showSecondSubMenu() {
		Document.get().getElementById("sub7").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub8").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub9").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub10").getStyle()
				.setDisplay(Display.BLOCK);
	}

	private void hideSecondSubMenu() {
		Document.get().getElementById("sub7").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub8").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub9").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub10").getStyle()
				.setDisplay(Display.NONE);
	}

	private void hideThirdSubMenu() {
		Document.get().getElementById("sub11").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub12").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub13").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub14").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub15").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub16").getStyle()
				.setDisplay(Display.NONE);
	}

	private void showThirdSubMenu() {
		Document.get().getElementById("sub11").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub12").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub13").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub14").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub15").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub16").getStyle()
				.setDisplay(Display.BLOCK);
	}
	private void hideSixthSubMenu() {
		Document.get().getElementById("sub17").getStyle()
				.setDisplay(Display.NONE);
		Document.get().getElementById("sub18").getStyle()
		.setDisplay(Display.NONE);
	}
	
	private void showSixthSubMenu() {
		Document.get().getElementById("sub17").getStyle()
				.setDisplay(Display.BLOCK);
		Document.get().getElementById("sub18").getStyle()
		.setDisplay(Display.BLOCK);
	}
	

	private void setVisibilityForAllFields() {
		Document.get().getElementById("descriptionField").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("skuField").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("pictureField").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("priceField").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("inventoryField").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("custFirstName").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("custLastName").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("custID").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("customerListBox").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("itemListBox").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("createPOSTransactionTotal").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("createPOSTransactionPaid").getStyle()
				.setVisibility(Visibility.VISIBLE);
		Document.get().getElementById("transCustomerListBox").getStyle()
		.setVisibility(Visibility.VISIBLE);
	}
}