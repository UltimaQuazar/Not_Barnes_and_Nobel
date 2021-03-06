package Controller_Pack;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import Gate_Pack.AuthorTableGateway;
import Gate_Pack.BookTableGateway;
import Main_Pack.Validation;
import Model_Pack.ViewType;
import Model_Pack.Book;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class BookController {
	private AuthorTableGateway adb;
	private static Logger logger = LogManager.getLogger();
	private List<Book> books;
	private BookTableGateway bookTableGateway;
	private Validation validation;
	ObservableList<Book> items;
	@FXML private ListView<Book> listView;
	@FXML private Button filter;
	@FXML private TextField pubField;
	@FXML private TextField titleField;
	@FXML private TextField dateField;

	public BookController(List<Book> books) throws SQLException {
		// TODO Auto-generated constructor stub
		this.books = books;
		this.bookTableGateway = new BookTableGateway();
		this.validation = new Validation();
	}
	
	@FXML private void onMouseClick(MouseEvent action) throws IOException, SQLException, ParseException{
		if(action.getClickCount() == 2){
			Book book = listView.getSelectionModel().getSelectedItem();
			if(book == null){
				return;
			}
			Object source = action.getSource();
			if(source == listView){
				logger.info("clicked on " + book);
            	MasterController.getInstance().changeView(ViewType.BOOK_DETAIL, book);

			}
		}
	}

	@FXML private void onButtonPress(ActionEvent action) throws IOException, SQLException, ParseException{
	    String title = "", date = "", publisher = "";
		Object source = action.getSource();
		adb = new AuthorTableGateway();
		//todo filter is going to have things correctly filter based on the text input
		if(source == filter){
			if(validation.publishValid(pubField.getText()) &&
					validation.titleValid(titleField.getText()) &&
					validation.dateValidation(dateField.getText())){
				if (!pubField.getText().isEmpty()){
					logger.info(pubField.getText());
					publisher = pubField.getText();
				}
				if(!dateField.getText().isEmpty()){
					date = dateField.getText();
				}
				if(!titleField.getText().isEmpty()){
					title = titleField.getText();
				}

				books = bookTableGateway.filter(title, publisher, date);
				items = listView.getItems();
				logger.info(listView.getItems());
				items.clear();
				logger.info(items.toString() );
				for(Book book: books){
					items.add(book);
				}
				books.clear();
			}
		}

	}


	public void initialize() throws SQLException{
		ObservableList<Book> items = listView.getItems();
		for(Book c : books) {
			items.add(c);
		}
		books.clear();
		
	}
}
