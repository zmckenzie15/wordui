package edu.mills.cs180a.wordui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.mills.cs180a.wordui.model.SampleData;
import edu.mills.cs180a.wordui.model.WordRecord;
import edu.mills.cs180a.wordui.model.WordRecord.SortOrder;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class FXMLController implements Initializable {
	@FXML
	private ChoiceBox<WordRecord.SortOrder> sortChoiceBox;
	@FXML
	private TextField wordTextField;
	@FXML
	private TextField frequencyTextField;
	@FXML
	private TextArea definitionTextArea;
	@FXML
	private Button removeButton;
	@FXML
	private Button createButton;
	@FXML
	private Button updateButton;
	@FXML
	private ListView<WordRecord> listView;

	public static Callback<WordRecord, Observable[]> extractor = wr -> new Observable[] { wr.definitionProperty(),
			wr.frequencyProperty() };

	private final ObservableList<WordRecord> wordRecordList = FXCollections.observableArrayList(extractor);

	private WordRecord selectedWordRecord;
	private final BooleanProperty modifiedProperty = new SimpleBooleanProperty(false);
	private ChangeListener<WordRecord> wordRecordChangeListener = new WordRecordChangeListener();

	private class WordRecordChangeListener implements ChangeListener<WordRecord> {
		@Override
		public void changed(ObservableValue<? extends WordRecord> observable, WordRecord oldValue,
				WordRecord newValue) {
			System.out.println("Selected item: " + newValue);
			// newValue can be null if nothing is selected
			selectedWordRecord = newValue;
			modifiedProperty.set(false);
			if (newValue != null) {
				wordTextField.setText(selectedWordRecord.getWord());
				frequencyTextField.setText(String.valueOf(selectedWordRecord.getFrequency()));
				definitionTextArea.setText(selectedWordRecord.getDefinition());
			} else {
				wordTextField.setText("");
				frequencyTextField.setText("");
				definitionTextArea.setText("");
			}
		}
	}

	private void setSortOrder(SortOrder newValue) {
		SortedList<WordRecord> sortedList = new SortedList<>(wordRecordList);
		sortedList.setComparator(newValue.getComparator());
		listView.setItems(sortedList);
	}

	private class SortOrderChangeListener implements ChangeListener<WordRecord.SortOrder> {
		@Override
		public void changed(ObservableValue<? extends SortOrder> observable, SortOrder oldValue, SortOrder newValue) {
			setSortOrder(newValue);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Initialize the list.
		SampleData.fillSampleData(wordRecordList);

		configureButtons();
		populateChoiceBox();

		// Sort list alphabetically.

		addListeners();

		// Pre-select the first item.
		listView.getSelectionModel().selectFirst();
	}

	private void configureButtons() {
		// Disable the Remove button if nothing is selected in the ListView control.
		removeButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());

		// Disable the Update button if nothing is selected, no modifications have
		// been made, or any field is empty or invalid.
		updateButton.disableProperty()
				.bind(listView.getSelectionModel().selectedItemProperty().isNull().or(modifiedProperty.not())
						.or(wordTextField.textProperty().isEmpty()).or(definitionTextArea.textProperty().isEmpty()));

		// Disable the Create button if an existing entry is selected or any
		// field is empty or invalid.
		createButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNotNull()
				.or(wordTextField.textProperty().isEmpty()).or(definitionTextArea.textProperty().isEmpty()));
	}

	private void populateChoiceBox() {
		// https://stackoverflow.com/a/27801365/631051
		sortChoiceBox.setItems(FXCollections.observableArrayList(WordRecord.SortOrder.values()));
		sortChoiceBox.setValue(WordRecord.SortOrder.ALPHABETICALLY_FORWARD);
		setSortOrder(WordRecord.SortOrder.ALPHABETICALLY_FORWARD);
	}

	private void addListeners() {
		// Listen for records to be selected.
		listView.getSelectionModel().selectedItemProperty().addListener(wordRecordChangeListener);

		sortChoiceBox.getSelectionModel().selectedItemProperty().addListener(new SortOrderChangeListener());

	}

	@FXML
	private void handleKeyAction(KeyEvent keyEvent) {
		modifiedProperty.set(true);
	}

	@FXML
	private void createButtonAction(ActionEvent actionEvent) {
		System.out.println("Create");
		WordRecord wordRecord = new WordRecord(wordTextField.getText(), Integer.parseInt(frequencyTextField.getText()),
				definitionTextArea.getText());
		wordRecordList.add(wordRecord);
		// Select the new item.
		listView.getSelectionModel().select(wordRecord);
	}

	@FXML
	private void removeButtonAction(ActionEvent actionEvent) {
		System.out.println("Remove " + selectedWordRecord);
		wordRecordList.remove(selectedWordRecord);
	}

	@FXML
	private void updateButtonAction(ActionEvent actionEvent) {
		System.out.println("Update " + selectedWordRecord);
		WordRecord entry = listView.getSelectionModel().getSelectedItem();
		listView.getSelectionModel().selectedItemProperty().removeListener(wordRecordChangeListener);
		entry.setWord(wordTextField.getText());
		entry.setFrequency(Integer.parseInt(frequencyTextField.getText()));
		entry.setDefinition(definitionTextArea.getText());
		listView.getSelectionModel().selectedItemProperty().addListener(wordRecordChangeListener);
		modifiedProperty.set(false);
	}
}
