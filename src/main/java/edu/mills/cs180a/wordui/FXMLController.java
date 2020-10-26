package edu.mills.cs180a.wordui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

import edu.mills.cs180a.wordui.model.SampleData;
import edu.mills.cs180a.wordui.model.WordRecord;

public class FXMLController implements Initializable {
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

	private final ObservableList<WordRecord> wordRecordList = FXCollections.observableArrayList(/*Word.extractor*/);
	// Observable objects returned by extractor (applied to each list element) are listened for changes and
	// transformed into "update" change of ListChangeListener.

	private WordRecord selectedWordRecord;
	private final BooleanProperty modifiedProperty = new SimpleBooleanProperty(false);
	private ChangeListener<WordRecord> wordRecordChangeListener;


	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// Disable the Remove/Edit buttons if nothing is selected in the ListView control
		removeButton.disableProperty().bind(listView.getSelectionModel().
				selectedItemProperty().isNull());
		updateButton.disableProperty().bind(listView.getSelectionModel().
				selectedItemProperty().isNull() .or(modifiedProperty.not())
				.or(wordTextField.textProperty().isEmpty()
						.or(definitionTextArea.textProperty().isEmpty())));
		createButton.disableProperty().bind(listView.getSelectionModel().
				selectedItemProperty().isNotNull()
				.or(wordTextField.textProperty().isEmpty()
						.or(definitionTextArea.textProperty().isEmpty())));

		SampleData.fillSampleData(wordRecordList);

		// Sort list alphabetically.
		SortedList<WordRecord> sortedList = new SortedList<>(wordRecordList);
		sortedList.setComparator((p1, p2) -> p1.getWord().compareToIgnoreCase(p2.getWord()));
		listView.setItems(sortedList);

		listView.getSelectionModel().selectedItemProperty().addListener(
				wordRecordChangeListener = (observable, oldValue, newValue) -> {
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
				});

		// Pre-select the first item
		listView.getSelectionModel().selectFirst();

	}

	@FXML
	private void handleKeyAction(KeyEvent keyEvent) {
		modifiedProperty.set(true);
	}

	@FXML
	private void createButtonAction(ActionEvent actionEvent) {
		System.out.println("Create");
		WordRecord wordRecord = new WordRecord(
				wordTextField.getText(),
				Integer.parseInt(frequencyTextField.getText()), 
				definitionTextArea.getText());
		wordRecordList.add(wordRecord);
		// and select it
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
